package com.example.person_finance;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SubCategory extends Activity {

	TextView tv;
	EditText addsub;
	Button add;
	MyDBHandler db = new MyDBHandler(this, null, null, 1);
	EditText et ;
	String type;
	String regex = "^[A-Za-z]++$";	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_category);
		
		
		Intent i = getIntent();
		String catname = i.getExtras().getString("data", "");
		type = i.getExtras().getString("type");
		tv = (TextView) findViewById(R.id.textView_Sub_cat);
		tv.setText(catname);
		
	//	tv.setText(type);
	
		DisplaySubCat(catname,type);
	
		addSubCat();
		ListView lv = (ListView) findViewById(R.id.listView_Sub_cat);
	registerForContextMenu(lv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sub_category, menu);
		return true;
	}

	
	//add subcategory
	public void addSubCat(){
		final String catt = tv.getText().toString();
		addsub = (EditText) findViewById(R.id.editText_sub_addsub);
		add = (Button) findViewById(R.id.button_sub_add);
		
		add.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(addsub.getText().toString().matches(""))
						{
							addsub.setError("Type a subcategory to add");	
						}	
						if(!addsub.getText().toString().matches(regex))
						{
							addsub.setError("Type a Valid category");	
						}
						else
						{
						
					db.AddSubCat(catt, addsub.getText().toString(),type);
					addsub.setText("");
						
					String catname = tv.getText().toString();
					DisplaySubCat(catname,type);
						}
					}
				}
				
				);
		
		
		
	
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		 if(v.getId()==R.id.listView_Sub_cat)
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
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = item.getItemId();
		String menuItem[] = {"Edit","Delete"};
		String menuitemName = menuItem[index];
		
		 
		 if(menuitemName=="Delete"){
				AlertDialog.Builder builder = new AlertDialog.Builder(SubCategory.this);
				builder.setTitle("Delete Alert");
				builder.setMessage("Do you really want to delete this?");
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(SubCategory.this,Long.toString(info.id), Toast.LENGTH_LONG).show();
						String id = Long.toString(info.id);
						db.DeleteSubcategoryRaw(id);
						String catname = tv.getText().toString();
						DisplaySubCat(catname,type);
						
					}
				}
				);
				builder.setNegativeButton("No", null);
				
				AlertDialog al = builder.create();
				al.show();
				return false;
		 }
		 if(menuitemName=="Edit")
		 {
				final EditText et = (EditText) findViewById(R.id.editText_custom_dialog_edit);			
			 showDialog((int) info.id);
		 }
		 
		 
		
		return super.onContextItemSelected(item);
	}
	
	public void DisplaySubCat(String catt,String TYPE){
		 Cursor cc = db.displaySubCat(catt);
		 String[] from = new String[] {MyDBHandler.COLUMN_CAT_ID,MyDBHandler.COLUMN_CAT_SUBCATEGORY};
		 int[] to = new int[] {R.id.textViewCatId,R.id.textViewCatName};
		 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.cat_layout, cc, from, to,0);
		 ListView catlist = (ListView) findViewById(R.id.listView_Sub_cat);
		 catlist.setAdapter(cusad);
	}
	
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflator = this.getLayoutInflater();
		final View dialogView = inflator.inflate(R.layout.custom_dialog_edit, null);
		dialogBuilder.setView(dialogView);
		et = (EditText)dialogView.findViewById(R.id.editText_custom_dialog_edit);
		
		
		dialogBuilder.setTitle("Category Edit");
		
		String idd = Integer.toString(id);
		et.setText(db.SucatforId(idd).toString());
		//
		
		dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(et.getText().toString().matches(""))
				{
					et.setError("Type a subcategory to add");	
				}	
				if(!et.getText().toString().matches(regex))
				{
					et.setError("Type a Valid category");	
				}
				else{
				String idd = Integer.toString(id);
				String catname = tv.getText().toString();				
				String subcat = et.getText().toString();
				db.UpdateSubCat(idd,subcat);
				DisplaySubCat(catname,"Income");
				Toast.makeText(SubCategory.this,"Successfully Updated", Toast.LENGTH_LONG).show();
				}
			}
		}
		);
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		
		AlertDialog a = dialogBuilder.create();
		a.show();
		
		return super.onCreateDialog(id);
	}
	
		
	}
	
	

