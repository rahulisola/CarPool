package com.rahulisola.carpool;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WhatsHotFragment extends Fragment {
	
	public WhatsHotFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
        
        TextView tv = (TextView) rootView.findViewById(R.id.txtLabel);
        tv.setText("Simple Car Pool Application developed by Rahul Isola (rahul@rahulisola.com) and Pratyush Gupta (pratyushgupta@pratyush.com)");
         
        return rootView;
    }
}
