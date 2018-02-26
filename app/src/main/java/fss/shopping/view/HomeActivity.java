package fss.shopping.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import fss.shopping.R;
import fss.shopping.utils.BottomNavigationViewHelper;

/**
 * Created by Alex on 23.02.2018.
 */

public abstract class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreate " + this.getClass().getName());
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            setUpBottomNavigationBar();
        } catch(Exception e) {
            Log.e(TAG, "onCreate: Exception occured:" + e.getMessage(), e);
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart " + this.getClass().getName());
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop " + this.getClass().getName());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy " + this.getClass().getName());
        super.onDestroy();
    }

    public abstract int getActivityNumber();

    /**
     * BottomNavigationView setup
     */
    public void setUpBottomNavigationBar() {
        Log.d(TAG, "setUpBottomNavigationBar " + this.getClass().getName());
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bot_nav_bar);
        BottomNavigationViewHelper.setUpBottomnavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(getActivityNumber());
        menuItem.setChecked(true);
    }
}
