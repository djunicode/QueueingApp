package com.djunicode.queuingapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.djunicode.queuingapp.data.QueuesContract.QueuesEntry;
import com.djunicode.queuingapp.model.RecentEvents;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruturaj on 02-03-2018.
 */

public class QueuesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "queues.db";
    private static final int DATABASE_VERSION = 1;

    public QueuesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_QUEUE_TABLE = "CREATE TABLE " + QueuesEntry.TABLE_NAME + " (" +
                QueuesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QueuesEntry.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                QueuesEntry.COLUMN_BATCH + " TEXT NOT NULL, " +
                QueuesEntry.COLUMN_FROM + " TEXT NOT NULL, " +
                QueuesEntry.COLUMN_TO + " TEXT NOT NULL, " +
                QueuesEntry.COLUMN_NO_OF_STUDENTS + " INTEGER NOT NULL, " +
                QueuesEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                QueuesEntry.COLUMN_SERVER_ID + " INTEGER NOT NULL " +
                "); ";
        db.execSQL(SQL_CREATE_QUEUE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QueuesEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addQueue(RecentEvents recentEvents) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QueuesEntry.COLUMN_SUBJECT, recentEvents.getSubjectName());
        cv.put(QueuesEntry.COLUMN_BATCH, recentEvents.getBatchName());
        cv.put(QueuesEntry.COLUMN_FROM, recentEvents.getStartTime());
        cv.put(QueuesEntry.COLUMN_TO, recentEvents.getEndTime());
        cv.put(QueuesEntry.COLUMN_NO_OF_STUDENTS, recentEvents.getNoOfStudents());
        cv.put(QueuesEntry.COLUMN_LOCATION, recentEvents.getLocation());
        cv.put(QueuesEntry.COLUMN_SERVER_ID, recentEvents.getServerId());

        db.insert(QueuesEntry.TABLE_NAME, null, cv);
        db.close();
    }

    public List<RecentEvents> getAllQueues() {
        List<RecentEvents> recentEvents = new ArrayList<RecentEvents>();
        String selectQuery = "SELECT  * FROM " + QueuesEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecentEvents event = new RecentEvents(null, null, null, null, null, null, null);
                event.setSubjectName(cursor.getString(1));
                event.setBatchName(cursor.getString(2));
                event.setStartTime(cursor.getString(3));
                event.setEndTime(cursor.getString(4));
                event.setNoOfStudents(cursor.getInt(5));
                event.setLocation(cursor.getString(6));
                event.setServerId(cursor.getInt(7));
                recentEvents.add(event);
            } while (cursor.moveToNext());
        }
        return recentEvents;
    }

    public int updateQueue(RecentEvents recentEvents) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QueuesEntry.COLUMN_SUBJECT, recentEvents.getSubjectName());
        cv.put(QueuesEntry.COLUMN_BATCH, recentEvents.getBatchName());
        cv.put(QueuesEntry.COLUMN_FROM, recentEvents.getStartTime());
        cv.put(QueuesEntry.COLUMN_TO, recentEvents.getEndTime());
        cv.put(QueuesEntry.COLUMN_NO_OF_STUDENTS, recentEvents.getNoOfStudents());
        cv.put(QueuesEntry.COLUMN_LOCATION, recentEvents.getLocation());

        return db.update(QueuesEntry.TABLE_NAME, cv, QueuesEntry.COLUMN_SERVER_ID + " = ?",
                new String[] { String.valueOf(recentEvents.getServerId()) });
    }

    public void deleteQueue(RecentEvents recentEvents) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QueuesEntry.TABLE_NAME, QueuesEntry.COLUMN_SERVER_ID + " = ?",
                new String[] { String.valueOf(recentEvents.getServerId()) });
        db.close();
    }
}
