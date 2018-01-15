package com.kca.www.pastquestion;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.system.Os.remove;
import static com.kca.www.pastquestion.R.id.grade;
import static com.kca.www.pastquestion.R.id.textView;

public class GPCalcSettingsActivity extends AppCompatActivity {

    private boolean isSwitched;
    private boolean isCreditChanged;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSwitched = false;
        isCreditChanged = false;
        ViewGroup rootView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_gpcalc_setting, null, false);
        String[] grades = getResources().getStringArray(R.array.grades_array);
        String[] values = getResources().getStringArray(R.array.values_array);
        sharedPreferences = this.getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        for (int i = 0; i < grades.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.content_gpcalc_setting, null, false);
            TextView textView1 = view.findViewById(grade);
            textView1.setText(grades[i]);
            TextView textView2 = view.findViewById(R.id.value);
            Switch enabledSwitch = view.findViewById(R.id.enabled);
            if (textView1.getText().toString().equals("A")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch1",true));
                textView2.setText(sharedPreferences.getString("edit1",values[i]));
            }
            else if (textView1.getText().toString().equals("B")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch2",true));
                textView2.setText(sharedPreferences.getString("edit2",values[i]));
            }
            else if (textView1.getText().toString().equals("C")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch3",true));
                textView2.setText(sharedPreferences.getString("edit3",values[i]));
            }
            else if (textView1.getText().toString().equals("D")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch4",true));
                textView2.setText(sharedPreferences.getString("edit4",values[i]));
            }
            else if (textView1.getText().toString().equals("E")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch5",false));
                textView2.setText(sharedPreferences.getString("edit5",values[i]));
            }
            else if (textView1.getText().toString().equals("F")) {
                enabledSwitch.setChecked(sharedPreferences.getBoolean("switch6",true));
                textView2.setText(sharedPreferences.getString("edit6",values[i]));
            }

            enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!isSwitched) {
                        isSwitched = true;
                    }
                    ViewGroup viewGroup = (ViewGroup) compoundButton.getParent().getParent();
                    TextView view1 = viewGroup.findViewById(R.id.grade);
                    if (view1.getText().toString().equals("A")) {
                        sharedPreferences.edit().putBoolean("switch1",b).apply();
                    }
                    else if (view1.getText().toString().equals("B")) {
                        sharedPreferences.edit().putBoolean("switch2",b).apply();
                    }
                    else if (view1.getText().toString().equals("C")) {
                        sharedPreferences.edit().putBoolean("switch3",b).apply();
                    }
                    else if (view1.getText().toString().equals("D")) {
                        sharedPreferences.edit().putBoolean("switch4",b).apply();
                    }
                    else if (view1.getText().toString().equals("E")) {
                        sharedPreferences.edit().putBoolean("switch5",b).apply();
                    }
                    else if (view1.getText().toString().equals("F")) {
                        sharedPreferences.edit().putBoolean("switch6",b).apply();
                    }
                }
            });
            rootView.addView(view);
        }
        setContentView(rootView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("SwitchState",isSwitched);
        intent.putExtra("CreditChanged",isCreditChanged);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    public void edit (final View view) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View rootView = view;
        final String grade = "Grade";
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null, false);
        LinearLayout linearLayout = (LinearLayout) rootView.getParent().getParent().getParent();
        final EditText editText = dialogView.findViewById(R.id.dialog_view_editText);
        editText.setHint("Value");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        TextView textView = linearLayout.findViewById(R.id.grade);
        final String grade2 = textView.getText().toString();
        Button positiveButton = dialogView.findViewById(R.id.dialog_view_positiveButton);
        positiveButton.setText("Update");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().equals("")) {
                    TextView textView1 = ((LinearLayout) rootView.getParent().getParent()).findViewById(R.id.value);
                    textView1.setText(editText.getText().toString());
                    switch (grade2) {
                        case "A" :
                            sharedPreferences.edit().putString("edit1",editText.getText().toString()).apply();
                            break;
                        case "B" :
                            sharedPreferences.edit().putString("edit2",editText.getText().toString()).apply();
                            break;
                        case "C" :
                            sharedPreferences.edit().putString("edit3",editText.getText().toString()).apply();
                            break;
                        case "D" :
                            sharedPreferences.edit().putString("edit4",editText.getText().toString()).apply();
                            break;
                        case "E" :
                            sharedPreferences.edit().putString("edit5",editText.getText().toString()).apply();
                            break;
                        case "F" :
                            sharedPreferences.edit().putString("edit6",editText.getText().toString()).apply();
                            break;
                    }
                    if(!isCreditChanged) {
                        isCreditChanged = true;
                    }
                    dialog.dismiss();
                }
            }
        });
        Button negativeButton = dialogView.findViewById(R.id.dialog_view_negativeButton);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setTitle(grade + " \"" + grade2 + "\"" );
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.show();
    }

}
