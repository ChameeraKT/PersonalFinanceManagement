package com.example.person_finance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CalendarView.OnDateChangeListener;

public class View_Schedule extends Activity {

	
	MyDBHandler dbh;
	ListView lv;
	CalendarView cal;
	String curDate, idofschedule,date;
	Spinner edit_spn_cat;
	EditText edit_description;
	String category,subcategory,description;
	String catt;
	DatePicker date_picker;
	Spinner edit_spn_subcat;
	Button filter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view__schedule);
		
		// variable declaration
		dbh = new MyDBHandler(this,null,null,1);
		cal = (CalendarView) findViewById(R.id.calendarView1);
		lv = (ListView) findViewById(R.id.listView_Schedule);
		filter = (Button) findViewById(R.id.button_ViewSch_Filter);
		
		//display the schedules on the current date
		Calendar c = Calendar.getInstance(); 
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		String date=String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
        DisplaySchedule(date);
		
		//register the context menu for ListView
		registerForContextMenu(lv);
	
		//display the schedules on the selected date
		cal.setOnDateChangeListener(
				new OnDateChangeListener() {
					
					@Override
					public void onSelectedDayChange(CalendarView view, int year, int month,
						int dayOfMonth) {
						int d = dayOfMonth;
						int y = year;
						int m = month+1;
				
	                    curDate = String.valueOf(y)+"/"+String.valueOf(m)+"/"+String.valueOf(d);
	                    DisplaySchedule(curDate);
	             
					}
				}								
				);
	
		 //set all schedules to the listview 
		filter.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Filter();
					}
				}
				);
		//end of onCreate
	}
	
	
	//crate the context menu for edit,delete
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		 if(v.getId()==R.id.listView_Schedule)
		 {
			 menu.setHeaderTitle("Select an option");
			 String menuItem[] = {"Edit","Delete"};
			 for(int i=0;i<menuItem.length;i++){
				 menu.add(Menu.NONE,i,i,menuItem[i]);
			
			 }
		 }
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	//set the context menu selections
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = item.getItemId();
		String menuItem[] = {"Edit","Delete"};
		String menuitemName = menuItem[index];
		
		//if the user select delete option
		 if(menuitemName=="Delete"){
			 
			 //create an alert box ask for deletion
				AlertDialog.Builder builder = new AlertDialog.Builder(View_Schedule.this);
				builder.setTitle("Delete Alert");
				builder.setMessage("Do you really want to delete this?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					//if the user select yes
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String id = Long.toString(info.id);
						idofschedule=id;
						dbh.DeleteScheduleRaw(idofschedule);
						Toast.makeText(View_Schedule.this,"1 Item Deleted", Toast.LENGTH_LONG).show();
						DisplaySchedule(curDate);
						
					
					}
				}
				);
				//if the user select no do nothing
				builder.setNegativeButton("No", null);
				
				AlertDialog al = builder.create();
				al.show();
				return false;
		 }
		 
		//if the user select edit option
		 if(menuitemName=="Edit")
		 {
			
			 showDialog((int) info.id);   //call the edit menu
		 }
		 
		 
		
		return super.onContextItemSelected(item);
	}
	
	
	//create a dialog box for edit schedules
	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflator = this.getLayoutInflater();
		final View dialogView = inflator.inflate(R.layout.edit_schedules, null);
		dialogBuilder.setView(dialogView);
		
		//declare variables in the dialog box
		edit_spn_cat = (Spinner) dialogView.findViewById(R.id.spinner_EditSchule_cat);
		edit_description = (EditText) dialogView.findViewById(R.id.editText_editSchedule_Des);
		date_picker = (DatePicker) dialogView.findViewById(R.id.datePicker_editschedule);
		
		
		//populate the category spinner
		Cursor c= dbh.displayCat("Expenses");
		String[] from = new String[] {MyDBHandler.COLUMN_CAT_CATEGORY};
		int[] to = new int[] {R.id.textView_spinner_catname};
		SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, c, from, to,0);
		edit_spn_cat.setAdapter(cusad);
			 
		//change the subcategory according to category
		edit_spn_cat.setOnItemSelectedListener(
					   new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							
							String idd = Long.toString(arg3);
							int id = Integer.parseInt(idd);
							catt = dbh.fromcat(id);
							Cursor cc = dbh.displaySubCat(catt);
							String[] from = new String[] {MyDBHandler.COLUMN_CAT_SUBCATEGORY};
							int[] to = new int[] {R.id.textView_spinner_catname};
							SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, cc, from, to,0);
							edit_spn_subcat = (Spinner) dialogView.findViewById(R.id.spinner_EditSchule_subcat);
							edit_spn_subcat.setAdapter(cusad);
						   
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
							
						}
					} 
					   );   
			 //get the values from DB for editing
			 Cursor curs = dbh.displayScheduleforId(Integer.toString(id)); 	
			 
			 if(curs.moveToFirst()){
				 do{
					 date = curs.getString(curs.getColumnIndex("date"));
					 category = curs.getString(curs.getColumnIndex("category"));
					 description=curs.getString(curs.getColumnIndex("description"));
					 subcategory=curs.getString(curs.getColumnIndex("subcategory"));
				 }while(curs.moveToNext());
			 }
			 
			 String[] datee = date.split("/");
			 int y= Integer.parseInt(datee[0]);
			 int m= Integer.parseInt(datee[1])-1;
			 int d= Integer.parseInt(datee[2]);
			 date_picker.updateDate(y, m, d);			//Set the date to the picker
			 edit_description.setText(description);		//Set the description to the EditText
			 edit_spn_cat.setSelection(setCat());		 //Set the category to the spinner
			
		
			 //set the update button click event
		 
			 dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
				 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				//get values to update
				int day = date_picker.getDayOfMonth();
	    	    int month = date_picker.getMonth()+1;
	    	    int year =  date_picker.getYear();

	    	   String date = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
	    	   Long idd = edit_spn_subcat.getSelectedItemId();
	    	   String iddd = Long.toString(idd);
	    	   String subcat = dbh.SucatforId(iddd);
	    	   String dess = edit_description.getText().toString();
	    
	    	   if(dess.matches(""))       //check whether textfield is empty or not
					Toast.makeText(View_Schedule.this,"Cannot Update. Description cannot be empty", Toast.LENGTH_LONG).show();
				else{	
	    	   //call the update method
	    	   dbh.UpdateSchedule(Integer.toString(id), date, catt, subcat, dess);
	    	   
	    	   edit_spn_cat.setSelection(0);
	    	   edit_spn_subcat.setSelection(0);   	
	    	   edit_description.setText("");
	    	   DisplaySchedule(date);
	    	   Toast.makeText(View_Schedule.this, "Schedule updated", Toast.LENGTH_LONG).show();
				}
			}
		}       
		);
			 
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}
		);
		
		//build and show the menu
		AlertDialog a = dialogBuilder.create();
		a.show();
		
		return super.onCreateDialog(id);
	}
			

	//Display schedules according to the date
	public void DisplaySchedule(String date){
		 Cursor cc = dbh.displaySchedule(date);
		 String[] from = new String[] {MyDBHandler.COLUMN_CAT,MyDBHandler.COLUMN_SUBCAT,MyDBHandler.COLUMN_DESCRIPTION,MyDBHandler.COLUMN_DATE};
		 int[] to = new int[] {R.id.textView_cat_layout,R.id.textView_subcat_layout,R.id.textView_des_layout,R.id.textView_date_layout};
		 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.view_schedule_layout, cc, from, to,0);
		 ListView catlist = (ListView) findViewById(R.id.listView_Schedule);
		 catlist.setAdapter(cusad);
	}
	
	
	/*Set the allocated category to the spinner
	 * get the all categories and find the matching one with the 
	 * selected category
	 * when it found return the cursor position 
	 */
	public int setCat(){
		Cursor catcurs = dbh.displayCat("Expenses");
		
		int i =0;
		
		 if(catcurs.moveToFirst()){
			 do{
					if(catcurs.getString(catcurs.getColumnIndex("categoryname")).equals(category)){
						 i=catcurs.getPosition();
					}
			 }while(catcurs.moveToNext());
		 }

			return i;	
	}
	
	
	/*Set the allocated subcategory to the spinner
	 * get the all subcategories and find the matching one with the 
	 * selected subcategory
	 * when it found return the cursor position 
	 */
	public int setSubCat(String cat){
		Cursor catcurs = dbh.displaySubCat(cat);
		
		int i =0;
		
		 if(catcurs.moveToFirst()){
			 do{
					if(catcurs.getString(catcurs.getColumnIndex("subcategoryname")).equals(subcategory)){
						 i=catcurs.getPosition();
					}
			 }while(catcurs.moveToNext());
		 }

			return i;	
	}
  
    public void Setdate(final TextView Text){
    	final Calendar myCalendar = Calendar.getInstance();

		final DatePickerDialog.OnDateSetListener datet = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
		            int dayOfMonth) {
		        myCalendar.set(Calendar.YEAR, year);
		        myCalendar.set(Calendar.MONTH, monthOfYear);
		        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        String myFormat = "yyyy/M/d"; 
				SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
				Text.setText(sdf.format(myCalendar.getTime()));
		    }

		};

		Text.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
			            new DatePickerDialog(View_Schedule.this, datet, myCalendar
			                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
			                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

						
					}
				}
				);	
		
    }
    
    
    //set all schedules to the listview 
    public void Filter(){
    
			 Cursor cc = dbh.ViewScheduleDateRange();
			 String[] from = new String[] {MyDBHandler.COLUMN_CAT,MyDBHandler.COLUMN_SUBCAT,MyDBHandler.COLUMN_DESCRIPTION,MyDBHandler.COLUMN_DATE};
			 int[] to = new int[] {R.id.textView_cat_layout,R.id.textView_subcat_layout,R.id.textView_des_layout,R.id.textView_date_layout};
			 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.view_schedule_layout, cc, from, to,0);
			 ListView catlist = (ListView) findViewById(R.id.listView_Schedule);
			 catlist.setAdapter(cusad);
	
    	
    }
  
}

	