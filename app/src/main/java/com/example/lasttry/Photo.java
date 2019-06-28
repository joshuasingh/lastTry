package com.example.lasttry;

public class Photo {

    private String webUrl;
    private String url;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Photo() {

    }

    public Photo(String webUrl, String url) {
        this.webUrl = webUrl;
        this.url = url;
    }
}

