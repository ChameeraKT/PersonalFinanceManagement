package com.example.person_finance;

import android.R.array;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ClipData.Item;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Allocations extends Activity {
	
	Spinner SpnMonth;
	Spinner SpnCat;
	EditText Amt;
	TextView Tot;
	Button Add;
	MyDBHandler dbh;
	String month, allocatedAmt, allocatedCat, allocatedMonth;
	String cat;
	ListView lv;
	Spinner edit_spn_month;
	Spinner edit_spn_cat;
	EditText edit_amount;
	Intent iii;
	PendingIntent pendingintent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allocations);
	
	// variable declaration
	SpnMonth = (Spinner) findViewById(R.id.spn);
	SpnCat = (Spinner) findViewById(R.id.spinnerAlCat);
	dbh = new MyDBHandler(this,null,null,1);
	Amt = (EditText) findViewById(R.id.editTextAlAmount);
	Add = (Button) findViewById(R.id.buttonAlAdd);
	lv = (ListView) findViewById(R.id.listView_allocation);
	Tot = (TextView) findViewById(R.id.textView_Al_remainingincome);
	
	
	//load the spinners
	LoadCat();
	LoadMonth();
	
	//display previous allocations
	DisplayAllocation();
	
	//register context menu for long click item of listview
	registerForContextMenu(lv);
	
	//get the remainning income 
	int t = remaingincome();
	Tot.setText(Integer.toString(t));
	
	//get the selected month from the spinner
	SpnMonth.setOnItemSelectedListener(
			new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					month= (String) SpnMonth.getItemAtPosition(arg2);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
	
				}		
			}			
			);
	
	//get the selected month from the spinner
	SpnCat.setOnItemSelectedListener(
			new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String idd = Long.toString(arg3);
					int id = Integer.parseInt(idd);
					cat = dbh.fromcat(id);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
						
				}
			}
			);
	
	//set the add schedule button 
	Add.setOnClickListener(
			new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Amt.getText().toString().matches(""))  //check whether the amount is empty
				{
					Amt.setError("Type an amount");	
				}
				else if(availability(month, cat)) //check whether is there previous added data for that month and category
				{
					dbh.AddAllocations(month, cat, Amt.getText().toString());  //add the schedule
					Toast.makeText(Allocations.this,"Allocation added Sucessfully", Toast.LENGTH_LONG).show();
					SpnMonth.setSelection(0);
					SpnCat.setSelection(0);
					Amt.setText("");
						
					//display allocations
					DisplayAllocation();
					
					//get the remaining income
					int t = remaingincome();
					Tot.setText(Integer.toString(t));
				}
				else{
					 Toast.makeText(Allocations.this, "Amount already allocated", Toast.LENGTH_LONG).show();
				}			
			}
		}
		);
	
	}
	
	//create the context menu for edit and delete
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		 if(v.getId()==R.id.listView_allocation)
		 {
			 AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo; 
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
				AlertDialog.Builder builder = new AlertDialog.Builder(Allocations.this);
				builder.setTitle("Delete Alert");
				builder.setMessage("Do you really want to delete this?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					//if the user select yes
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String id = Long.toString(info.id);
						dbh.DeleteAllocationRaw(id);   //delete the allocation
						Toast.makeText(Allocations.this,"Allocation deleted", Toast.LENGTH_LONG).show();
						DisplayAllocation();		
						int t = remaingincome();
						Tot.setText(Integer.toString(t));
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
			 //call the edit dialog box
			 showDialog((int) info.id);
		 }
		 
		return super.onContextItemSelected(item);
	}
	
	//get data of category from DB and set them to a spinner
	public void LoadCat(){
		 Cursor c= dbh.displayCat("Expenses");
		   String[] from = new String[] {MyDBHandler.COLUMN_CAT_CATEGORY};
			 int[] to = new int[] {R.id.textView_spinner_catname};
			 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, c, from, to,0);
			Spinner catlist = (Spinner) findViewById(R.id.spinnerAlCat);
			 catlist.setAdapter(cusad);			 
	}
	
	public void LoadMonth(){
		String colors[] = {"Jan","Feb","Mar","Apr","Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		SpnMonth.setAdapter(spinnerArrayAdapter);		
	}
	
	//get data of allocations from DB and set them to the listview
	public void DisplayAllocation(){
		 Cursor cc = dbh.displayAllocation();
		 String[] from = new String[] {MyDBHandler.COLUMN_AL_MONTH,MyDBHandler.COLUMN_AL_CATEGORY,MyDBHandler.COLUMN_AL_AMOUNT};
		 int[] to = new int[] {R.id.textView_allayout_month,R.id.textView_allayout_cat,R.id.textView_allayout_amount};
		 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.allocation_listview_layout, cc, from, to,0);
		 ListView catlist = (ListView) findViewById(R.id.listView_allocation);
		 catlist.setAdapter(cusad);
	}
	
	/*check whether is there an amount allocated
	 *  for the input month and category
	 */
	public boolean availability(String month,String cat)
	{
		String count = dbh.getcount(month,cat);
		int c = Integer.parseInt(count);	
		if(c==0)
			return true;
		else
			return false; 	
	}
	
	/*get sum of income from DB and subtract it from the 
	 * sum of allocations
	 *  return the result as remaining income
	 */
	public int remaingincome(){
		int totalIncome =dbh.getIncomeTotal();
		int totalAllocations = dbh.getAllocationTotal();
		Tot.setTextColor(Color.rgb(0,0,0));
		
		if((totalIncome-totalAllocations)<0)
		{
			notification();  //create a notification allocations are exceeding the income
		}	
		
		return totalIncome-totalAllocations;
	}
	
	//create a dialog box for edit the allocations
	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflator = this.getLayoutInflater();
		final View dialogView = inflator.inflate(R.layout.edit_allocation_layout, null);
		dialogBuilder.setView(dialogView);
		
		//declare variables in the dialog box
		edit_spn_month = (Spinner) dialogView.findViewById(R.id.spinner_edit_all_month);
		edit_spn_cat = (Spinner) dialogView.findViewById(R.id.spinner_edit_all_cat);
		edit_amount = (EditText) dialogView.findViewById(R.id.editText_Edit_Al_amount);
		
		//load the categories to the spinner in the dialog box
		Cursor c= dbh.displayCat("Expenses");
		String[] from = new String[] {MyDBHandler.COLUMN_CAT_CATEGORY};
		int[] to = new int[] {R.id.textView_spinner_catname};
		SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.layout_spinner, c, from, to,0);
		edit_spn_cat.setAdapter(cusad);
			 
		//load the months to the spinner in the dialog box
		String colors[] = {"Jan","Feb","Mar","Apr","Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		edit_spn_month.setAdapter(spinnerArrayAdapter);
			 
		//get the details about the selected allocations from DB		
		String idd = Integer.toString(id);
		Cursor cur = dbh.AlocationsforId(idd);
				
		if(cur.moveToFirst()){
				do{
					allocatedCat = cur.getString(cur.getColumnIndex("al_cat"));
					allocatedAmt = cur.getString(cur.getColumnIndex("al_amount"));
					allocatedMonth = cur.getString(cur.getColumnIndex("al_month"));
				}while(cur.moveToNext());
			}
		
			edit_amount.setText(allocatedAmt);            //Set the allocated amount to the textview
			edit_spn_cat.setSelection(setCat());		  //Set the allocated category to the spinner
			edit_spn_month.setSelection(setMonth());	  //Set the allocated month to the spinner
			
			
			
		dialogBuilder.setTitle("Edit Allocation");		//set the title of the dialog
		
		//set the update button click event of the dialog box
		dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String idd = Integer.toString(id);  //get the primary key of allocation data
				String colors[] = {"Jan","Feb","Mar","Apr","Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
				int month = edit_spn_month.getSelectedItemPosition();
				String monthh = colors[month];			//get the selected month
				Long cat = edit_spn_cat.getSelectedItemId();	//get the selected category
				String catt = dbh.CatforId(Long.toString(cat));
				String amt = edit_amount.getText().toString();
				
				if(amt.matches(""))     //check whether the field are empty or not
				{	Toast.makeText(Allocations.this,"Cannot update.Type an amount", Toast.LENGTH_LONG).show();
				showDialog(id);
				}
				else{
				
				dbh.UpdateAllocations(idd, monthh, catt, amt);		//update the allocation
				DisplayAllocation();
				int t = remaingincome();
				Tot.setText(Integer.toString(t));
				Toast.makeText(Allocations.this,"Successfully Updated", Toast.LENGTH_LONG).show();
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
		
		//create and show the dialog box
		AlertDialog a = dialogBuilder.create();
		a.show();
		
		return super.onCreateDialog(id);
	}
	
	
	/*Set the allocated category to the spinner
	 * get the all categories and find the matching one with the 
	 * selected category
	 * when it found return the cursor position 
	 */
	public int setCat(){
		Cursor catcurs = dbh.displayCat("Expenses");	//get the categories
		int i =0;
		
		 if(catcurs.moveToFirst()){
			 do{
					if(catcurs.getString(catcurs.getColumnIndex("categoryname")).equals(allocatedCat)){
						 i=catcurs.getPosition();
					}
			 }while(catcurs.moveToNext());
		 }

			return i;	
	}
	
	
	//Set the allocated category to the spinner
	public int setMonth(){
		
		String colors[] = {"Jan","Feb","Mar","Apr","Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
		int index =0;
			for(int i=0;i<12;i++){
		
					if(colors[i].equals(allocatedMonth)){
						index=i;
						break;
					}
	
		 }

			return index;	
	}
    
	
	//make a notification for indicate the exceeding of the income amount
	public void notification(){
		NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
		notification.setAutoCancel(true);
		iii = new Intent(this,Allocations.class);
		pendingintent = PendingIntent.getActivity(this, 0, iii, PendingIntent.FLAG_UPDATE_CURRENT);
	
			notification.setSmallIcon(R.drawable.ic_launcher);
			notification.setTicker("You are exceeding Income Amount");
			notification.setWhen(System.currentTimeMillis());
			notification.setContentTitle("You are exceeding Income Amount");
			notification.setContentText("Click for more details");				
			notification.setContentIntent(pendingintent);
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(1,notification.build());
				
		Tot.setTextColor(Color.rgb(200,0,0)); //set the income amount color to red
	
	}
}
