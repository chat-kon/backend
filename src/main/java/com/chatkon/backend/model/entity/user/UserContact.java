package com.chatkon.backend.model.entity.user;

import com.chatkon.backend.model.entity.chat.Chat;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@PrimaryKeyJoinColumn
public class UserContact extends Chat {
    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserContact that = (UserContact) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user1, user2);
    }
}
