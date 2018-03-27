package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;



public class TeacherListModel {
    /*@SerializedName("id")
    private Integer id;*/
    @SerializedName("teachers")
    private List<String> teachers;

    public TeacherListModel(List<String> teachers){
        this.teachers = teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getTeachers() {
        return teachers;
    }
}
