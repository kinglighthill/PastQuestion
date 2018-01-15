package com.kca.www.pastquestion;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class OthersFragment extends Fragment {

    View rootView;
    View gradeView;
    ViewGroup coursesLayout;
    LinearLayout gradeLayout;
    ArrayList<String> gradeSpinnerArray = new ArrayList<>();
    String[] switchNames = {"switch1","switch2","switch3","switch4","switch5","switch6"};
    private boolean switchStates[] = new boolean[6] ;
    private int _id = 0;
    SharedPreferences sharedPreferences;
    public static TextView gpaTextView;
    private Calculator calculator;
    private Calculator.InnerCalculator innerCalculator;

    public static OthersFragment newInstance(int position) {
        OthersFragment othersFragment = new OthersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",position);
        //bundle.putString("title",title);
        othersFragment.setArguments(bundle);
        return othersFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calculator = Calculator.getInstance();
        innerCalculator = calculator.getInnerCalculator(getActivity());
        rootView = inflater.inflate(R.layout.fragment_others, container, false);
        gradeView = inflater.inflate(R.layout.grade_view,container,false);
        coursesLayout = rootView.findViewById(R.id.courses_linear_layout);
        gradeLayout = gradeView.findViewById(R.id.grade_view);
        sharedPreferences = getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(),
                            Context.MODE_PRIVATE);
        gpaTextView = rootView.findViewById(R.id.gpa_label);
        gpaTextView.setText("0.00");
        initialiseGradeSpinnerArray();
        final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
        getSelectedGrades();
        final Spinner gradeSpinner = gradeLayout.findViewById(R.id.grade_spinner);
        final Button courseButton = gradeLayout.findViewById(R.id.course_button);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(20 * Resources.getSystem().getDisplayMetrics().density),
                                    (int)(20 * Resources.getSystem().getDisplayMetrics().density), 0, 0);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null, false);
                final EditText editText = dialogView.findViewById(R.id.dialog_view_editText);
                editText.setHint("Enter Course Code");
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setLayoutParams(params);
                Button positiveButton = dialogView.findViewById(R.id.dialog_view_positiveButton);
                positiveButton.setText("Submit");
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editText.getText().toString().equals("")) {
                            courseButton.setText(editText.getText().toString());
                            dialog.dismiss();
                            if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                                    || gradeSpinner.getSelectedItem().toString().equals("Select"))) {
                                String id = courseButton.getText().toString();
                                String item = gradeSpinner.getSelectedItem().toString();
                                Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                                innerCalculator.setGrades(item, credit, id);
                                innerCalculator.calculateGPA();
                                gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                            }
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
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        ArrayAdapter<String> gradeSpinnerAdapter = new ArrayAdapter<>(getContext(),
                                                    R.layout.spinner_item, gradeSpinnerArray);
        gradeSpinner.setAdapter(gradeSpinnerAdapter);
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout linearLayout = (LinearLayout) adapterView.getParent();
                Button button = linearLayout.findViewById(R.id.course_button);
                if(!(courseButton.getText().toString().equals("Select")
                        || creditSpinner.getSelectedItem().toString().equals("Select"))) {
                    String id = button.getText().toString();
                    String item = adapterView.getItemAtPosition(i).toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    innerCalculator.setGrades(item, credit, id);
                    innerCalculator.calculateGPA();
                    gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                }
                else {
                    if (gradeSpinner.getSelectedItem().toString().equals("Select")) {
                        String id = button.getText().toString();
                        String item = gradeSpinner.getSelectedItem().toString();
                        innerCalculator.setGrades(item, 1, id);
                        innerCalculator.calculateGPA();
                        gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        creditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                        || gradeSpinner.getSelectedItem().toString().equals("Select")
                        || courseButton.getText().toString().equals("Select"))) {
                    String id =courseButton.getText().toString();
                    String item = gradeSpinner.getSelectedItem().toString();
                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                    innerCalculator.setGrades(item, credit, id);
                    innerCalculator.calculateGPA();
                    gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                }
                else {
                    if (creditSpinner.getSelectedItem().toString().equals("Select")) {
                        String id = courseButton.getText().toString();
                        innerCalculator.setGrades("Select", 1, id);
                        innerCalculator.calculateGPA();
                        gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        Button removeButton = gradeLayout.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                        || gradeSpinner.getSelectedItem().toString().equals("Select")
                        || courseButton.getText().toString().equals("Select"))) {
                    String id = courseButton.getText().toString();
                    innerCalculator.setGrades("Select", 1, id);
                    innerCalculator.calculateGPA();
                    gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                }
                LinearLayout coursesLayout = (LinearLayout) view.getParent().getParent();
                LinearLayout gradeLayout = (LinearLayout) view.getParent();
                coursesLayout.removeView(gradeLayout);
            }
        });
        coursesLayout.addView(gradeLayout);
        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(fabListener);
        return rootView;
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            gradeView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view,coursesLayout,false);
            gradeLayout = gradeView.findViewById(R.id.grade_view);
            final Spinner creditSpinner = gradeLayout.findViewById(R.id.credits_spinner);
            getSelectedGrades();
            final Spinner gradeSpinner = gradeLayout.findViewById(R.id.grade_spinner);
            final Button courseButton = gradeLayout.findViewById(R.id.course_button);
            courseButton.setId(_id);
            courseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins((int)(20 * Resources.getSystem().getDisplayMetrics().density),
                            (int)(20 * Resources.getSystem().getDisplayMetrics().density), 0, 0);
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null, false);
                    final EditText editText = dialogView.findViewById(R.id.dialog_view_editText);
                    editText.setHint("Enter Course Code");
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setLayoutParams(params);
                    Button positiveButton = dialogView.findViewById(R.id.dialog_view_positiveButton);
                    positiveButton.setText("Submit");
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!editText.getText().toString().equals("")) {
                                courseButton.setText(editText.getText().toString());
                                dialog.dismiss();
                                if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                                        || gradeSpinner.getSelectedItem().toString().equals("Select"))) {
                                    String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                                    String item = gradeSpinner.getSelectedItem().toString();
                                    Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                                    innerCalculator.setGrades(item, credit, id);
                                    innerCalculator.calculateGPA();
                                    gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                                }
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
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                                                    R.layout.spinner_item, gradeSpinnerArray);
            gradeSpinner.setAdapter(spinnerAdapter);
            gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!(courseButton.getText().toString().equals("Select")
                            || creditSpinner.getSelectedItem().toString().equals("Select"))) {
                        String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                        String item = adapterView.getItemAtPosition(i).toString();
                        Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                        innerCalculator.setGrades(item, credit, id);
                        innerCalculator.calculateGPA();
                        gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                    }
                    else {
                        if (gradeSpinner.getSelectedItem().toString().equals("Select")) {
                            String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                            String item = gradeSpinner.getSelectedItem().toString();
                            innerCalculator.setGrades(item, 1, id);
                            innerCalculator.calculateGPA();
                            gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            creditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                            || gradeSpinner.getSelectedItem().toString().equals("Select")
                            || courseButton.getText().toString().equals("Select"))) {
                        String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                        String item = gradeSpinner.getSelectedItem().toString();
                        Double credit = Double.parseDouble(creditSpinner.getSelectedItem().toString());
                        innerCalculator.setGrades(item, credit, id);
                        innerCalculator.calculateGPA();
                        gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                    }
                    else {
                        if (creditSpinner.getSelectedItem().toString().equals("Select")) {
                            String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                            innerCalculator.setGrades("Select", 1, id);
                            innerCalculator.calculateGPA();
                            gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            Button removeButton = gradeLayout.findViewById(R.id.remove);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(creditSpinner.getSelectedItem().toString().equals("Select")
                            || gradeSpinner.getSelectedItem().toString().equals("Select")
                            || courseButton.getText().toString().equals("Select"))) {
                        String id = courseButton.getText().toString() + String.valueOf(courseButton.getId());
                        innerCalculator.setGrades("Select", 1, id);
                        innerCalculator.calculateGPA();
                        gpaTextView.setText(String.format("%.2f",innerCalculator.getGpa()));
                    }
                    LinearLayout coursesLayout = (LinearLayout) view.getParent().getParent();
                    LinearLayout gradeLayout = (LinearLayout) view.getParent();
                    coursesLayout.removeView(gradeLayout);
                }
            });
            coursesLayout.addView(gradeLayout);
            _id++;
        }
    };

    /*public void animate(ViewGroup layout, boolean animateOut) {
        Animator animator;
        Animation slide_in;
        Animation slide_out;
        if(animateOut) {
            slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.sliding_out);
            layout.startAnimation(slide_out);
        }
        else {
            slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.sliding_in);
            layout.startAnimation(slide_in);
        }
    }*/

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

}
