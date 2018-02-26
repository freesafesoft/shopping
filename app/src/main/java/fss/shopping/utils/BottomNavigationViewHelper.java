package fss.shopping.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import fss.shopping.R;
import fss.shopping.view.HomeActivity;
import fss.shopping.view.lists.ShoppingListActivity;
import fss.shopping.view.products.ListOfProductsActivity;
import fss.shopping.view.profile.ProfileActivity;

/**
 * Created by Alex on 23.02.2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setUpBottomnavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setUpBottomnavigationView: menu bar configuration");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final HomeActivity activity, BottomNavigationViewEx view) {

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.ic_lists:
                        if (activity instanceof ShoppingListActivity)
                            return false;
                        Intent intent1 = new Intent(activity, ShoppingListActivity.class);
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent1);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        activity.finish();
                        break;

                    case R.id.ic_new_list:
                        if (activity instanceof ListOfProductsActivity)
                            return false;
                        Intent intent2 = new Intent(activity, ListOfProductsActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent2);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        activity.finish();
                        break;

                    case R.id.ic_profile:
                        if (activity instanceof ProfileActivity)
                            return false;
                        Intent intent3 = new Intent(activity, ProfileActivity.class);
                        //intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent3);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        activity.finish();
                        break;
                }
                return false;
            }
        });
    }
}
