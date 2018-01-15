package com.kca.www.pastquestion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by KCA on 12/21/2017.
 */

public class SecondSemesterFragment extends Fragment {

    View rootView;
    View gradeView;
    ViewGroup coursesLayout;
    LinearLayout gradeLayout;
    private static ArrayList<Spinner> spinnerArrayList = new ArrayList<>();
    ArrayList<String> gradeSpinnerArray = new ArrayList<>();
    String[] courseCodes = {"GST 102", "GST 108", "GST 110", "MTH 102", "PHY 102", "CHM 102", "ENG 102", "ENG 104"};
    String[] switchNames = {"switch1","switch2","switch3","switch4","switch5","switch6"};
    private boolean switchStates[] = new boolean[6];
    private boolean isSaved;
    int[] credits = {2, 2, 1, 4, 4, 4, 1, 1};
    SharedPreferences sharedPreferences;
    private TextView gpaTextView;
    public static TextView cgpaTextView;
    private static Button savedButton;
    private static Button editButton;
    private Calculator calculator;

    public static SecondSemesterFragment newInstance(int position) {
        SecondSemesterFragment secondSemesterFragment = new SecondSemesterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",position);
        //bundle.putString("title",title);
        secondSemesterFragment.setArguments(bundle);
        return secondSemesterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(),
                            Context.MODE_PRIVATE);
        isSaved = sharedPreferences.getBoolean("isSaved2",false);
        spinnerArrayList.clear();
        calculator = Calculator.getInstance();
        calculator.setUp(getActivity(),1);
        rootView = inflater.inflate(R.layout.fragment_second_semester, container, false);
        coursesLayout = rootView.findViewById(R.id.courses_linear_layout);
        gpaTextView = rootView.findViewById(R.id.gpa_label);
        gpaTextView.setText("0.00");
        cgpaTextView = rootView.findViewById(R.id.cgpa_label);
        cgpaTextView.setText("0.00");
        savedButton = rootView.findViewById(R.id.save_button2);
        editButton = rootView.findViewById(R.id.edit_button2);
        initialiseGradeSpinnerArray();
        for (int i = 0; i < courseCodes.length; i++) {
            gradeView = inflater.inflate(R.layout.grade_view,container,false);
            gradeLayout = gradeView.findViewById(R.id.grade_view);
            Button removeButton = gradeView.findViewById(R.id.remove);
            gradeLayout.removeView(removeButton);
            final Button courseButton = gradeLayout.findViewById(R.id.course_button);
            courseButton.setText(courseCodes[i]);
            final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
            creditSpinner.setSelection(credits[i]);
            creditSpinner.setEnabled(false);
            getSelectedGrades();
            Spinner gradeSpinner = gradeLayout.findViewById(R.id.grade_spinner);
            ArrayAdapter<String> gradeSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, gradeSpinnerArray);
            gradeSpinner.setAdapter(gradeSpinnerAdapter);
            spinnerArrayList.add(gradeSpinner);
            if(isSaved) {
                gradeSpinner.setSelection(sharedPreferences.getInt(String.valueOf(i)+String.valueOf(2), 0));
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
                    calculator.setGrades(item, credit, id, 1);
                    calculator.calculateGPA(1);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f", calculator.getGpa(1)));
                    cgpaTextView.setText(String.format("%.2f", calculator.getCgpa()));
                    FirstSemesterFragment.cgpaTextView.setText(String.format("%.2f", calculator.getCgpa()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            coursesLayout.addView(gradeLayout);
        }
        addExtraCourse();
        return rootView;
    }

    private void addExtraCourse(){
        gradeView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view, coursesLayout,false);
        coursesLayout = rootView.findViewById(R.id.courses_linear_layout);
        gradeLayout = gradeView.findViewById(R.id.grade_view);
        Button removeButton = gradeView.findViewById(R.id.remove);
        gradeLayout.removeView(removeButton);
        final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
        creditSpinner.setSelection(1);
        creditSpinner.setEnabled(false);
        View spinnerView = LayoutInflater.from(getContext()).inflate(R.layout.course2_spinner_view, coursesLayout, false);
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
        spinnerArrayList.add(gradeSpinner);
        spinnerArrayList.add(courseSpinner);
        if(isSaved) {
            gradeSpinner.setSelection(sharedPreferences.getInt(String.valueOf(8)+String.valueOf(2), 0));
            gradeSpinner.setEnabled(false);
            courseSpinner.setSelection(sharedPreferences.getInt(String.valueOf(9)+String.valueOf(2), 0));
            gradeSpinner.setEnabled(false);
        }
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!courseSpinner.getSelectedItem().toString().equals("Select")) {
                    String id = "extraSpinner2";
                    String item = adapterView.getItemAtPosition(i).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 1);
                    calculator.calculateGPA(1);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(1)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    FirstSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = "extraSpinner2";
                if (!courseSpinner.getSelectedItem().toString().equals("Select")) {
                    String item = gradeSpinner.getSelectedItem().toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 1);
                    calculator.calculateGPA(1);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(1)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    FirstSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
                else {
                    String item = gradeSpinner.getItemAtPosition(0).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    calculator.setGrades(item, credit, id, 1);
                    calculator.calculateGPA(1);
                    calculator.calculateCgpa();
                    gpaTextView.setText(String.format("%.2f",calculator.getGpa(1)));
                    cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                    FirstSemesterFragment.cgpaTextView.setText(String.format("%.2f",calculator.getCgpa()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        coursesLayout.addView(gradeLayout);
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
