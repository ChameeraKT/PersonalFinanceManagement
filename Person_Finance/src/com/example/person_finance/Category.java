package com.example.person_finance;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Category extends Activity {

	
	MyDBHandler db;
	EditText et ;
	Button add;
	ListView lv ;
	String regex = "^[A-Za-z]++$";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		//variable declaration
		 db = new MyDBHandler(this,null,null,1);
		 et = (EditText) findViewById(R.id.editText_sub_addsub);
		 add = (Button) findViewById(R.id.buttonAdd);
		 lv = (ListView) findViewById(R.id.listViewCategory);
		
		 //dispaly the previous added categories
		 DisplayCat("Income");
		 
		//register the context menu for ListView
		 registerForContextMenu(lv);
		 
		 //add button click event
		 add.setOnClickListener(
				 new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						if(et.getText().toString().matches(""))  //check whether the field is empty or not
						{
							et.setError("Type a Category to add");	
						}
						if(!et.getText().toString().matches(regex))
						{
							et.setError("Type a Valid category");	
						}
						else
						{
						String catname = et.getText().toString();
						db.AddCat(catname,"Income");		//call the method to add category
						Toast.makeText(Category.this, "Category Added", Toast.LENGTH_LONG).show();
						DisplayCat("Income");
						et.setText("");
						}
					}
				}
				 
				 );
		 
		
	//go to the subcategory activity when click a category
		 lv.setOnItemClickListener(
				 new OnItemClickListener() {
					 			
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							 	String idd = Long.toString(arg3);
								String catname = db.fromcat(Integer.parseInt(idd));
								
								//make an intent to go to the activity
								Intent i = new Intent(getBaseContext(), SubCategory.class);
								i.putExtra("data", catname);
								i.putExtra("type", "Income");
								startActivity(i);
						}		
				}
				 );
		
	}  //end of onCreate

	
	//crate the contexmenu for edit,delete
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		 if(v.getId()==R.id.listViewCategory)
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
				AlertDialog.Builder builder = new AlertDialog.Builder(Category.this);
				builder.setTitle("Delete Alert");
				builder.setMessage("Do you really want to delete this?");
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//if the user select yes
						String id = Long.toString(info.id);
						db.DeleteCategoryRaw(id);
						Toast.makeText(Category.this,"1 Item Deleted", Toast.LENGTH_LONG).show();			
						DisplayCat("Income");						
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
			 showDialog((int) info.id);		 //call the edit menu
		 }
		return super.onContextItemSelected(item);
	}
	
	//get data of category from DB and set them to a spinner
	public void DisplayCat(String type){
		 Cursor cc = db.displayCat(type);
		 String[] from = new String[] {MyDBHandler.COLUMN_CAT_CATEGORY};
		 int[] to = new int[] {R.id.textViewCatName};
		 SimpleCursorAdapter cusad = new SimpleCursorAdapter(getBaseContext(), R.layout.cat_layout, cc, from, to,0);
		 ListView catlist = (ListView) findViewById(R.id.listViewCategory);
		 catlist.setAdapter(cusad);
		 	 
	}

	//create a dialog box for edit category
	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflator = this.getLayoutInflater();
		final View dialogView = inflator.inflate(R.layout.custom_dialog_edit, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setTitle("Category Edit");
		et = (EditText)dialogView.findViewById(R.id.editText_custom_dialog_edit);
		
		//set category to the textView in the dialog box
		String idd = Integer.toString(id);
		et.setText(db.CatforId(idd).toString());
		final String category = et.getText().toString();
		
		//set the update button click event
		dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(et.getText().toString().matches(""))       //check whether textfield is empty or not
					Toast.makeText(Category.this,"Cannot Update. Category name cannot be empty", Toast.LENGTH_LONG).show();
				if(!et.getText().toString().matches(regex))
				{
					et.setError("Type a Valid category");	
				}
				else{				
				String catEdit = et.getText().toString();			//update the category
				db.UpdateCat(category,catEdit);
				DisplayCat("Income");
				Toast.makeText(Category.this,"Successfully Updated", Toast.LENGTH_LONG).show();
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