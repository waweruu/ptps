package com.isproject.ptps;

public class Review {

    private float stars;
    private String text;
    private String userUid;
    private String licencePlate;
    private String timeStamp;

    public Review() {
        //Firebase needs this
    }

    public Review(float stars, String text, String userUid, String licencePlate, String timeStamp) {
        this.stars = stars;
        this.text = text;
        this.userUid = userUid;
        this.licencePlate = licencePlate;
        this.timeStamp = timeStamp;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
