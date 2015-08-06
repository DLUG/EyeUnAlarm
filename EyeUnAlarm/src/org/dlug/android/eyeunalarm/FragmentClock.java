package org.dlug.android.eyeunalarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentClock extends Fragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clock, container, false);
        
        return rootView;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	//first saving my state, so the bundle wont be empty. 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE"); 
    	super.onSaveInstanceState(outState); 
    }
}
