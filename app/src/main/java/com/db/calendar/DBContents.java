package com.db.calendar;

import android.provider.BaseColumns;

public final class DBContents implements BaseColumns {
    public static final String TABLE_NAME = "SCHEDULE_TABLE";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GROUP = "user_group";
    public static final String COLUMN_WORK = "work";

    public static final int INDEX_FOR_COLUMN_WORK = 5;
}
