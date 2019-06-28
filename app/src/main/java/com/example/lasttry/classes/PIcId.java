package com.example.lasttry.classes;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import android.support.annotation.NonNull;

@Entity
public class PIcId {


    public PIcId(String pic_Id, String groupId, String url) {
        this.pic_Id = pic_Id;
        this.groupId = groupId;
        this.url = url;
    }

    @Override
    public String toString() {
        return "PIcId{" +
                "pic_Id='" + pic_Id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getPic_Id() {
        return pic_Id;
    }

    public void setPic_Id(String pic_Id) {
        this.pic_Id = pic_Id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @PrimaryKey
    public String pic_Id;


    @ColumnInfo(name = "group_Id")
    public String groupId;

    @ColumnInfo(name = "url")
    public String url;


}