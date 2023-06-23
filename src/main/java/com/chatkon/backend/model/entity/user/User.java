package com.chatkon.backend.model.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private Boolean allowedInvite;
    private Boolean visibleAvatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return allowedInvite == user.allowedInvite && visibleAvatar == user.visibleAvatar && Objects.equals(id, user.id) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, email, username, password, name, avatar, allowedInvite, visibleAvatar);
    }
}
