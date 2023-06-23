package com.chatkon.backend.model.entity.chat;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberId implements Serializable {
    private Long userId;
    private Long chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId memberId)) return false;
        return Objects.equals(userId, memberId.userId) && Objects.equals(chatId, memberId.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId);
    }
}
