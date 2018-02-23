package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Ruturaj on 23-02-2018.
 */

public class StudentSubscriptions {
  @SerializedName("teacherNames")
  private List<String> teacherNames;

  public void setTeacherNames(List<String> teacherNames) {
    this.teacherNames = teacherNames;
  }

  public List<String> getTeacherNames() {
    return teacherNames;
  }
}
