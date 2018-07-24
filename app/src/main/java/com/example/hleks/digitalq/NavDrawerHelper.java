package com.example.hleks.digitalq;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

;

/**
 * Created by Hleks on 2018/05/13.
 */

public class NavDrawerHelper extends ContextWrapper {
    public NavDrawerHelper(Context base) {
        super(base);
    }

    public void initialize(final DrawerLayout drawer, NavigationView navView, Toolbar toolbar, final boolean isFinish){
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                return true;
            }
        });

        ActionBarDrawerToggle trigger = new ActionBarDrawerToggle(((AppCompatActivity) getBaseContext()), drawer, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (this.onOptionsItemSelected(item))
                    return true;
                return super.onOptionsItemSelected(item);
            }
        };

        drawer.addDrawerListener(trigger);
        trigger.syncState();
    }

}
