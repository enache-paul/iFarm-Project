package be.howest.ti.mars.logic.domain;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OwnedPlant extends Plant {
    private final int id;
    private Garden garden;
    private Constants.Status status;
    private List<Scan> scans = new ArrayList<>();

    public OwnedPlant(int id, int plantId, String name, String imgLink, Garden garden, Constants.Status status) {
        super(plantId, name, imgLink);
        this.id = id;
        this.garden = garden;
        this.status = status;
    }
    public OwnedPlant(int id, Plant plant, Constants.Status status) {
        super(plant.getId(), plant.getName(), plant.getImgLink());
        this.status = status;
        this.id = id;
    }
    public OwnedPlant(int id, Plant plant, Constants.Status status, List<Scan> scans) {
        this(id, plant, status);
        this.scans = scans;
    }
    public OwnedPlant(int id, Plant plant, Garden garden, Constants.Status status) {
        this(id, plant.getId(), plant.getName(), plant.getImgLink(), garden, status);
    }
    public OwnedPlant(int id, int plantId, String name, String imgLink, Garden garden, Constants.Status status, List<Scan> scans) {
        this(id, plantId, name, imgLink, garden, status);
        this.scans = scans;
    }

    @Override
    public int getId() {
        return id;
    }
    public Garden getGarden() {
        return garden;
    }
    public Constants.Status getStatus() {
        return status;
    }
    public List<Scan> getScans() {
        return scans;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnedPlant)) return false;
        if (!super.equals(o)) return false;
        OwnedPlant that = (OwnedPlant) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
