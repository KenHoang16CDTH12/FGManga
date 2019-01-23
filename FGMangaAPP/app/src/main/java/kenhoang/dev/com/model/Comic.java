package kenhoang.dev.com.model;

public class Comic {
    private int ID;
    private String Name;
    private String Image;

    public Comic() {
    }

    public Comic(int ID, String Name, String Image) {
        this.ID = ID;
        this.Name = Name;
        this.Image = Image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
