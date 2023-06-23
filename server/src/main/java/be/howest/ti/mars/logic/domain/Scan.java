package be.howest.ti.mars.logic.domain;

import java.util.Date;
import java.util.Objects;

public class Scan {
    private OwnedPlant plant;
    private final Date scanTime;
    private final int fertilizerLevel;
    private final int waterLevel;
    private final int expectedYield;
    private final int health;
    private final int growth;

    public Scan(Date scanTime, int fertilizerLevel, int waterLevel, int expectedYield, int health, int growth) {
        this.scanTime = scanTime;
        this.fertilizerLevel = fertilizerLevel;
        this.waterLevel = waterLevel;
        this.expectedYield = expectedYield;
        this.health = health;
        this.growth = growth;
    }
    public Scan(OwnedPlant plant, Date scanTime, int fertilizerLevel, int waterLevel, int expectedYield, int health, int growth) {
        this(scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth);
        this.plant = plant;
    }

    public OwnedPlant getPlant() {
        return plant;
    }
    public Date getScanTime() {
        return scanTime;
    }
    public int getFertilizerLevel() {
        return fertilizerLevel;
    }
    public int getWaterLevel() {
        return waterLevel;
    }
    public int getExpectedYield() {
        return expectedYield;
    }
    public int getHealth() {
        return health;
    }
    public int getGrowth() {
        return growth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scan)) return false;
        Scan scan = (Scan) o;
        return fertilizerLevel == scan.fertilizerLevel && waterLevel == scan.waterLevel && expectedYield == scan.expectedYield && health == scan.health && growth == scan.growth && Objects.equals(scanTime, scan.scanTime);
    }
    @Override
    public int hashCode() {
        return Objects.hash(scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth);
    }
}
