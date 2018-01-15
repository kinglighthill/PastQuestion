package com.kca.www.pastquestion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CBTGame extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbtgame);
        context = this;
        String[] courses = {"MTH 101_ELEMENTARY MATHEMATICS", "PHY 101_GENERAL PHYSICS I", "CHM 101_GENERAL CHEMISTRY I",
                            "BIO 101_BIOLOGY FOR PHYSICAL SCIENCES", "ENG 101_WORKSHOP PRACTICE I",
                            "ENG 103_ENGINEERING DRAWING I", "GST 101_USE OF ENGLISH I", "GST 103_HUMANITIES",
                            "IGB 101_INTRODUTION TO IGBO I", "FRN 101_USE OF FRENCH"};
        ListView listView = (ListView) findViewById(R.id.cbt_game_list_view);
        ListAdapter listAdapter = new CbtGameAdapter(this, courses);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(context, GameActivity.class);
            startActivity(intent);
        }
    };

}
