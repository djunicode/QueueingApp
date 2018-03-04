package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aniru on 04-02-2018.
 */

public class TeacherCreateNew {

    @SerializedName("subject")
    public String subject;
    @SerializedName("size")
    public int size;
    @SerializedName("startTime")
    public String from;
    @SerializedName("endTime")
    public String to;
    @SerializedName("maxLength")
    public Integer noOfStudents;
    @SerializedName("queueItems")
    public String queueItems;
    @SerializedName("id")
    public int id;
    @SerializedName("teacherName")
    public String teacherName;


    public TeacherCreateNew(int id, String subject, int size, String from, String to,
                            Integer noOfStudents, String queueItems, String teacherName) {
        this.subject = subject;
        this.size = size;
        this.from = from;
        this.to = to;
        this.noOfStudents = noOfStudents;
        this.queueItems = queueItems;
        this.id = id;
        this.teacherName = teacherName;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public int getSize() {
        return size;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getNoOfStudents() {
        return noOfStudents;
    }

    public String getQueueItems() {
        return queueItems;
    }

    public void setNoOfStudents(Integer noOfStudents) {
        this.noOfStudents = noOfStudents;
    }

    public String getTeacherName() {
        return teacherName;
    }
}