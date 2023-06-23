package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Robot {
    private final String name;
    private Garden garden;
    private String description;
    private final int batteryLevel;

    private int gardenId;

    public Robot(String name, String description, int batteryLevel) {
        this.name = name;
        this.description = description;
        this.batteryLevel = batteryLevel;
    }
    public Robot(String name, Garden garden, String description, int batteryLevel) {
        this(name, description, batteryLevel);
        this.garden = garden;
        this.gardenId = garden.getId();
    }

    public Robot(String name, int gardenId, String description, int batteryLevel) {
        this.name = name;
        this.gardenId = gardenId;
        this.description = description;
        this.batteryLevel = batteryLevel;
    }

    public String getName() {
        return name;
    }
    public Garden getGarden() {
        return garden;
    }
    public String getDescription() {
        return description;
    }
    public int getBatteryLevel() {
        return batteryLevel;
    }
    public int getGardenId() { return gardenId;}
    public void setDescription(String description) { this.description = description;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Robot)) return false;
        Robot robot = (Robot) o;
        return name.equals(robot.name) && garden.equals(robot.garden);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, garden);
    }
}
