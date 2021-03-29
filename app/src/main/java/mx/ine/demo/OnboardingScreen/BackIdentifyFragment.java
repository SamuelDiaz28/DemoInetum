package mx.ine.demo.OnboardingScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ine.demo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackIdentifyFragment extends Fragment {



    public BackIdentifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_back_identify, container, false);
    }
}