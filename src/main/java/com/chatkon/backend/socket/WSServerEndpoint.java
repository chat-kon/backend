package com.chatkon.backend.socket;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.action.handler.ActionHandlerFactory;
import com.chatkon.backend.exception.BadRequestException;
import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.service.user.SessionService;
import com.chatkon.backend.socket.config.CustomSpringConfigurator;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Component
@ServerEndpoint(
        value = "/",
        encoders = {ActionEncoder.class},
        decoders = {ActionDecoder.class},
        configurator = CustomSpringConfigurator.class
)
public class WSServerEndpoint {
    private static final Logger LOGGER = Logger.getLogger(WSServerEndpoint.class.getName());
    private static final Map<Long, Set<Session>> sessions = new HashMap<>();
    private final ActionHandlerFactory actionHandlerFactory;
    private final SessionService sessionService;
    @Value("${websocket.hostname}")
    private String hostname;
    @Value("${websocket.port}")
    private String port;
    @Value("${websocket.endpoint}")
    private String endpoint;

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("[SERVER]: Handshake successful! - Connected! - Session ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, Action action) {
        LOGGER.info("[FROM CLIENT]: " + action + ", Session ID: " + session.getId());

        Long userId = null;
        if (tokenRequired(action.getType())) {
            userId = sessionService.findUserSession(action.getToken()).getUser().getId();
        }

        ActionHandler<ActionDto> actionHandler = actionHandlerFactory.getHandler(action.getType());
        ActionResult result = actionHandler.handle(userId, action.getDto());

        if (!tokenRequired(action.getType())) {
            session.getAsyncRemote().sendObject(result.getAction());
        } else {
            sessions.putIfAbsent(userId, new HashSet<>());
            sessions.get(userId).add(session);
        }

        for (long receiverId : result.getReceivers()) {
            if (!sessions.containsKey(receiverId)) {
                continue; // offline user
            }
            sessions.get(receiverId).forEach(receiver ->
                    receiver.getAsyncRemote().sendObject(result.getAction()));
        }
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer buffer) {
        LOGGER.info("[FROM CLIENT]: " + "input buffer " + buffer.get(0) + ", Session ID: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable err) {
        LOGGER.warning("[SERVER]: Error!, Session ID: " + session.getId() + ", " + err);

        Action action = Action.builder()
                .type(ActionType.ERROR)
                .build();

        if (err instanceof BadRequestException) {
            action.setError(err.getClass().getSimpleName());
        } else {
            action.setError(err.getMessage());
        }

        session.getAsyncRemote().sendObject(action);

        err.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOGGER.info("[SERVER]: Session " + session.getId() + " closed, because " + closeReason);
        sessions.values().forEach(s -> s.remove(session));
    }

    @PostConstruct
    public void start() {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            Server server = new Server(hostname, Integer.parseInt(port), endpoint, null, WSServerEndpoint.class);
            try {
                server.start();
                System.out.println("WebSocket server started");
                latch.await();
            } catch (DeploymentException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                server.stop();
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
    }

    private boolean tokenRequired(ActionType actionType) {
        return !actionType.equals(ActionType.SIGN_IN) && !actionType.equals(ActionType.SIGN_UP);
    }
}
