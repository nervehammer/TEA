package com.procleaus.tea;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by suraj on 07-03-2017.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EncryptActivity extends FilePickerHelper{

    Button btnDatePicker, btnTimePicker, btneit, btnatt;
    TextView txtDate, txtTime;
    EditText etpass;
    private int mYear, mMonth, mDay, pHour, pMinute,pday,pyear,pmonth;
    private boolean dateflag;
    String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btneit = (Button) findViewById(R.id.btn_eit);
        btnatt = (Button) findViewById(R.id.btnatt);
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);
        etpass=(EditText)findViewById(R.id.editTextPassword);
        btnatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                  pickafile();

            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EncryptActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        txtTime.setText(null);
                        pHour=-99;
                        pMinute=-99;
                        pyear=year;
                        pday=dayOfMonth;
                        pmonth=monthOfYear;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                Calendar c = Calendar.getInstance();

                TimePickerDialog mTimePicker = new TimePickerDialog(EncryptActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Calendar temp = Calendar.getInstance();
                                temp.set(Calendar.HOUR_OF_DAY, selectedHour);
                                temp.set(Calendar.MINUTE, selectedMinute);
                                //Current Date flag check
                                if(pyear==mYear && pmonth==mMonth && pday==mDay){
                                    dateflag = true;
                                }
                                else dateflag=false;

                                if (temp.before(GregorianCalendar.getInstance()) && dateflag) {
                                    Toast.makeText(EncryptActivity.this, "Cannot select a past time", Toast.LENGTH_SHORT).show();
                                } else {
                                    Calendar datetime = Calendar.getInstance();
                                    datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                                    datetime.set(Calendar.MINUTE, selectedMinute);
                                    pHour=selectedHour;
                                    pMinute=selectedMinute;
                                    txtTime.setText(selectedHour + ":" +selectedMinute);

                                }
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

                mTimePicker.updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                mTimePicker.show();
            }
        });

        btneit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nmonth=pmonth+1;
                String str_date=pyear+"-"+nmonth+"-"+pday+" "+pHour+":"+pMinute+":00";
                Log.i("ggdnt",str_date);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(str_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Log.i("timestamp-uncut",String.valueOf(date.getTime()));
                src=setSrc();
                AesED.setReq(1);
                AesED.setCryptPassword(etpass.getText().toString());
                String cutts = String.valueOf(date.getTime()).substring(0,10);
                Log.i("timestamp-cut", cutts);
                AesED.setUnlockTime(cutts);
                try {
                    Intent i= new Intent(EncryptActivity.this,AesED.class);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
