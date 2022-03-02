package gabriel.medpel.gilraphal.model;

import java.util.Objects;

public class Item {
    private String glassType;
    private int height;
    private int width;
    private int line;
    private int place;

    public Item() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return height == item.height && width == item.width && line == item.line && place == item.place && Objects.equals(glassType, item.glassType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(glassType, height, width, line, place);
    }

    public Item(String glassType, int height, int width, int line, int place) {
        this.glassType = glassType;
        this.height = height;
        this.width = width;
        this.line = line;
        this.place = place;
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
