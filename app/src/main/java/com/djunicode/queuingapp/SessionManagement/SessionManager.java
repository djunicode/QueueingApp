package com.djunicode.queuingapp.SessionManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by DELL_PC on 05-01-2018.
 */

public class SessionManager {
  // Shared Preferences
  SharedPreferences sharedPreferences;

  // Editor for Shared preferences
  Editor editor;

  // Context
  Context _context;

  // Shared preferences mode
  int PRIVATE_MODE = 0;

  String prefName;

  public SessionManager(Context context, String PREF_NAME){
    this._context = context;
    this.prefName = PREF_NAME;
    sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = sharedPreferences.edit();
  }

  public void createLoginSession(String sapid, String password, String name){


    if (prefName.equals("Teacher")){

      // Storing sapID in sharedPreferences
      editor.putString("teacher_sapid", sapid);

      // Storing password in sharedPreferences
      editor.putString("teacher_password", password);

      // Storing Name in sharedPreferences
      editor.putString("teacher_name", name);
    }

    else {

      // Storing sapid in sharedPreferences
      editor.putString("student_sapid", sapid);

      // Storing password in sharedPreferences
      editor.putString("student_password", password);

      // Storing Name in sharedPreferences
      editor.putString("student_name", name);
    }

    // commit changes
    editor.commit();
  }
}
