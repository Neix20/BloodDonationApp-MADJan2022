package my.edu.utar.blooddonationmadnew.admin.data;

public class BloodEvent {
    private String id;
    private String title;
    private String address;

    public BloodEvent(String id, String title, String address) {
        this.id = id;
        this.title = title;
        this.address = address;
    }

    public BloodEvent(){
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Title: %s, Address: %s", id, title, address);
    }
}
