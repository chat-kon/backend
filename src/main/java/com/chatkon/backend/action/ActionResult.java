package com.chatkon.backend.action;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class ActionResult {
    private Action action;
    private Set<Long> receivers;
}
