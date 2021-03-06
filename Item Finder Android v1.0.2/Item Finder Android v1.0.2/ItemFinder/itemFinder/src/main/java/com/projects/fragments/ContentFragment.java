package com.projects.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.etsy.android.grid.StaggeredGridView;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.libraries.dataparser.DataParser;
import com.libraries.imageview.StaggeredImageView;
import com.models.Category;
import com.models.Data;
import com.models.Item;
import com.models.Photo;
import com.projects.activities.DetailItemActivity;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class ContentFragment extends Fragment implements OnItemClickListener {

	private View viewInflate;
	ArrayList<Item> arrayData;
	MGAsyncTask task;
    Queries q;

	public ContentFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_content, null);
		return viewInflate;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        q = main.getQueries();
		main.showSwipeProgress();
		main.getDebugKey();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {

            @Override
            public void run() {
                getData();
            }
        }, Config.DELAY_SHOW_ANIMATION);
	}

	public void getData() {
		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {

            @Override
            public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

            @Override
            public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
                asyncTask.dialog.hide();
            }

            @Override
            public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
                MainActivity main = (MainActivity) getActivity();
                arrayData = q.getItems();
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
                main.hideSwipeProgress();
            }

            @Override
            public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
                try {
                    String strUrl = String.format(
                            "%s?api_key=%s&min_count=%d&max_count=%d",
                            Config.DATA_ITEM_JSON_URL,
                            Config.API_KEY,
                            0,
                            1000);

                    DataParser parser = new DataParser();
                    Data data = parser.getData(strUrl);
                    MainActivity main = (MainActivity) getActivity();
                    if (main == null)
                        return;

                    Queries q = main.getQueries();
                    if (data == null)
                        return;

                    if(data.getItems() != null) {
                        q.deleteTable("items");
                        q.deleteTable("users");
                    }

                    syncItems(data.getItems());
                    syncItems(data.getUser_items());

                    data = parser.getData(Config.CATEGORY_JSON_URL);
                    if (data == null)
                        return;

                    if (data.getCategories() != null && data.getCategories().size() > 0) {
                        q.deleteTable("categories");
                        for (Category category : data.getCategories()) {
                            q.deleteCategory(category.getCategory_id());
                            q.insertCategory(category);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
		task.execute();
	}

    private void syncItems(ArrayList<Item> items) {
        if (items != null && items.size() > 0) {
            for (Item item : items) {
                q.deleteItem(item.getItem_id());
                q.insertItem(item);
                if (item.getPhotos() != null) {
                    for (Photo photo : item.getPhotos()) {
                        q.deletePhoto(photo.getPhoto_id());
                        q.insertPhoto(photo);
                    }
                }
                if (item.getUser() != null) {
                    q.deleteUser(item.getUser().getUser_id());
                    q.insertUser(item.getUser());
                }
            }
        }
    }
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
    }
	
	private void showList() {
		if(arrayData == null)
			return;

		MGListAdapter adapter = new MGListAdapter(
				getActivity(), arrayData.size(), R.layout.item_grid_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {

            @Override
            public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
                                                      int position, ViewGroup viewGroup) {
                // TODO Auto-generated method stub
                Item item = arrayData.get(position);
                Photo photo = q.getPhotoByItemId(item.getItem_id());
                StaggeredImageView imgViewThumb = (StaggeredImageView) v.findViewById(R.id.imgViewThumb);
                imgViewThumb.setCornerRadius(0.0f);
                imgViewThumb.setBorderWidth(UIConfig.BORDER_WIDTH);
                imgViewThumb.setBorderColor(getResources().getColor(UIConfig.THEME_MAIN_COLOR));
                if (photo != null && photo.getThumb_url() != null) {
                    MainActivity.getImageLoaderInstance(getActivity()).displayImage(
                            photo.getThumb_url(),
                            imgViewThumb,
                            MainActivity.getDisplayImageOptionsInstance());
                } else {
                    MainActivity.getImageLoaderInstance(getActivity()).displayImage(
                            null,
                            imgViewThumb,
                            MainActivity.getDisplayImageOptionsInstance());
                }
                TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                tvTitle.setText(item.getItem_price());

                double positionHeight = getPositionRatio(position);
                imgViewThumb.setHeightRatio(positionHeight);

                LinearLayout linearRibbon = (LinearLayout) v.findViewById(R.id.linearRibbon);
                linearRibbon.setBackgroundResource(R.drawable.item_ribbon);

                if(item.getItem_status() == 1) {
                    linearRibbon.setBackgroundResource(R.drawable.item_ribbon_sold);
                    tvTitle.setText(R.string.sold);
                }

                if(item.getFeatured() == 1)
                    linearRibbon.setBackgroundResource(R.drawable.item_ribbon_featured);
            }
        });

		StaggeredGridView gridView = (StaggeredGridView) viewInflate.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(this);
		gridView.setAdapter(adapter);
	}

    private Random mRandom = new Random();
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("getPositionRatio", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        // height will be 1.0 - 1.5
        // return (mRandom.nextDouble() / 2.0) + 1.0;
        return (mRandom.nextDouble() / 1.5) + 0.75;
        // the width
    }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		Item item = arrayData.get(pos);
        Intent i = new Intent(getActivity(), DetailItemActivity.class);
        i.putExtra("item", item);
        getActivity().startActivity(i);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}
