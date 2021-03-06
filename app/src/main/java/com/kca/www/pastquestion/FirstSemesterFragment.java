package com.kca.www.pastquestion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.button;
import static android.R.attr.factor;
import static com.kca.www.pastquestion.R.id.container;
import static com.kca.www.pastquestion.R.id.spinner;

/**
 * Created by KCA on 12/21/2017.
 */

public class FirstSemesterFragment extends Fragment {

    private View rootView;
    private View gradeView;
    private ViewGroup coursesLayout;
    private LinearLayout gradeLayout;
    private static ArrayList<Spinner> spinnerArrayList = new ArrayList<>();
    private ArrayList<String> gradeSpinnerArray = new ArrayList<>();
    private boolean isSaved;
    private String[] temp_credits;
    private SharedPreferences sharedPreferences;
    private int[] credits = new int[8];
    private String[] courseCodes = new String[8];
    private String[] switchNames = new String[6];
    private boolean switchStates[] = new boolean[6];
    private TextView gpaTextView;
    public static TextView cgpaTextView;
    private static Button savedButton;
    private static Button editButton;
    private Calculator calculator;

    public static FirstSemesterFragment newInstance(int position) {
        FirstSemesterFragment firstSemesterFragment = new FirstSemesterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",position);
        firstSemesterFragment.setArguments(bundle);
        return firstSemesterFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp_credits = getResources().getStringArray(R.array.credits1);
        for (int i = 0; i < temp_credits.length; i++) {
            credits[i] = Integer.parseInt(temp_credits[i]);
        }
        courseCodes = getResources().getStringArray(R.array.courses1);
        switchNames = getResources().getStringArray(R.array.switches);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "createView");
        sharedPreferences = getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(),
                Context.MODE_PRIVATE);
        isSaved = sharedPreferences.getBoolean("isSaved1",false);
        rootView = inflater.inflate(R.layout.fragment_first_semester, container, false);
        spinnerArrayList.clear();
        calculator = Calculator.getInstance();
        calculator.setUp(getActivity(),0);
        coursesLayout = (LinearLayout)rootView.findViewById(R.id.courses_linear_layout);
        gpaTextView = rootView.findViewById(R.id.gpa_label);
        gpaTextView.setText("0.00");
        cgpaTextView = rootView.findViewById(R.id.cgpa_label);
        cgpaTextView.setText("0.00");
        savedButton = rootView.findViewById(R.id.save_button1);
        editButton = rootView.findViewById(R.id.edit_button1);
        initialiseGradeSpinnerArray();
        for (int i = 0; i < courseCodes.length; i++) {
            gradeView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view,coursesLayout,false);
            gradeLayout = gradeView.findViewById(R.id.grade_view);
            Button removeButton = gradeView.findViewById(R.id.remove);
            gradeLayout.removeView(removeButton);
            final Button courseButton = gradeLayout.findViewById(R.id.course_button);
            courseButton.setText(courseCodes[i]);
            final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
            creditSpinner.setSelection(credits[i]);
            creditSpinner.setEnabled(false);
            creditSpinner.setId(i);
            getSelectedGrades();
            Spinner gradeSpinner = gradeLayout.findViewById(R.id.grade_spinner);
            gradeSpinner.setId(i);
            ArrayAdapter<String> gradeSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, gradeSpinnerArray);
            gradeSpinner.setAdapter(gradeSpinnerAdapter);
            spinnerArrayList.add(gradeSpinner);
            if(isSaved) {
                gradeSpinner.setSelection(sharedPreferences.getInt(String.valueOf(gradeSpinner.getId())+String.valueOf(1), 0));
                gradeSpinner.setEnabled(false);
                savedButton.setEnabled(false);
                editButton.setEnabled(true);
            }
            else {
                editButton.setEnabled(false);
            }
            gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    LinearLayout linearLayout = (LinearLayout) adapterView.getParent();
                    Button button = linearLayout.findViewById(R.id.course_button);
                    String id = button.getText().toString();
                    String item = adapterView.getItemAtPosition(i).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 0);
                    calculator.calculateGPA(0);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(0)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    SecondSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            coursesLayout.addView(gradeLayout);
            gradeLayout.setTranslationY(coursesLayout.getHeight()+1000);
            gradeLayout.animate().translationYBy(-(coursesLayout.getHeight()+1000)).setDuration(
                    (long) ((coursesLayout.getHeight()+1000)*1.2)+ (i*100));
        }
        addExtraCourse();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d("TAG","Charles");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("key", 4);
        super.onSaveInstanceState(outState);
    }

    private void addExtraCourse(){
        gradeView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view, coursesLayout, false);
        coursesLayout = (LinearLayout)rootView.findViewById(R.id.courses_linear_layout);
        gradeLayout = gradeView.findViewById(R.id.grade_view);
        Button removeButton = gradeView.findViewById(R.id.remove);
        gradeLayout.removeView(removeButton);
        final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
        creditSpinner.setSelection(1);
        creditSpinner.setEnabled(false);
        View spinnerView = LayoutInflater.from(getContext()).inflate(R.layout.course1_spinner_view, coursesLayout, false);
        final Spinner courseSpinner = spinnerView.findViewById(R.id.spinner);
        LinearLayout courseLayout = gradeLayout.findViewById(R.id.course_layout);
        final Button courseButton = courseLayout.findViewById(R.id.course_button);
        courseLayout.removeView(courseButton);
        courseLayout.addView(spinnerView);
        getSelectedGrades();
        final Spinner gradeSpinner = gradeLayout.findViewById(R.id.grade_spinner);
        ArrayAdapter<String> gradeSpinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, gradeSpinnerArray);
        gradeSpinner.setAdapter(gradeSpinnerAdapter);
        if(isSaved) {
            gradeSpinner.setSelection(sharedPreferences.getInt(String.valueOf(gradeSpinner.getId())+String.valueOf(1), 0));
            gradeSpinner.setEnabled(false);
            courseSpinner.setSelection(sharedPreferences.getInt(String.valueOf(courseSpinner.getId())+String.valueOf(1), 0));
            courseSpinner.setEnabled(false);
        }
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!courseSpinner.getSelectedItem().toString().equals("Select")) {
                    String id = "extraSpinner";
                    String item = adapterView.getItemAtPosition(i).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 0);
                    calculator.calculateGPA(0);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(0)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    SecondSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = "extraSpinner";
                if (!courseSpinner.getSelectedItem().toString().equals("Select")) {
                    String item = gradeSpinner.getSelectedItem().toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 0);
                    calculator.calculateGPA(0);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(0)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    SecondSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
                else {
                    String item = gradeSpinner.getItemAtPosition(0).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 0);
                    calculator.calculateGPA(0);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(0)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    SecondSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });
        spinnerArrayList.add(gradeSpinner);
        spinnerArrayList.add(courseSpinner);
        coursesLayout.addView(gradeLayout);
        gradeLayout.setTranslationY(coursesLayout.getHeight()+1000);
        gradeLayout.animate().translationYBy(-(coursesLayout.getHeight()+1000)).setDuration(
                (long) ((coursesLayout.getHeight()+1000)*1.2)+(800));
    }

    public void getSelectedGrades() {
        for (int i = 0; i < switchStates.length; i++) {
            if (i != 4) {
                switchStates[i] = sharedPreferences.getBoolean(switchNames[i], true);
            }
            else {
                switchStates[i] = sharedPreferences.getBoolean(switchNames[i], false);
            }
        }
        gradeSpinnerArray.clear();
        gradeSpinnerArray.add("Select");
        for (int i = 0; i < switchStates.length; i++) {
            if(switchStates[i]) {
                switch (i) {
                    case 0 : {
                        gradeSpinnerArray.add("A");
                        break;
                    }
                    case 1 : {
                        gradeSpinnerArray.add("B");
                        break;
                    }
                    case 2 : {
                        gradeSpinnerArray.add("C");
                        break;
                    }
                    case 3 : {
                        gradeSpinnerArray.add("D");
                        break;
                    }
                    case 4 : {
                        gradeSpinnerArray.add("E");
                        break;
                    }
                    case 5 : {
                        gradeSpinnerArray.add("F");
                        break;
                    }
                }
            }
        }
    }

    public void initialiseGradeSpinnerArray(){
        String[] array = {"Select","A","B","C","D","E","F"};
        for (int i = 0; i < array.length; i++) {
            gradeSpinnerArray.add(array[i]);
        }
    }

    public static Button getSavedButton() {
        return savedButton;
    }

    public static Button getEditButton() {
        return editButton;
    }

    public static ArrayList<Spinner> getSpinnerArrayList() {
        return spinnerArrayList;
    }
}
