package com.rahulisola.carpool;

import java.util.ArrayList;
import java.util.List;


import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CarFragment extends Fragment {

	TextView tv;
	View rootView;
	ArrayList<ParseObject> carList;
	ListView list;

	public CarFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_car, container, false);
		carList = new ArrayList<ParseObject>();
		tv = (TextView) rootView.findViewById(R.id.title_text);
		String uid = ParseUser.getCurrentUser().getString("username");


		ParseQuery<ParseObject> query = ParseQuery.getQuery("car_details");
		query.whereEqualTo("username", uid);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> cars, ParseException e) {
				if (e == null) {
					carList.add(null);

					if(cars.size()>0)
					{
						//tv.setText(cars.get(0).getString("car_number"));
						for(int i=0;i<cars.size();i++)
						{
							ParseObject car = cars.get(i);
							carList.add(cars.get(i));

						}
						System.out.println(carList.size());

					}

					ListAdapter adapter = new ListAdapter(CarFragment.this.getActivity(), carList);

					list=(ListView) rootView.findViewById(R.id.carList);
					list.setAdapter(adapter);
					System.out.println(list.getCount());
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							if(position==0)
							{
								Toast.makeText(CarFragment.this.getActivity(), "Add new Car", Toast.LENGTH_SHORT).show();
								Fragment f = new AddCarActivity();
								FragmentManager fragmentManager = getFragmentManager();
								fragmentManager.beginTransaction()
								.replace(R.id.frame_container, f).commit();
							}
							else
							{
								Toast.makeText(CarFragment.this.getActivity(), "Click and hold to delete " +carList.get(position).getString("car_number"), Toast.LENGTH_SHORT).show();
							}
						}
					});

					list.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if(arg2>0)
							{
								ParseObject.createWithoutData("car_details", carList.get(arg2).getObjectId()).deleteInBackground(new DeleteCallback() {
									@Override
									public void done(ParseException arg0) {
										Toast.makeText(CarFragment.this.getActivity(), "Deleted the car successfully!", Toast.LENGTH_SHORT).show();
										Fragment f = new CarFragment();
										FragmentManager fragmentManager = getFragmentManager();
										fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
									}
								});
							}
							return false;
						}
					});
				} else {
				}
			}
		});

		return rootView;
	}
}
