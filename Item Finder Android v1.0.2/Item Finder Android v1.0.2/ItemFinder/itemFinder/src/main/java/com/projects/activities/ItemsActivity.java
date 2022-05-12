package com.projects.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.libraries.html.MGHtml;
import com.libraries.imageview.MGImageView;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Favorite;
import com.models.Item;
import com.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemsActivity extends SwipeRefreshActivity implements OnItemClickListener{
	
	private ArrayList<Item> arrayData;
	DisplayImageOptions options;
	private Queries q;
	private SQLiteDatabase db;

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
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
		.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
		.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		showSwipeProgress();
		sortData();
	}

	private void sortData() {

		Category category = (Category)this.getIntent().getSerializableExtra("category");
		if(category != null) {
			arrayData = q.getItemsByCategoryId(category.getCategory_id());
		}
		else {
			arrayData = (ArrayList<Item>)this.getIntent().getSerializableExtra("items");
		}
		if(arrayData == null || arrayData.size() == 0) {
			MGUtilities.showNotifier(this, MainActivity.offsetY);
			return;
		}

		if(MainActivity.location != null) {
			for(Item item : arrayData) {
				Location locStore = new Location("Store");
				locStore.setLatitude(item.getLat());
				locStore.setLongitude(item.getLon());
				float distance = locStore.distanceTo(MainActivity.location) / 1000;
				item.setDistance(distance);
			}
			Collections.sort(arrayData, new Comparator<Item>() {
				@Override
				public int compare(Item t1, Item t2) {
					if (t1.getDistance() < t2.getDistance())
						return -1;
					if (t1.getDistance() > t2.getDistance())
						return 1;
					return 0;
				}
			});
		}

		showList();
	}
	
	private void showList() {
		
		hideSwipeProgress();
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		MGListAdapter adapter = new MGListAdapter(
				ItemsActivity.this, arrayData.size(), R.layout.item_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@SuppressLint("DefaultLocale") 
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				final Item item = arrayData.get(position);
				Photo p = q.getPhotoByItemId(item.getItem_id());
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.NO_BORDER_WIDTH);
				imgViewPhoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(ItemsActivity.this, DetailItemActivity.class);
						i.putExtra("item", item);
						ItemsActivity.this.startActivity(i);
					}
				});

				if (p != null) {
					MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imgViewPhoto, options);
				} else {
					imgViewPhoto.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
				}

				Spanned name = Html.fromHtml(item.getItem_name());
				Spanned desc = Html.fromHtml(item.getItem_desc());
				String strDesc = MGHtml.decodeToPlainText(desc.toString());

				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);

				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				tvSubtitle.setText(item.getItem_price());

				TextView tvDesc = (TextView) v.findViewById(R.id.tvDesc);
				tvDesc.setText(strDesc);

				ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
				imgViewStarred.setVisibility(View.VISIBLE);
				Favorite fave = q.getFavoriteByItemId(item.getItem_id());
				if(fave == null)
					imgViewStarred.setVisibility(View.INVISIBLE);

				ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
				imgViewFeatured.setVisibility(View.VISIBLE);
				if (item.getFeatured() == 0)
					imgViewFeatured.setVisibility(View.INVISIBLE);

				TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
				tvDistance.setVisibility(View.INVISIBLE);

				ImageView imgViewRibbon = (ImageView) v.findViewById(R.id.imgViewRibbon);
				imgViewRibbon.setImageResource(R.drawable.ribbon_edge_sold);
				imgViewRibbon.setVisibility(View.GONE);

				if(item.getItem_status() == 1)
					imgViewRibbon.setVisibility(View.VISIBLE);

            }
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resid) {
		// TODO Auto-generated method stub
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

}
