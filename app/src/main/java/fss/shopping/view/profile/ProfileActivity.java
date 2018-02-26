package fss.shopping.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;

import fss.shopping.view.HomeActivity;

/**
 * Created by Alex on 25.02.2018.
 */

public class ProfileActivity extends HomeActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getActivityNumber() {
        return 2;
    }
}
