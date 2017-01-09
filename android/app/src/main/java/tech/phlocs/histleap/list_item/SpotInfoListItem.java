package tech.phlocs.histleap.list_item;

public class SpotInfoListItem {
    private long id = 0;
    private String title = null;
    private String content = null;

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
