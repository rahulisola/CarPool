package com.rahulisola.carpool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.hardware.Camera;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GiveRideFormActivity extends Fragment implements OnItemSelectedListener {
	Camera camera;
	EditText pcarnumgr;
	EditText pfrom;
	EditText pto;
	EditText pdate;
	EditText pprice;

	Button post;

	String tcarnum;
	String tfrom;
	String tto;
	String tdate;
	String tprice;
	View rootView;
	Spinner pcarnumlist;
	List<String> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_give_ride_form, container, false);

		pcarnumlist = (Spinner) rootView.findViewById(R.id.pcarnumlist);
		
		list = new ArrayList<String>();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("car_details");
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> cars, ParseException e) {
				if (e == null)
				{
					if(cars.size()>0)
					{
						for(int i=0;i<cars.size();i++)
						{
							ParseObject car = cars.get(i);
							list.add(car.getString("car_number"));
						}
					}
					else
					{
						list.add("Add New Car");
					}
				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				pcarnumlist.setAdapter(dataAdapter);
				pcarnumlist.setOnItemSelectedListener(GiveRideFormActivity.this);
			}
		});
		pfrom = (EditText) rootView.findViewById(R.id.pfrom);
		pto = (EditText) rootView.findViewById(R.id.pto);
		pdate= (EditText) rootView.findViewById(R.id.pdate);
		pprice= (EditText) rootView.findViewById(R.id.pprice);

		post = (Button) rootView.findViewById(R.id.postgiveride);
		post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try
				{
					tcarnum = pcarnumlist.getSelectedItem().toString();
					tfrom = pfrom.getText().toString();
					tto = pto.getText().toString();
					tdate = pdate.getText().toString();
					tprice = pprice.getText().toString();
					
					System.out.println(pcarnumlist.getSelectedItemPosition());

					if (tcarnum.equals("") && tfrom.equals("") && tto.equals("")
							&& tdate.equals("")&& tprice.equals("")) {
						Toast.makeText(rootView.getContext().getApplicationContext(),
								"Please fill all details",
								Toast.LENGTH_LONG).show();
					} else {

						// Save new user data into Parse.com Data Storage
						ParseObject routes= new ParseObject("routes");
						routes.put("username",ParseUser.getCurrentUser().getUsername());
						routes.put("carNumber",tcarnum);
						routes.put("location_from",tfrom);
						routes.put("location_to",tto);
						routes.put("vacancy",true);
						Date date = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH).parse(tdate);
						routes.put("date",date);
						routes.put("price",tprice);
						
						new getLatLang(routes).execute(tfrom, tto);
					}
				}
				catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return rootView;
	}
	
	String HTML;
	
	public class getLatLang extends AsyncTask<String, Void, Void> {
		ParseObject routes;
	    public getLatLang(ParseObject routes) {
			this.routes = routes;
		}

		@Override
	    protected Void doInBackground(String... url) {
	        try {
	            Thread.sleep(4000);
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpContext localContext = new BasicHttpContext();
	            String fromLatLang = "";
	            String line = null;
	            double lat, lng; 
	            
	            HttpGet httpGetFrom = new HttpGet("http://rahulisola.com/geo.php?type=getLatlng&address="+URLEncoder.encode(url[0])); // URL!
	            HttpResponse response = httpClient.execute(httpGetFrom,localContext);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	            while ((line = reader.readLine()) != null) {
	            	fromLatLang += line + "\n";
	            }
	            String[] fromLatLangs = fromLatLang.split(",");
	            lat = Double.parseDouble(fromLatLangs[0]);
	            lng = Double.parseDouble(fromLatLangs[1]);
	            
	            ParseGeoPoint from = new ParseGeoPoint(lat, lng);
	            
	            String toLatLang = "";
	            HttpGet httpGetTo = new HttpGet("http://rahulisola.com/geo.php?type=getLatlng&address="+URLEncoder.encode(url[1])); // URL!
	            response = httpClient.execute(httpGetTo,localContext);
	            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	            while ((line = reader.readLine()) != null) {
	            	toLatLang += line + "\n";
	            }
	            String[] toLatLangs = toLatLang.split(",");
	            lat = Double.parseDouble(toLatLangs[0]);
	            lng = Double.parseDouble(toLatLangs[1]);
	            
	            ParseGeoPoint to = new ParseGeoPoint(lat, lng);

	            routes.put("geo_from", from);
	            routes.put("geo_to", to);

				routes.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							Toast.makeText(rootView.getContext().getApplicationContext(),"Posted!!",Toast.LENGTH_LONG).show();

							Fragment f = new HomeFragment();
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
						} else {
							Toast.makeText(rootView.getContext().getApplicationContext(),"Some Error Dude\n"+e.toString(), Toast.LENGTH_LONG).show();
						}
					}
				});
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        // TODO Auto-generated method stub
	        return null;
	    }
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String sel = pcarnumlist.getSelectedItem().toString();
		if(sel.equals("Add New Car"))
		{
			Fragment f = new AddCarActivity();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}
