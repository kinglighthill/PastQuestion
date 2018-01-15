package com.kca.www.pastquestion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.R.attr.resource;
import static android.content.res.Resources.getSystem;

/**
 * Created by KCA on 12/21/2017.
 */

public class CbtGameAdapter extends ArrayAdapter<String> {

    Context context;

    public CbtGameAdapter(Context context, String[] courses) {
        super(context, R.layout.cbt_game_list_item, courses);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.cbt_game_list_item, parent, false);
        }
        int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels - 200;
        LinearLayout layout = view.findViewById(R.id.course_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = displayHeight / getCount();
        layout.setLayoutParams(params);
        String[] items = getItem(position).split("_");
        TextView courseTitle = view.findViewById(R.id.course_title);
        TextView courseCode = view.findViewById(R.id.course_code);
        courseCode.setText(items[0]);
        courseCode.setGravity(Gravity.CENTER_VERTICAL);
        courseTitle.setText(items[1]);
        courseTitle.setGravity(Gravity.CENTER_VERTICAL);

        return view;
    }
}
