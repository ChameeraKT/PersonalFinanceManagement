package com.example.person_finance;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Category_main extends ActivityGroup {

	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_main);
		
		 tabHost = (TabHost) findViewById(android.R.id.tabhost);
	        tabHost.setup(this.getLocalActivityManager());
	      
	        TabSpec spec= tabHost.newTabSpec("tag1");
	        spec.setIndicator("Income");
	        Intent intent=new Intent(this,Category.class);
	        intent.putExtra("keyname","income");
	        spec.setContent(intent);
	        tabHost.addTab(spec);
	       
	       
	        spec= tabHost.newTabSpec("tag2");
	        spec.setIndicator("Expenses");
	        intent=new Intent(this, Category_Expenses.class);
	        intent.putExtra("keyname","expenses");
	        spec.setContent(intent);
	        tabHost.addTab(spec);
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_main, menu);
		return true;
	}

}
