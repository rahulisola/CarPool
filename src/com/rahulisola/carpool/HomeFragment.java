package com.rahulisola.carpool;

import com.parse.ParseUser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.txtLabel);
        tv.setText("Hello " + ParseUser.getCurrentUser().getString("name"));
        return rootView;
    }
}
