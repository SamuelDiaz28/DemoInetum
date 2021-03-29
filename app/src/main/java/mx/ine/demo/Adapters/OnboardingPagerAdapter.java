package mx.ine.demo.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import mx.ine.demo.OnboardingScreen.BackIdentifyFragment;
import mx.ine.demo.OnboardingScreen.CameraFragment;
import mx.ine.demo.OnboardingScreen.InfoCameraFragment;
import mx.ine.demo.OnboardingScreen.ObverseIdentifyFragment;

public class OnboardingPagerAdapter extends FragmentStateAdapter {

    public OnboardingPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ObverseIdentifyFragment();
            case 1:
                return new BackIdentifyFragment();
            case 2:
                return new InfoCameraFragment();
            default:
                return new CameraFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
