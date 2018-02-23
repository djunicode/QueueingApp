package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruv on 23/2/18.
 */

public class TeacherModel {

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("isFree")
    public Boolean isFree;

    @SerializedName("sapId")
    public String sapId;

    @SerializedName("subject")
    public String subject;

    @SerializedName("user")
    public Integer user;

    @SerializedName("created_at")
    public String created;

    @SerializedName("updated_at")
    public String updated;

    @SerializedName("location")
    public Integer location;

    TeacherModel(Integer id, String name, Boolean isFree, String sapId, String subject,
                 Integer user, String created, String updated, Integer location){

        this.id=id;
        this.name=name;
        this.isFree=isFree;
        this.created=created;
        this.sapId=sapId;
        this.subject=subject;
        this.user=user;
        this.updated=updated;
        this.location=location;

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
