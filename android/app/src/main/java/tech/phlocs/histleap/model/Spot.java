package tech.phlocs.histleap.model;

import java.util.ArrayList;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Spot {
    private String id;
    private String name;
    private float latitude;
    private float longitude;
    private String address;
    private String url;
    private String imageUrl;
    private String overview;
    private ArrayList<Event> eventList;

    public Spot(String id,
                String name,
                float latitude,
                float longitude,
                String address,
                String url,
                String imageUrl,
                String overview,
                ArrayList<Event> eventList
    ) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.url = url;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.eventList = eventList;
    }

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<Event> getEventList() { return eventList; }

    public void setEventList(ArrayList<Event> eventList) { this.eventList = eventList; }
}
