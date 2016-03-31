package com.example.person_finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class Add_Schedule extends Activity {

		DatePicker datepicker;
	    EditText cat;
	    EditText subcat;
	    EditText des ;
	    MyDBHandler dbh;
	    Button add;
	    Button view;
	    Spinner spn_cat;
	    Spinner spn_subcat;
	    String catt;
	    
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        // variable declaration
        datepicker = (DatePicker) findViewById(R.id.dddd);
        des = (EditText) findViewById(R.id.catname);
        add= (Button) findViewById(R.id.buttonAddCat);
        view= (Button) findViewById(R.id.button_Category);
        dbh = new MyDBHandler(this,null, null, 1);
        spn_cat = (Spinner) findViewById(R.id.spinner_addschedule_cat); 
        spn_subcat = (Spinner) findViewById(R.id.spinner_subcat); 
   
        //Load categories to the spinner
        LoadCat();

        //change subcategory according to the category
        spn_cat.setOnItemSelectedListener(
		   new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String idd = Long.toString(arg3);
				int id = Integer.parseInt(idd);
				catt = dbh.fromcat(id);
				LoadSubCat(catt);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		} 
		
		   );
   
        //add schedules when click add button
        add.setOnClickListener(
		   new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if(des.getText().toString().matches(""))
					des.setError("Type a description");
				
				else
				addClick();
				
			}
		   						}
		   );
  
        
        //View schedule Button
        final Intent i = new Intent(this, View_Schedule.class);
        view.setOnClickListener(
        		new OnClickListener() {
			
        			@Override
        			public void onClick(View v) 
        			{
        				startActivity(i); 	
        			}
        		}
        		);
    
    
    
    }

    //Add schedule method
    public void addClick(){
    	
    		int day = datepicker.getDayOfMonth();
    	    int month = datepicker.getMonth()+1;
    	    int year =  datepicker.getYear();

    	   String date = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
    	   Long id = spn_subcat.getSelectedItemId();
    	   String idd = Long.toString(id);
    	   String subcat = dbh.SucatforId(idd);
    	   String dess = des.getText().toString();
    	   
    	   if(dess.matches(""))
    		   des.setError("Type a description to add");
    	   else if(ValidateDate(date))
    		   Toast.makeText(Add_Schedule.this, "Choose a valid date", Toast.LENGTH_LONG).show();
    	   else{
    	   dbh.AddSchedule(date,catt,subcat,dess);
    	   
    	   spn_cat.setSelection(0);
    	   spn_subcat.setSelection(0);   	
    	   des.setText("");
    	   Toast.makeText(Add_Schedule.this, "Saved in Schedules", Toast.LENGTH_LONG).show();
    		 
    	   }
    }
    
    //Load category Method
   public void LoadCat(){
	   
	   	Spinner catlist = (Spinner) findViewById(R.id.spinner_addschedule_cat);
	   
	   	Cursor c= dbh.displayCat("Expenses");
	   	String[] from = new String[] {MyDBHandler.COLUMN_CAT_CATEGORY};
	   	int[] to = new int[] {R.id.textView_spinner_catname};
	   	SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, c, from, to,0);
	   	catlist.setAdapter(cusad);
		 	
   }
  
   //Load subcategory Method
   public void LoadSubCat(String catt){
	   	 Cursor cc = dbh.displaySubCat(catt);
		 String[] from = new String[] {MyDBHandler.COLUMN_CAT_SUBCATEGORY};
		 int[] to = new int[] {R.id.textView_spinner_catname};
		 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, cc, from, to,0);
		 Spinner catlist = (Spinner) findViewById(R.id.spinner_subcat);
		 catlist.setAdapter(cusad);
   }
   
   public boolean ValidateDate(String date){
	   Calendar c = Calendar.getInstance(); 
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		String curdate=String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date convertedDate = new Date();
	    Date convertedCurDate = new Date();
	        try {
				convertedDate = dateFormat.parse(date);
				convertedCurDate = dateFormat.parse(curdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        if(convertedDate.before(convertedCurDate))
	        	return true;
	        else
	        	return false;
	        
   }
     
}
