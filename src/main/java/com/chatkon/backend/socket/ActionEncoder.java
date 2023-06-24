package com.chatkon.backend.socket;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.util.Mapper;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class ActionEncoder implements Encoder.Text<Action> {

    @Override
    public String encode(Action action) {
        try {
            String jsonMessage = Mapper.toJson(action);
//            EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
//            return encryptDecrypt.encrypt(jsonMessage);
            return jsonMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

}