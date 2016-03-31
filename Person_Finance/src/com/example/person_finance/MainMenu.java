package com.example.person_finance;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainMenu extends Activity {

	Button sch;
	Button cat;
	Button not;
	Button all;
	Intent iii;
	PendingIntent pendingintent;
	
	 static final int DATE_DIALOG_ID = 1;
	    private int mYear = 2013;
	    private int mMonth = 5;
	    private int mDay = 30;
	    private EditText etPickADate;
	
	NotificationCompat.Builder notification;
	private static final int uniqueID= 12345;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
	
	
		
		//NotificationManager  nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	//	Notification notifi = new Notification(R.drawable.ic_launcher,"Notify Alarm Start",System.currentTimeMillis());
//		Intent myintent = new Intent(this,Add_Schedule.class);
		//PendingIntent contentIntent = PendingIntent.getActivity(this,0,myintent,0);
	//	notifi.setLatestEventInfo(this, "Notify Label", "Notify Text", contentIntent);
//		nm.notify(1,notifi);
		
		sch= (Button) findViewById(R.id.buttonMenuSchedule);
		cat= (Button) findViewById(R.id.buttonMenuCategory);
	//	not= (Button) findViewById(R.id.buttonNotification);
		all = (Button) findViewById(R.id.buttonMenuAllocation);
		
		final Intent i = new Intent(this, Add_Schedule.class);
		final Intent ii = new Intent(this, Category_main.class);
		final Intent iiii = new Intent(this, Allocations.class);
		
		all.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(iiii);
						
					}
				}
				);
		
		sch.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(i);
						
					}
				}
				);
		
		cat.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(ii);
						
					}
				}
				
				);
/*		
		notification = new NotificationCompat.Builder(this);
		notification.setAutoCancel(true);
		 iii = new Intent(this,View_Schedule.class);
		  pendingintent = PendingIntent.getActivity(this, 0, iii, PendingIntent.FLAG_UPDATE_CURRENT);
	not.setOnClickListener(
			new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				notification.setSmallIcon(R.drawable.ic_launcher);
				notification.setTicker("You have a schedule message");
				notification.setWhen(System.currentTimeMillis());
				notification.setContentTitle("You have a schedule message");
				notification.setContentText("Click for more details");
				
				notification.setContentIntent(pendingintent);
				
				
				NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				nm.notify(uniqueID,notification.build());
				
				
				
				
				
				}
			}
			);	
		
		*/
		
	}
	OnDateSetListener mDateSetListner = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDate();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            /*
             * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth,
             * mDay);
             */
            DatePickerDialog datePickerDialog = this.customDatePicker();
            return datePickerDialog;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    protected void updateDate() {
        int localMonth = (mMonth + 1);
        String monthString = localMonth < 10 ? "0" + localMonth : Integer
                .toString(localMonth);
        String localYear = Integer.toString(mYear).substring(2);
        etPickADate.setText(new StringBuilder()
        // Month is 0 based so add 1
                .append(monthString).append("/").append(localYear).append(" "));
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner,
                mYear, mMonth, mDay);
        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                        .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }
}
