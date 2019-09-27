package com.db.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(Context context){

        super(context, "newcalDB" , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE " + DBContents.TABLE_NAME + " ( " );
        sb.append( "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " );
        sb.append( DBContents.COLUMN_YEAR + " INTEGER, " );
        sb.append( DBContents.COLUMN_MONTH + " INTEGER, " );
        sb.append( DBContents.COLUMN_DATE + " INTEGER, " );
        sb.append( DBContents.COLUMN_GROUP + " INTEGER, " );
        sb.append( DBContents.COLUMN_WORK + " TEXT); ");

        db.execSQL(sb.toString());



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE_TABLE");

        onCreate(db);

    }


}

