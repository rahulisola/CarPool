package com.rahulisola.carpool;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Fragment;
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

public class FindRideFragment extends Fragment {

	TextView tv;
	View rootView;
	ArrayList<ParseObject> rideList;
	ListView list;

	public FindRideFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_give_ride, container, false);
		rideList = new ArrayList<ParseObject>();
		tv = (TextView) rootView.findViewById(R.id.title_text);
		String uid = ParseUser.getCurrentUser().getString("username");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> rides, ParseException e) {
				if (e == null) {
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

					RideListAdapter adapter = new RideListAdapter(FindRideFragment.this.getActivity(), rideList, false);

					list=(ListView) rootView.findViewById(R.id.rideList);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							Toast.makeText(FindRideFragment.this.getActivity(), "You Clicked at " +rideList.get(position), Toast.LENGTH_SHORT).show();
							Intent i = new Intent(FindRideFragment.this.getActivity(),NavigationActivity.class);
							ParseGeoPoint geo_from = rideList.get(position).getParseGeoPoint("geo_from");
							ParseGeoPoint geo_to = rideList.get(position).getParseGeoPoint("geo_to");
							i.putExtra("from_lat", geo_from.getLatitude());
							i.putExtra("from_long", geo_from.getLongitude());
							i.putExtra("to_lat", geo_to.getLatitude());
							i.putExtra("to_long", geo_to.getLongitude());
							startActivity(i);
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