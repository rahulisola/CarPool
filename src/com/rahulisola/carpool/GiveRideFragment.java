package com.rahulisola.carpool;

import java.util.ArrayList;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class GiveRideFragment extends Fragment {

	TextView tv;
	View rootView;
	ArrayList<ParseObject> rideList;
	ListView list;

	public GiveRideFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_give_ride, container, false);
		rideList = new ArrayList<ParseObject>();
		tv = (TextView) rootView.findViewById(R.id.title_text);String uid = ParseUser.getCurrentUser().getString("username");


		ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
		query.whereEqualTo("username", uid);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> rides, ParseException e) {
				if (e == null) {
					ParseObject newRide = new ParseObject("routes");

					newRide.add("username", "");
					newRide.add("carNumber", "");
					newRide.add("date", "");
					newRide.add("vacancy", " ");
					newRide.add("location_from", "New");
					newRide.add("location_to", "Ride");
					newRide.add("price", "");

					rideList.add(newRide);

					if(rides.size()>0)
					{
						//tv.setText(cars.get(0).getString("car_number"));
						for(int i=0;i<rides.size();i++)
						{
							ParseObject ride = rides.get(i);
							rideList.add(ride);
						}
					}
					System.out.println(rideList.size());

					RideListAdapter adapter = new RideListAdapter(GiveRideFragment.this.getActivity(), rideList, true);

					list=(ListView) rootView.findViewById(R.id.rideList);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							if(position==0)
							{
								Toast.makeText(GiveRideFragment.this.getActivity(), "Add new Ride", Toast.LENGTH_SHORT).show();
								//Intent i = new Intent(CarFragment.this.getActivity(),AddCarActivity.class);
								//startActivity(i);
								Fragment f = new GiveRideFormActivity();
								FragmentManager fragmentManager = getFragmentManager();
								fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
							}
							else
							{
								Toast.makeText(GiveRideFragment.this.getActivity(), "You Clicked at " +rideList.get(position), Toast.LENGTH_SHORT).show();
								Intent i = new Intent(GiveRideFragment.this.getActivity(),NavigationActivity.class);
								ParseGeoPoint geo_from = rideList.get(position).getParseGeoPoint("geo_from");
								ParseGeoPoint geo_to = rideList.get(position).getParseGeoPoint("geo_to");
								i.putExtra("from_lat", geo_from.getLatitude());
								i.putExtra("from_long", geo_from.getLongitude());
								i.putExtra("to_lat", geo_to.getLatitude());
								i.putExtra("to_long", geo_to.getLongitude());
								startActivity(i);
							}
						}
					});


					list.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if(arg2>0)
							{
								ParseObject.createWithoutData("routes", rideList.get(arg2).getObjectId()).deleteInBackground(new DeleteCallback() {
									@Override
									public void done(ParseException arg0) {
										Toast.makeText(GiveRideFragment.this.getActivity(), "Deleted the ride successfully!", Toast.LENGTH_SHORT).show();
										Fragment f = new GiveRideFragment();
										FragmentManager fragmentManager = getFragmentManager();
										fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
									}
								});
							}
							return false;
						}
					});
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
		return rootView;
	}
}
