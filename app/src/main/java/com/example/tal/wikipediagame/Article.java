package com.example.tal.wikipediagame;


import java.util.concurrent.ThreadLocalRandom;

public class Article {

    private int pageid;
    private String name;
    private String url;


    public Article() {}

    public Article(int pageid, String name) {
        this.pageid = pageid;
        this.name = name;
    }

    public Article(int pageid, String name, String url) {
        this.pageid = pageid;
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
