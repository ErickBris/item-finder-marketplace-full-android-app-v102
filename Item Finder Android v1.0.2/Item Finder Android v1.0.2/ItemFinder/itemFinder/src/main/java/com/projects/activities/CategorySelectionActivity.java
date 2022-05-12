package com.projects.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.config.Config;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.adapters.MGListAdapter;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectionActivity extends SwipeRefreshActivity implements OnItemClickListener{
	
	private Queries q;
	private SQLiteDatabase db;
	private List<Category> arrayData;
    private Category selectedCategory;
    int pid = 0;
	boolean willAddAllCategories = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_category);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.getActionBar().setIcon(R.drawable.header_logo);
        this.getActionBar().setTitle("");
		
		DbHelper dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);

		showSwipeProgress();
		willAddAllCategories = this.getIntent().getBooleanExtra("willAddAllCategories", false);
        selectedCategory = (Category)this.getIntent().getSerializableExtra("category");
        if(selectedCategory != null)
            pid = selectedCategory.getCategory_id();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setList();
			}
		}, Config.DELAY_SHOW_ANIMATION);
		
	}
	
	private void setList() {
		arrayData = q.getCategoriesByPID(pid);

		if(willAddAllCategories) {
			Category cat = new Category();
			cat.setPid(0);
			cat.setCategory_id(-1);
			cat.setCategory(MGUtilities.getStringFromResource(this, R.string.all_categories));
			arrayData.add(0, cat);
		}

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		MGListAdapter mAdapter = new MGListAdapter(this, arrayData.size(), R.layout.category_entry);
		mAdapter.setOnMGListAdapterAdapterListener(new MGListAdapter.OnMGListAdapterAdapterListener() {

			@Override
			public void OnMGListAdapterAdapterCreated(
					MGListAdapter adapter, View v, int position, ViewGroup viewGroup) {

				Category category = arrayData.get(position);
				Spanned title = Html.fromHtml(category.getCategory());
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(title);
			}
		});

		listView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		hideSwipeProgress();

		if(arrayData != null && arrayData.size() == 0) {
			MGUtilities.showNotifier(this, MainActivity.offsetY);
			return;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		Category category = arrayData.get(pos);
		ArrayList<Category> categories = q.getCategoriesByPID(category.getCategory_id());
		Intent i;
		if(categories.size() > 0) {
			i = new Intent(this, CategorySelectionActivity.class);
			i.putExtra("category", category);
			startActivityForResult(i, Config.CATEGORY_SELECTION_REQUEST_CODE);
		}
		else {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("category", category);
			setResult(RESULT_OK,returnIntent);
			finish();
		}
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == Config.CATEGORY_SELECTION_REQUEST_CODE) {
	        if(resultCode == RESULT_OK) {
	        	Category category = (Category) data.getSerializableExtra("category");
	        	Intent returnIntent = new Intent();
				returnIntent.putExtra("category", category);
				setResult(RESULT_OK,returnIntent);
				finish();
	        }
	        if (resultCode == RESULT_CANCELED) { }
	    }
	}
}
