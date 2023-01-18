package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClickEvent extends Event {
    private ClickType type;

    public enum ClickType {
        LEFT,
        RIGHT,
        MIDDLE;
    }
}
