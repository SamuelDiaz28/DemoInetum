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
public class ObverseIdentifyFragment extends Fragment {

    public ObverseIdentifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overse_identify, container, false);
    }
}