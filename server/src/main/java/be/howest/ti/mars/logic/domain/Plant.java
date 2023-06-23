package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Plant {
    private int id;
    private final String name;
    private final String imgLink;
    private static final int ID_NOT_SET = -1;

    public Plant(String name, String imgLink) {
        this.name = name;
        this.imgLink = imgLink;
        this.id = ID_NOT_SET;
    }

    public Plant(int id, String name, String imgLink) {
        this.id = id;
        this.name = name;
        this.imgLink = imgLink;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImgLink() {
        return imgLink;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plant)) return false;
        Plant plant = (Plant) o;
        return id == plant.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
