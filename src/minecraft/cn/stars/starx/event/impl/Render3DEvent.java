package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Render3DEvent extends Event {
    private final float partialTicks;
}