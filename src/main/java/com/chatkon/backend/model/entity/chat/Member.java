package com.chatkon.backend.model.entity.chat;

import com.chatkon.backend.model.entity.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @EmbeddedId
    private MemberId id;

    @ManyToOne
    @MapsId(value = "userId")
    private User user;

    @ManyToOne
    @MapsId(value = "chatId")
    private PublicChat chat;

    private Boolean canSendMessage;
}
