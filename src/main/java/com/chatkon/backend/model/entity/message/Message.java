package com.chatkon.backend.model.entity.message;

import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long date;
    private MessageType messageType;
    private Long views;

    @ManyToOne
    private User sender;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private Message forwardMessageRef;

    @ManyToOne
    private Message replyMessageRef;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(date, message.date) && messageType == message.messageType && Objects.equals(sender, message.sender) && Objects.equals(chat, message.chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, messageType, sender, chat);
    }
}
