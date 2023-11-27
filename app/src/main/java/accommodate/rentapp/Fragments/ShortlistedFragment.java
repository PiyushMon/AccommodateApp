package accommodate.rentapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import accommodate.rentapp.R;


public class ShortlistedFragment extends Fragment {

    public ShortlistedFragment() {
    }

    public static ShortlistedFragment newInstance(String param1, String param2) {
        ShortlistedFragment fragment = new ShortlistedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shortlisted, container, false);
        return view;
    }
}