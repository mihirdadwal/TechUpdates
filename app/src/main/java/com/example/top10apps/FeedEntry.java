package com.example.top10apps;

public class FeedEntry {
    private String name;
    private String artist;
    private String releasedate;
    private String summary;
    private String imageURl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    @Override
    public String toString() {
        return "name=" + name + '\n' +
                ", artist=" + artist + '\n' +
                ", releasedate=" + releasedate + '\n' +
                ", imageURl=" + imageURl + '\n' ;
    }
}
