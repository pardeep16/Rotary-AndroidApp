package dev.pardeep.healthappointment;

/**
 * Created by pardeep on 08-06-2017.
 */
public class HomeListContent {
    private String listTitle;
    private int icon;
    private String listContent;

    public HomeListContent(String listTitle, int icon, String listContent) {
        this.listTitle = listTitle;
        this.icon = icon;
        this.listContent = listContent;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }
}
