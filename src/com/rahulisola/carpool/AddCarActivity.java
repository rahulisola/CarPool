package com.rahulisola.carpool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddCarActivity extends Fragment {
	Camera camera;
	EditText pcarnum;
	EditText pcarmake;
	EditText pcarmodel;
	EditText pcaryear;
	EditText pcarcolor;
	EditText pcardesc;

	Button post;

	String tcarnum;
	String tcarmake;
	String tcarmodel;
	String tcaryear;
	String tcarcolor;
	String tcardesc;

	Button photos;

	File imagesFolder;
	File image;
	byte[] data;
	FileInputStream fis;
	ByteArrayOutputStream bos;
	byte[] buf;
	ParseFile file1;
	Bitmap mImageBitmap;
	View rootView;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		ImageView mImageView = (ImageView) rootView.findViewById(R.id.imageView1);
		mImageView.setImageBitmap(mImageBitmap);
		if(mImageBitmap!=null)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			file1 = new ParseFile("photo.jpg", byteArray);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_add_car, container, false);

		pcarnum = (EditText) rootView.findViewById(R.id.pcarnum);
		pcarmake = (EditText) rootView.findViewById(R.id.pcarmake);
		pcarmodel = (EditText) rootView.findViewById(R.id.pcarmodel);
		pcaryear= (EditText) rootView.findViewById(R.id.pcaryear);
		pcarcolor = (EditText) rootView.findViewById(R.id.pcarcolor);
		pcardesc = (EditText) rootView.findViewById(R.id.pcardesc);

		photos=(Button)rootView.findViewById(R.id.photosButton);

		post = (Button) rootView.findViewById(R.id.post);

		photos.setOnClickListener(new OnClickListener() {
			@Override 
			public void onClick(View v) { 
				// TODO Auto-generated method stub 
				if (camera!=null)
				{ 
					camera.stopPreview(); 
					camera.release(); 
					camera=null; 
				} 
				else 
				{
					try{
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
						startActivityForResult(intent, 0);
					}
					catch(Exception e){ 
						Log.d("Camera", "Failed to open Camera"); 
					}
				}
			}
		});

		post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tcarnum = pcarnum.getText().toString();
				tcarmake = pcarmake.getText().toString();
				tcarmodel = pcarmodel.getText().toString();
				tcaryear = pcaryear.getText().toString();
				tcarcolor = pcarcolor.getText().toString();
				tcardesc = pcardesc.getText().toString();

				if (tcarnum.equals("") && tcarmake.equals("") && tcarmodel.equals("")
						&& tcaryear.equals("") && tcarcolor.equals("")
						&& tcardesc.equals("")) {
					Toast.makeText(rootView.getContext().getApplicationContext(),
							"Please fill all details",
							Toast.LENGTH_LONG).show();
				} else {

					// Save new user data into Parse.com Data Storage
					ParseObject publish = new ParseObject("car_details");
					publish.put("username",ParseUser.getCurrentUser().getUsername());
					publish.put("car_number",tcarnum);
					publish.put("car_make",tcarmake);
					publish.put("car_model",tcarmodel);
					publish.put("car_year",tcaryear);
					publish.put("car_color",tcarcolor);
					publish.put("car_description",tcardesc);
					System.out.println(file1);
					if(file1!=null)
					{
						publish.put("car_photo", file1);
					}
					publish.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								// Show a simple Toast message upon successful
								// registration
								Toast.makeText(rootView.getContext().getApplicationContext(),"Posted!!",Toast.LENGTH_LONG).show();
										getFragmentManager().popBackStackImmediate();
							} else {
								Toast.makeText(rootView.getContext().getApplicationContext(),
										"Publish Error", Toast.LENGTH_LONG)
										.show();
							}
						}
					});
				}
			}
		});
		return rootView;
	}
}
