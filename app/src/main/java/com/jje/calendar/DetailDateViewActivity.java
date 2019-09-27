package com.jje.calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.db.calendar.DBContents;
import com.db.calendar.DBHelper;

public class DetailDateViewActivity extends Activity {

    EditText mEditTextForAdd;
    Button mButtonDoneForAdd;
    Button mButtonModify;
    Button mButtonDelete;
    Button mButtonClose;

    DBHelper mDBHelper;


    private int year;
    private int month;
    private int date;
    private int group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.detail_view);

        Intent intent = getIntent();

        year = intent.getExtras().getInt("year");
        month = intent.getExtras().getInt("month");
        date = intent.getExtras().getInt("date");
        group = intent.getExtras().getInt("group");

        mDBHelper = new DBHelper(getApplicationContext());

        mEditTextForAdd = (EditText) findViewById(R.id.edit_text_for_add);
        mButtonDoneForAdd = (Button) findViewById(R.id.button_done_for_add);
        mButtonModify = (Button) findViewById(R.id.button_modify);
        mButtonDelete = (Button) findViewById(R.id.button_delete);

        mButtonClose = (Button) findViewById(R.id.button_close);

        Button.OnClickListener mOnClickListener = new View.OnClickListener() {
            public void onClick(View view) {

                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues row = new ContentValues();


                switch (view.getId()){

                    case R.id.button_done_for_add :
                        String newWork = mEditTextForAdd.getText().toString();

                        row.put(DBContents.COLUMN_YEAR, year);
                        row.put(DBContents.COLUMN_MONTH, month);
                        row.put(DBContents.COLUMN_DATE, date);
                        row.put(DBContents.COLUMN_GROUP, group);
                        row.put(DBContents.COLUMN_WORK,newWork);

                        db.insert(DBContents.TABLE_NAME, null, row);

                        mDBHelper.close();

                        break;

                    case R.id.button_modify :
                        mEditTextForAdd.setEnabled( true );
                        break;

                    case R.id.button_delete :
                        mEditTextForAdd.setText("");
                        break;

                    case R.id.button_close :
                        finish();
                        break;

                }



            }
        };

        mButtonDoneForAdd.setOnClickListener(mOnClickListener);
        mButtonModify.setOnClickListener(mOnClickListener);
        mButtonDelete.setOnClickListener(mOnClickListener);
        mButtonClose.setOnClickListener(mOnClickListener);

    }
}
