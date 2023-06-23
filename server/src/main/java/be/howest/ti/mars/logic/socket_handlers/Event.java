package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.domain.Constants;

public abstract class Event {
    private final Constants.EventType type;
    private final int gardenId;

    protected Event(int gardenId, Constants.EventType type) {
        this.gardenId = gardenId;
        this.type = type;
    }

    public Constants.EventType getType() {
        return type;
    }
    public int getGardenId() {
        return gardenId;
    }
}
