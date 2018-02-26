package fss.shopping.view.lists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fss.shopping.R;
import fss.shopping.view.HomeActivity;

/**
 * Created by Alex on 25.02.2018.
 */

public class ShoppingListActivity extends HomeActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpBottomNavigationBar();
    }

    @Override
    public int getActivityNumber() {
        return 0;
    }
}
