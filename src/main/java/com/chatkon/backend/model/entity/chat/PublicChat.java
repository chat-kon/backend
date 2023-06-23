package com.chatkon.backend.model.entity.chat;

import com.chatkon.backend.model.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@PrimaryKeyJoinColumn
public class PublicChat extends Chat {
    private String title;
    private String link;

    private String avatar;

    @ManyToOne
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PublicChat chat = (PublicChat) o;
        return Objects.equals(title, chat.title) && Objects.equals(link, chat.link) && Objects.equals(avatar, chat.avatar) && Objects.equals(owner, chat.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, link, avatar, owner);
    }
}
