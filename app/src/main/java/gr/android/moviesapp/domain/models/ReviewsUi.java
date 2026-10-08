package gr.android.moviesapp.domain.models;

public class ReviewsUi {

    private String author;
    private String content;
    private String createdAt;
    private String id;
    private String updatedAt;
    private String url;

    public ReviewsUi(String author, String content, String createdAt, String id, String updatedAt, String url) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.updatedAt = updatedAt;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ReviewsUi{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", id='" + id + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

