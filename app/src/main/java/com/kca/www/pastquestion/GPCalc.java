package com.kca.www.pastquestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import static com.kca.www.pastquestion.R.id.container;

public class GPCalc extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SharedPreferences sharedPreferences;
    private ViewPager mViewPager;
    private Context context;
    private static int REQUEST_CODE = 1;
    private int selectedPage = 0;
    private boolean isSaved1;
    private boolean isSaved2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpcalc);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        isSaved1 = sharedPreferences.getBoolean("isSaved1", false);
        isSaved2 = sharedPreferences.getBoolean("isSaved2", false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Log.d("TAG1","create");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean switchState = data.getBooleanExtra("SwitchState",false);
        boolean creditChanged = data.getBooleanExtra("CreditChanged",false);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && (switchState || creditChanged)) {
            finish();
            startActivity(getIntent());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,"GPA settings updated",Toast.LENGTH_LONG).show();
                }
            },1000);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void save1(View view) {
        if (!isSaved1) {
            ArrayList<Spinner> spinnerArrayList = FirstSemesterFragment.getSpinnerArrayList();
            for (int i = 0; i < spinnerArrayList.size(); i++) {
                spinnerArrayList.get(i).setEnabled(false);
                sharedPreferences.edit().putInt(String.valueOf(spinnerArrayList.get(i).getId())+String.valueOf(1),
                        spinnerArrayList.get(i).getSelectedItemPosition()).apply();
            }
        }
        isSaved1 = true;
        sharedPreferences.edit().putBoolean("isSaved1", isSaved1).apply();
        FirstSemesterFragment.getSavedButton().setEnabled(false);
        FirstSemesterFragment.getEditButton().setEnabled(true);
        Toast.makeText(this, "Results Saved", Toast.LENGTH_SHORT).show();

    }

    public  void save2(View view) {
        if (!isSaved2) {
            ArrayList<Spinner> spinnerArrayList = SecondSemesterFragment.getSpinnerArrayList();
            for (int i = 0; i < spinnerArrayList.size(); i++) {
                spinnerArrayList.get(i).setEnabled(false);
                if (i == 1) {
                    sharedPreferences.edit().putInt(String.valueOf("credit_spinner")+String.valueOf(2),
                            spinnerArrayList.get(i).getSelectedItemPosition()).apply();
                }
                else if (i == 8) {
                    sharedPreferences.edit().putInt("grade_spinner1",
                            spinnerArrayList.get(i).getSelectedItemPosition()).apply();
                }
                else if (i == 10) {
                    sharedPreferences.edit().putInt("grade_spinner2",
                            spinnerArrayList.get(i).getSelectedItemPosition()).apply();
                }
                else {
                    sharedPreferences.edit().putInt(String.valueOf(spinnerArrayList.get(i).getId())+String.valueOf(2),
                            spinnerArrayList.get(i).getSelectedItemPosition()).apply();
                }
            }
        }
        isSaved2 = true;
        sharedPreferences.edit().putBoolean("isSaved2", isSaved2).apply();
        SecondSemesterFragment.getSavedButton().setEnabled(false);
        SecondSemesterFragment.getEditButton().setEnabled(true);
        Toast.makeText(this, "Results Saved", Toast.LENGTH_SHORT).show();
    }

    public void edit1(View view) {
        if (isSaved1) {
            ArrayList<Spinner> spinnerArrayList = FirstSemesterFragment.getSpinnerArrayList();
            for (Spinner spinner : spinnerArrayList) {
                spinner.setEnabled(true);
            }
        }
        isSaved1 = false;
        sharedPreferences.edit().putBoolean("isSaved1", isSaved1).apply();
        FirstSemesterFragment.getEditButton().setEnabled(false);
        FirstSemesterFragment.getSavedButton().setEnabled(true);
    }

    public void edit2(View view) {
        if (isSaved2) {
            ArrayList<Spinner> spinnerArrayList = SecondSemesterFragment.getSpinnerArrayList();
            for (Spinner spinner : spinnerArrayList) {
                spinner.setEnabled(true);
            }
            isSaved2 = false;
            sharedPreferences.edit().putBoolean("isSaved2", isSaved2).apply();
            SecondSemesterFragment.getEditButton().setEnabled(false);
            SecondSemesterFragment.getSavedButton().setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpcalc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, GPCalcSettingsActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
        return true;
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            selectedPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private class SectionsPagerAdapter extends FragmentPagerAdapter

    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return FirstSemesterFragment.newInstance(position);
                case 1:
                    return SecondSemesterFragment.newInstance(position);
                case 2: {
                    return OthersFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1ST SEMESTER";
                case 1:
                    return "2ND SEMESTER";
                case 2:
                    return "OTHERS";
            }
            return null;
        }
    }
}
