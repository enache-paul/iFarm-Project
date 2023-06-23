package be.howest.ti.mars.logic.domain;

public class Constants {
    public enum Tier {
        FREE,
        PREMIUM,
        PREMIUMXL
    }

    public enum Status {
        FUTURE,
        CURRENT,
        FINISHED
    }

    public enum AdStatus {
        LIVE,
        SOLD
    }

    public enum EventType {
        UNICAST,
        POST_LIKE,
        COMMENT_LIKE
    }
}
