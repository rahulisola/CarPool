package com.rahulisola.carpool;

import java.util.ArrayList;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<ParseObject>{

	ImageView imageView;
	private final Activity context;

	private ArrayList<ParseObject> car;

	public ListAdapter(Activity context, ArrayList<ParseObject> carList) {
		super(context, R.layout.list_row, carList);
		this.context = context;
		this.car = carList;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		final View rowView= inflater.inflate(R.layout.list_row, null, true);
		if(position==0)
		{
			TextView txtTitle = (TextView) rowView.findViewById(R.id.description_text);
			TextView car_info = (TextView) rowView.findViewById(R.id.title_text);
			imageView = (ImageView) rowView.findViewById(R.id.img);
			car_info.setText("Add New Car");
			imageView.setImageResource(R.drawable.car_add);
		}
		else
		{
			ParseFile imageFile = (ParseFile) car.get(position).get("car_photo");
			if(imageFile!=null)
			{
				imageFile.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, ParseException e) {
						if (e == null)
						{
							Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
							imageView = (ImageView) rowView.findViewById(R.id.img);
							imageView.setImageBitmap(bm);
						} else {
							// something went wrong
						}
					}
				});	

			}
			TextView txtTitle = (TextView) rowView.findViewById(R.id.description_text);
			TextView car_info = (TextView) rowView.findViewById(R.id.title_text);
			txtTitle.setText(car.get(position).getString("car_description"));
			car_info.setText(car.get(position).getString("car_color")+" "+car.get(position).getString("car_make")+" " + car.get(position).getString("car_model") + " (" + car.get(position).getString("car_number")+")");
		}
		return rowView;
	}
}