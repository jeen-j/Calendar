package com.jje.calendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.calendar.DBContents;
import com.db.calendar.DBHelper;

import java.util.Calendar;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    TextView monthTextView;
    Button previousMonth;
    Button nextMonth;
    Button group1;
    Button group2;
    Button group3;


    Calendar currentCalendarInfo;

    public int currentMonth;
    public int currentYear;
    public int currentDate;

    public int mCurrentGroup = 1;

    public static int maxDateViewNum = 42;

    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_month_view);

        mDBHelper = new DBHelper(getApplicationContext());


        monthTextView = (TextView) findViewById(R.id.currentMonth);

        previousMonth = (Button) findViewById(R.id.monthPrevious);
        nextMonth = (Button) findViewById(R.id.monthNext);

        group1 = (Button) findViewById(R.id.btn_group1);
        group2 = (Button) findViewById(R.id.btn_group2);
        group3 = (Button) findViewById(R.id.btn_group3);


        previousMonth.setOnClickListener(buttonClickListener);
        nextMonth.setOnClickListener(buttonClickListener);
        group1.setOnClickListener(buttonClickListener);
        group2.setOnClickListener(buttonClickListener);
        group3.setOnClickListener(buttonClickListener);


        int resID;

        Cursor cursor;

        for (int dateview = 1 ; dateview <= maxDateViewNum ; dateview++){


            resID = getResources().getIdentifier("date_view" + dateview,"id", "com.jje.calendar");

            TextView currentTextView = (TextView) findViewById( resID );

            currentTextView.setOnClickListener(this);

        }

        currentCalendarInfo = Calendar.getInstance();


        currentYear = currentCalendarInfo.get(Calendar.YEAR);

        currentMonth = currentCalendarInfo.get(Calendar.MONTH);

        currentDate = currentCalendarInfo.get(Calendar.DATE); // today


        int a = Calendar.DAY_OF_MONTH;


        //mDBHelper = new DBHelper(this);


        updateMonthlyView( );

    }

    @Override
    protected void onResume() {
        updateMonthlyView( );

        super.onResume();
    }

    public void updateMonthlyView( ) {


        SQLiteDatabase db = mDBHelper.getWritableDatabase();


        monthTextView.setText( Integer.toString(currentMonth + 1) );


        currentCalendarInfo.set(currentYear, currentMonth,1);


        int dayOfMonth = currentCalendarInfo.getActualMaximum(Calendar.DAY_OF_MONTH);

        int date_view_number = currentCalendarInfo.get(Calendar.DAY_OF_WEEK);

        int resID ;


        if( date_view_number >= Calendar.MONDAY) {

            Calendar preMonthInfo = Calendar.getInstance();
            preMonthInfo.set(currentYear, currentMonth - 1,1);

            int dayOfPreviousMonth = preMonthInfo.getActualMaximum(Calendar.DAY_OF_MONTH);



            for (int predate = date_view_number - 1 ; predate >= Calendar.SUNDAY ; predate--) {

                resID = getResources().getIdentifier("date_view" + predate, "id", "com.jje.calendar");

                TextView currentTextView = (TextView) findViewById( resID );

                currentTextView.setTextColor(Color.LTGRAY);
                currentTextView.setText(Integer.toString(dayOfPreviousMonth));

                dayOfPreviousMonth--;

            }
        }


        Cursor cursor;

        for(int date = 1 ; date <= dayOfMonth ; date++){

            resID = getResources().getIdentifier("date_view" + date_view_number,"id", "com.jje.calendar");

            TextView currentTextView = (TextView) findViewById( resID );


            String sqlquery = "select * from " + DBContents.TABLE_NAME +  " where " + DBContents.COLUMN_YEAR + "='" + currentYear+
                    "' AND " + DBContents.COLUMN_MONTH + "='" + currentMonth + "' AND " + DBContents.COLUMN_DATE + "='"+ date +
                    "' AND " + DBContents.COLUMN_GROUP + "='"+ mCurrentGroup + "'";

            cursor = db.rawQuery(sqlquery, null);

            currentTextView.setTextColor(Color.GRAY);
            String text = Integer.toString(date);

            while ( cursor.moveToNext() ){
                String work = cursor.getString(DBContents.INDEX_FOR_COLUMN_WORK);
                text += ("\n" + work );
            }

            currentTextView.setText(text);


            date_view_number++;

        }



        if( (date_view_number % 7) != 1){

            int lastDay ;

            if( (date_view_number % 7) == 0)
            {
                lastDay = 1;
            }
            else{
                lastDay = Calendar.SATURDAY - (date_view_number%7 - 1);
            }

            for (int nextdate = 1 ; nextdate <= lastDay ; nextdate++){

                resID = getResources().getIdentifier("date_view" + date_view_number,"id", "com.jje.calendar");

                TextView currentTextView = (TextView) findViewById( resID );

                currentTextView.setTextColor(Color.LTGRAY);
                currentTextView.setText(Integer.toString(nextdate));

                date_view_number++;

            }

            LayoutInflater inflater;
            inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout extend_week_view = (LinearLayout )findViewById(R.id.extend_week_view);


            if(date_view_number <= 36) {


                extend_week_view.setVisibility(View.GONE); //View.GONE
                LinearLayout date_view_layout = (LinearLayout )findViewById(R.id.date_view_layout);
                date_view_layout.setWeightSum(5);
            }
            else {
                LinearLayout date_view_layout = (LinearLayout )findViewById(R.id.date_view_layout);
                date_view_layout.setWeightSum(6);
                extend_week_view.setVisibility(View.VISIBLE);
            }
        }



    }

    View.OnClickListener buttonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.monthPrevious :
                    currentMonth = currentMonth - 1;

                    if(currentMonth < 0){
                        currentMonth = 11;
                        currentYear--;
                    }

                    currentCalendarInfo.set(currentYear, currentMonth, 1);

                    updateMonthlyView();


                    break;

                case R.id.monthNext :
                    currentMonth = currentMonth + 1;
                    if(currentMonth > 11){
                        currentMonth = 0;
                        currentYear++;
                    }

                    monthTextView.setText( Integer.toString(currentMonth + 1) );
                    currentCalendarInfo.set(currentYear, currentMonth, 1);

                    updateMonthlyView();


                    break;

                case R.id.button_done_for_add :

                    EditText editTextForAdd = (EditText)findViewById(R.id.edit_text_for_add);

                    String work_for_add = editTextForAdd.getText().toString();

                    break;
                    //db 저장

                case R.id.btn_group1 :

                    mCurrentGroup = 1 ;
                    updateMonthlyView();

                    break;

                case R.id.btn_group2 :

                    mCurrentGroup = 2 ;
                    updateMonthlyView();

                    break;

                case R.id.btn_group3 :

                    mCurrentGroup = 3;
                    updateMonthlyView();

                    break;




            }
        }
    };

    @Override
    public void onClick(View view) {

        String date = (String) ((TextView)view).getText();

        String[] array = date.split("\n");


        int selectedDate = Integer.parseInt( array[0] );

        Intent intent = new Intent(getApplicationContext(), DetailDateViewActivity.class);


        intent.putExtra( "year" , currentYear);
        intent.putExtra("month" , currentMonth);
        intent.putExtra("date" , selectedDate);
        intent.putExtra("group" , mCurrentGroup);

        startActivity(intent);


    }

    public void ViewUpdate(int month){

    }
}
