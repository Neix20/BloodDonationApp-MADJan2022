package my.edu.utar.blooddonationmadnew.data;

import java.util.Objects;

public class Notification {
    private String id;
    private String title;
    private String body;

    public Notification(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Notification(){
        this("", "", "");
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body);
    }

    @Override
    public String toString() {
        return String.format("Id: %s\nTitle: %s\nBody: %s\n", id, title, body);
    }
}
