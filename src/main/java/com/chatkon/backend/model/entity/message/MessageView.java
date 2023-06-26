package com.chatkon.backend.model.entity.message;

import com.chatkon.backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long view;

    @ManyToOne
    private User user;

    @ManyToOne
    private Message message;
}
