package com.rahulisola.carpool;

import java.util.ArrayList;
import java.util.Date;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RideListAdapter extends ArrayAdapter<ParseObject>{

	ImageView imageView;
	private final Activity context;
	
	public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

	private ArrayList<ParseObject> ride;
	boolean editMode;

	public RideListAdapter(Activity context, ArrayList<ParseObject> rideList, boolean editMode) {
		super(context, R.layout.list_row, rideList);
		this.context = context;
		this.ride = rideList;
		this.editMode = editMode;
	}
	
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		final View rowView = inflater.inflate(R.layout.list_row, null, true);
		System.out.println(context.toString());
		if(position==0 && editMode)
		{
			TextView txtTitle = (TextView) rowView.findViewById(R.id.description_text);
			TextView car_info = (TextView) rowView.findViewById(R.id.title_text);
			car_info.setText("Post a new ride");
			txtTitle.setText("");
			ImageView icon = (ImageView) rowView.findViewById(R.id.img);
			icon.setImageResource(R.drawable.map_add);
		}
		else
		{
			TextView txtTitle = (TextView) rowView.findViewById(R.id.description_text);
			TextView car_info = (TextView) rowView.findViewById(R.id.title_text);
			TextView contact = (TextView) rowView.findViewById(R.id.contact_text);
			car_info.setText(ride.get(position).getString("location_from")+"->"+ride.get(position).getString("location_to"));
			Date date = ride.get(position).getDate("date");
			txtTitle.setText(DateFormat.format("yyyy/MM/dd", date).toString()+" " + " ($" + ride.get(position).getString("price")+")");
			contact.setText(ParseUser.getCurrentUser().getEmail() + " ("+ParseUser.getCurrentUser().getString("Phone")+")");
			ImageView icon = (ImageView) rowView.findViewById(R.id.img);
			icon.setImageResource(R.drawable.map);
		}
		return rowView;
	}
}