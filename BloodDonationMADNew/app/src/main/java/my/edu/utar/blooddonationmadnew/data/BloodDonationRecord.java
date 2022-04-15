package my.edu.utar.blooddonationmadnew.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BloodDonationRecord {

    private String id;
    private String venue_id;
    private String venue_title;
    private String user_id;
    private String user_name;

    private String user_id_venue;

    private String dateTime;


    public BloodDonationRecord(String id, String venue_id, String venue_title, String user_id, String user_name, String dateTime) {
        this.id = id;
        this.venue_id = venue_id;
        this.venue_title = venue_title;
        this.user_id = user_id;
        this.user_name = user_name;
        this.dateTime = dateTime;
    }

    public BloodDonationRecord() {
        this("", "", "", "", "", "");
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getVenue_title() {
        return venue_title;
    }

    public void setVenue_title(String venue_title) {
        this.venue_title = venue_title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser_id_venue() {
        return user_id_venue;
    }

    public void setUser_id_venue(String user_id_venue) {
        this.user_id_venue = user_id_venue;
    }
}
