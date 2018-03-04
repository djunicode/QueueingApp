package com.djunicode.queuingapp.data;

import android.provider.BaseColumns;

public class QueuesContract {

    public static final class QueuesEntry implements BaseColumns {

        public static final String TABLE_NAME = "queues";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_BATCH = "batch";
        public static final String COLUMN_FROM = "fromTime";
        public static final String COLUMN_TO = "toTime";
        public static final String COLUMN_NO_OF_STUDENTS = "noOfStudents";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_SERVER_ID = "serverId";
    }

}
