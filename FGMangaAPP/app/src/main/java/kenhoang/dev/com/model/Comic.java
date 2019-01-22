package kenhoang.dev.com.model;

public class Comic {
    private int ID;
    private String name;
    private String image;

    public Comic() {
    }

    public Comic(int ID, String name, String image) {
        this.ID = ID;
        this.name = name;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
