package com.example.lasttry;

import java.util.HashMap;

public class Upload {
    private String groupName;
    private Photo p1;

    public Upload() {
    }

    public Upload(String groupName, Photo p1) {
        this.groupName = groupName;
        this.p1 = p1;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Photo getP1() {
        return p1;
    }

    public void setP1(Photo p1) {
        this.p1 = p1;
    }
}