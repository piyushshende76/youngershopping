package util;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import youngershopping.tcc.R;

@SuppressLint("Registered")
public class ReplaceFragment extends AppCompatActivity {
    Fragment fragment;
    FragmentActivity fragmentActivity;


    public ReplaceFragment() {
    }


    public ReplaceFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ReplaceFragment(Fragment fragment, FragmentActivity fragmentActivity) {
        this.fragment = fragment;
        this.fragmentActivity = fragmentActivity;
    }

    public static void replaceFragment(Fragment fragment, FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentPanel, fragment).addToBackStack(null).commit();
    }


}
