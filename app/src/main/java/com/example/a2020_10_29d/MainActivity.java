package com.example.a2020_10_29d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class MyPagerAdapter extends FragmentPagerAdapter{
            ArrayList<Fragment> items =new ArrayList<Fragment>();

            public MyPagerAdapter(FragmentManager fm){
                super(fm);
            }
            public void addItem(Fragment item){
                items.add(item);
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return items.get(position);
            }

            @Override
            public int getCount() {
                return items.size();
            }
        }
        pager = findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        First_Fragment firstFragment = new First_Fragment();
        adapter.addItem(firstFragment);

        Second_Fragment secondFragment = new Second_Fragment();
        adapter.addItem(secondFragment);


        Third_Fragment thirdFragment = new Third_Fragment();
        adapter.addItem(thirdFragment);

        pager.setAdapter(adapter);
        pager.setCurrentItem(1,false);
    }


}