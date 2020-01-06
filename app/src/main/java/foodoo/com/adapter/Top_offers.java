package foodoo.com.adapter;

public class Top_offers {
    private String id,title,path;

    public Top_offers(String id, String title, String path) {
        this.id = id;
        this.title = title;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
