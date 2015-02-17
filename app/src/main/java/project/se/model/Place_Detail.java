package project.se.model;

/**
 * Created by wiwat on 2/15/2015.
 */
public class Place_Detail {

    String place_name;
    String address;
    String phone;
    Double longitude;
    Double latitude;

    public Place_Detail(String place_name, String address, String phone, Double longitude, Double latitude) {
        this.place_name = place_name;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

