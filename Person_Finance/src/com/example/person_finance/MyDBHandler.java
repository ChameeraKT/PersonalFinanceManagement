package com.example.person_finance;

import java.util.Date;

import android.R.string;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper{
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "DB2.db";
		public static final String TABLE_SCHEDULE = "schedule";
		public static final String TABLE_CATEGORY = "category";
		public static final String TABLE_ALLOCATION = "allocation";
	
		//coloums of schedule table
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_CAT = "category";
		public static final String COLUMN_SUBCAT = "subcategory";
		public static final String COLUMN_DESCRIPTION = "description";
		
		//columns of category table
		public static final String COLUMN_CAT_ID = "categoryid";
		public static final String COLUMN_CAT_CATEGORY = "categoryname";
		public static final String COLUMN_CAT_SUBCATEGORY = "subcategoryname";
		public static final String COLUMN_CAT_TYPE = "type";
		
		//columns of allocation table
		public static final String COLUMN_AL_ID = "al_id";
		public static final String COLUMN_AL_CATEGORY = "al_cat";
		public static final String COLUMN_AL_MONTH = "al_month";
		public static final String COLUMN_AL_AMOUNT = "al_amount";
		
		public static final String[] ALL_CAT = new String[]{COLUMN_CAT_ID+" as _id ",COLUMN_CAT_ID,COLUMN_CAT_CATEGORY,COLUMN_CAT_SUBCATEGORY,COLUMN_CAT_TYPE};
		public static final String[] ALL_SCHEDULE = new String[]{COLUMN_ID+" as _id ",COLUMN_ID,COLUMN_DATE,COLUMN_CAT,COLUMN_SUBCAT,COLUMN_DESCRIPTION};
		public static final String[] ALL_ALLOCATION = new String[]{COLUMN_AL_ID+" as _id ",COLUMN_AL_ID,COLUMN_AL_MONTH,COLUMN_AL_CATEGORY,COLUMN_AL_AMOUNT};
		
public MyDBHandler(Context context,String name, SQLiteDatabase.CursorFactory factory, int version){
	super(context,DATABASE_NAME , factory, DATABASE_VERSION);

	
}

@Override
public void onCreate(SQLiteDatabase db) {
String query = "CREATE TABLE "+ TABLE_SCHEDULE +"("
				+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				COLUMN_DATE+" DATE, "+
				COLUMN_CAT+" TEXT, "+
				COLUMN_SUBCAT+" TEXT, "+
				COLUMN_DESCRIPTION+" TEXT "+
				
		")";    

String query2 = "CREATE TABLE "+ TABLE_CATEGORY +" ( "
		+COLUMN_CAT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
		COLUMN_CAT_CATEGORY+" TEXT, "+
		COLUMN_CAT_SUBCATEGORY+" TEXT, "+
		COLUMN_CAT_TYPE+" TEXT )";

String query3 = "CREATE TABLE "+ TABLE_ALLOCATION +" ( "
		+COLUMN_AL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
		COLUMN_AL_CATEGORY+" TEXT, "+
		COLUMN_AL_MONTH+" TEXT," +
		COLUMN_AL_AMOUNT+" TEXT  )";

 String query4 = "CREATE TABLE "+ Ex_db.exp_info.Table_name+"("+ Ex_db.exp_info.SubCategory+" TEXT,"+
Ex_db.exp_info.Acount+" TEXT,"+ Ex_db.exp_info.Price+" TEXT,"+ Ex_db.exp_info.Payee+" TEXT,"+
Ex_db.exp_info.Project+" TEXT,"+ Ex_db.exp_info.Priodic+" TEXT,"+ Ex_db.exp_info.note+" TEXT, "+Ex_db.exp_info.Category+" TEXT, " +
        Ex_db.exp_info.date+" TEXT);";

	db.execSQL(query);
	db.execSQL(query2);
	db.execSQL(query3);
	db.execSQL(query4);
	
	
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHEDULE);
	db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
	db.execSQL("DROP TABLE IF EXISTS "+TABLE_ALLOCATION);
	db.execSQL("DROP TABLE IF EXISTS "+Ex_db.exp_info.Table_name);
	onCreate(db);

}
//Add schedule
public void AddSchedule(String date,String catt,String subcatt,String dess){
	ContentValues values = new ContentValues();
	values.put(COLUMN_DATE, date);
	values.put(COLUMN_CAT, catt);
	values.put(COLUMN_SUBCAT, subcatt);
	values.put(COLUMN_DESCRIPTION, dess);
	
	SQLiteDatabase db = getWritableDatabase();
	db.insert(TABLE_SCHEDULE, null, values);
	db.close();
}

//display schedules for input date
  public String DBtoString(String date) {
	String dbString="";
	SQLiteDatabase db = getWritableDatabase();
	String query = "Select * from "+TABLE_SCHEDULE+" WHERE "+COLUMN_DATE+" =\'"+date+"\'";
	
	
	Cursor c = db.rawQuery(query, null);
	c.moveToFirst();
	
	 while(c.moveToNext()){
		if(c.getString(c.getColumnIndex("description"))!=null){
			dbString += c.getString(c.getColumnIndex("description"));
			dbString += "\n";
		}
	  }
	db.close();
	return dbString;
}
  
  //ADD catagory
  
  public void AddCat(String cat,String type){
	  	String sub = "";
		ContentValues values = new ContentValues();
		values.put(COLUMN_CAT_CATEGORY, cat);
		values.put(COLUMN_CAT_SUBCATEGORY,sub);
		values.put(COLUMN_CAT_TYPE,type);
		
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_CATEGORY, null, values);
		db.close();
	}
  
//Display all category table data
  public Cursor displayCat(String type){
	  SQLiteDatabase db = getWritableDatabase();;
	  String where = COLUMN_CAT_TYPE +" =\'"+type+"\'";
	  Cursor c = db.query(true,TABLE_CATEGORY,ALL_CAT,where,null,COLUMN_CAT_CATEGORY,null,null,null);
	
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
  }
  
  
  //return details from catgory accrding to the ID
  public String fromcat(int id)
  	{
	  String cat="";
		SQLiteDatabase db = getWritableDatabase();
		String query = "Select * from "+TABLE_CATEGORY+" WHERE "+COLUMN_CAT_ID+" =\'"+id+"\'";
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		if(c.getString(c.getColumnIndex("categoryname"))!=null)
			{
			cat += c.getString(c.getColumnIndex("categoryname"));
			}
		
		  return cat;
  }

 //Add Sub category
  public void AddSubCat(String cat,String sub,String type){
		ContentValues values = new ContentValues();
		values.put(COLUMN_CAT_CATEGORY, cat);
		values.put(COLUMN_CAT_SUBCATEGORY,sub);
		values.put(COLUMN_CAT_TYPE,type);
	
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_CATEGORY, null, values);
		
	//	ContentValues valuess = new ContentValues();
	//	valuess.put(Ex_db.exp_info.Price,"10000");
		
	//	SQLiteDatabase dbv = getWritableDatabase();
	//	dbv.insert(Ex_db.exp_info.Table_name, null, valuess);
		db.close();	
	}
  
  //Display subcat according to cat
  public Cursor displaySubCat(String catt){
	  String nul ="";
	  SQLiteDatabase db = getWritableDatabase();
	  String where = COLUMN_CAT_CATEGORY+ " = \'"+catt+"\' and "+COLUMN_CAT_SUBCATEGORY+ " != \'"+nul+"\' "; 
	  Cursor c = db.query(true,TABLE_CATEGORY,ALL_CAT,where,null,null,null,null,null);
	//  Cursor c = db.rawQuery("select "+COLUMN_CAT_ID +" as _id, "+COLUMN_CAT_CATEGORY+" , "+COLUMN_CAT_SUBCATEGORY +" from "+TABLE_CATEGORY, null);
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
  }
  
  
  //Display Schedule for a particular date
  public Cursor displaySchedule(String date){
	  SQLiteDatabase db = getWritableDatabase();;
	  String where = COLUMN_DATE+" = \'"+date+"\' ";
	  Cursor c = db.query(true,TABLE_SCHEDULE,ALL_SCHEDULE,where,null,null,null,null,null);
	
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
  }
  
  //Add allocations
  public void AddAllocations(String month,String cat,String amt){
		ContentValues values = new ContentValues();
		values.put(COLUMN_AL_MONTH, month);
		values.put(COLUMN_AL_CATEGORY,cat);
		values.put(COLUMN_AL_AMOUNT,amt);
		
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_ALLOCATION, null, values);
		db.close();
	}
  
//Display Allocation
  public Cursor displayAllocation(){
	  SQLiteDatabase db = getWritableDatabase();
	  String where = "";
	  Cursor c = db.query(true,TABLE_ALLOCATION,ALL_ALLOCATION,where,null,null,null,null,null);
	
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
  }

//delete allocation
public void DeleteAllocationRaw(String id){
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("DELETE FROM "+TABLE_ALLOCATION+" where " +COLUMN_AL_ID+" = \'"+id+"\'");
	
	
}

//get the count of allocation for given month and category
public String getcount(String month,String cat){

	  SQLiteDatabase db = getWritableDatabase();
	
	  Cursor c = db.rawQuery("select count("+COLUMN_AL_ID +")as id from "+TABLE_ALLOCATION+" WHERE "+COLUMN_AL_MONTH+" = \'"+month+"\' and "+COLUMN_AL_CATEGORY+ " = \'"+cat+"\'", null);
		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			cat = c.getString(c.getColumnIndex("id"));
			}
	return cat;
	
	
}

//delete subcategory according to the id
public void DeleteSubcategoryRaw(String id){
	
	 String cat="";
		SQLiteDatabase db = getWritableDatabase();
		String query = "Select * from "+TABLE_CATEGORY+" WHERE "+COLUMN_CAT_ID+" =\'"+id+"\'";
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		if(c.getString(c.getColumnIndex("subcategoryname"))!=null)
			{
			cat += c.getString(c.getColumnIndex("subcategoryname"));
			}

	 db.execSQL("DELETE FROM "+TABLE_CATEGORY+" where " +COLUMN_CAT_SUBCATEGORY+" = \'"+cat+"\'");	
}

//delete category according to the id
public void DeleteCategoryRaw(String id)
{
	 String cat="";
		SQLiteDatabase db = getWritableDatabase();
		String query = "Select * from "+TABLE_CATEGORY+" WHERE "+COLUMN_CAT_ID+" =\'"+id+"\'";
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		if(c.getString(c.getColumnIndex("categoryname"))!=null)
			{
			cat += c.getString(c.getColumnIndex("categoryname"));
			}

	 db.execSQL("DELETE FROM "+TABLE_CATEGORY+" where " +COLUMN_CAT_CATEGORY+" = \'"+cat+"\'");	
}

//return the total of the income
public int getIncomeTotal(){
	int tot=0;
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select SUM("+ Ex_db.exp_info.Price +")as id from "+ Ex_db.exp_info.Table_name,null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			tot =+ Integer.parseInt(c.getString(c.getColumnIndex("id")));
			}
	return tot;
}

//return the total of the allocations
public int getAllocationTotal(){
	int tot=0;
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select SUM("+COLUMN_AL_AMOUNT +")as id from "+TABLE_ALLOCATION,null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			tot =+ Integer.parseInt(c.getString(c.getColumnIndex("id")));
			}
	return tot;
}

//return the subcategory name according to the id
public String SucatforId(String id){
	String sub= "";
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select "+COLUMN_CAT_SUBCATEGORY +" as id from "+TABLE_CATEGORY+" where "+COLUMN_CAT_ID+" = \'"+id+"\'",null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			sub = (c.getString(c.getColumnIndex("id")));
			}
	return sub;
}

//update the subcategory
public void UpdateSubCat(String id,String subcat){
	
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("Update "+TABLE_CATEGORY+" set "+COLUMN_CAT_SUBCATEGORY+"=\'"+subcat+"\' where " +COLUMN_CAT_ID+" = \'"+id+"\'");
	
}

//return the category name according to the id
public String CatforId(String id){
	String CAT= "";
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select "+COLUMN_CAT_CATEGORY +" as id from "+TABLE_CATEGORY+" where "+COLUMN_CAT_ID+" = \'"+id+"\'",null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			CAT = (c.getString(c.getColumnIndex("id")));
			}
	return CAT;
}

//upadte the category
public void UpdateCat(String categoty,String catEdit){
	
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("Update "+TABLE_CATEGORY+" set "+COLUMN_CAT_CATEGORY+"=\'"+catEdit+"\' where " +COLUMN_CAT_CATEGORY+" = \'"+categoty+"\'");
	
}

//return allocation date according to the id
public Cursor AlocationsforId(String id){
	String CAT= "";
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select * from "+TABLE_ALLOCATION+" where "+COLUMN_AL_ID+" = \'"+id+"\'",null);

	return c;
}

//Update the alllocations
public void UpdateAllocations(String id,String month,String cat,String amount){
	
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("Update "+TABLE_ALLOCATION+" set "+COLUMN_AL_MONTH+"=\'"+month+"\',"+COLUMN_AL_CATEGORY+"=\'"+cat+"\',"+COLUMN_AL_AMOUNT+"=\'"+amount+"\' where " +COLUMN_AL_ID+" = \'"+id+"\'");
	
}

//delete a schedule according to the id
public void DeleteScheduleRaw(String id){
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("DELETE FROM "+TABLE_SCHEDULE+" where " +COLUMN_ID+" = \'"+id+"\'");
	
	
}

//display schedude data for the id
public Cursor displayScheduleforId(String id){
	  SQLiteDatabase db = getWritableDatabase();
	  String where = COLUMN_ID+"= \'"+id+"\'";
	  Cursor c = db.query(true,TABLE_SCHEDULE,ALL_SCHEDULE,where,null,null,null,null,null);
	
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
}

//update schedule
public void UpdateSchedule(String id,String date,String cat,String subcat,String des){
	
	 SQLiteDatabase db = getWritableDatabase();
	 db.execSQL("Update "+TABLE_SCHEDULE+" set "+COLUMN_DATE+"=\'"+date+"\',"+COLUMN_CAT+"=\'"+cat+"\',"+COLUMN_SUBCAT+"=\'"+subcat+"\',"+COLUMN_DESCRIPTION+"=\'"+des+"\' where " +COLUMN_ID+" = \'"+id+"\'");
	
}

//return id for the given category name
public String idforCatname(String name){
	String CAT= "";
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select "+COLUMN_CAT_ID+" as id from "+TABLE_CATEGORY+" where "+COLUMN_CAT_CATEGORY+" = \'"+name+"\'",null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			CAT = (c.getString(c.getColumnIndex("id")));
			}
	return CAT;
}

//return id for the given subcategory name
public String idforSubCatname(String name){
	String CAT= "";
	 SQLiteDatabase db = getWritableDatabase();
	  Cursor c = db.rawQuery("select "+COLUMN_CAT_ID+" as id from "+TABLE_CATEGORY+" where "+COLUMN_CAT_SUBCATEGORY+" = \'"+name+"\'",null);

		c.moveToFirst();
		if(c.getString(c.getColumnIndex("id"))!=null)
			{
			CAT = (c.getString(c.getColumnIndex("id")));
			}
	return CAT;
}

//return all the schedule details
public Cursor ViewScheduleDateRange(){
	SQLiteDatabase db = getWritableDatabase();
	  String where = "";
	  Cursor c = db.query(true,TABLE_SCHEDULE,ALL_SCHEDULE,where,null,null,null,null,null);
	
	   if(c != null){
		   c.moveToFirst();
	   }
	  return c;
}

}
