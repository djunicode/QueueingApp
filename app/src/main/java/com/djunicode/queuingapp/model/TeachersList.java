package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Ruturaj on 01-03-2018.
 */

public class TeachersList {
    /*@SerializedName("id")
    private Integer id;*/
    @SerializedName("teachers")
    private List<String> teachers;

    public TeachersList(List<String> teachers){
        this.teachers = teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getTeachers() {
        return teachers;
    }
}
