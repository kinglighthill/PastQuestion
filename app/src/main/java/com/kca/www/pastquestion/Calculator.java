package com.kca.www.pastquestion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.R.attr.id;
import static com.kca.www.pastquestion.R.id.grade;

/**
 * Created by KCA on 12/25/2017.
 */

public class Calculator {

    private SharedPreferences sharedPreferences;
    private double cgpa = 0.00;
    private static Calculator calculator = new Calculator();
    private InnerCalculator innerCalculator;
    private InnerCalculator firstSemester;
    private InnerCalculator secondSemester;
    private double total_grade_units;
    private double total_units;

    private Calculator() {}

    public static Calculator getInstance() {
        return calculator;
    }

    public InnerCalculator getInnerCalculator(Activity activity) {
        innerCalculator = new InnerCalculator(activity);
        return innerCalculator;
    }

    public void setUp(Activity activity, int position) {
        switch (position) {
            case 0:
                firstSemester = new InnerCalculator(activity);
                break;
            case 1:
                secondSemester = new InnerCalculator(activity);
                break;
            default:
                break;
        }
    }

    public void setGrades(String grade, double credit, String id, int position) {
        switch (position) {
            case 0:
                firstSemester.setGrades(grade, credit, id);
                break;
            case 1:
                secondSemester.setGrades(grade, credit, id);
                break;
            default:
                break;
        }
    }

    public void calculateGPA(int position) {
        switch (position) {
            case 0:
                firstSemester.calculateGPA();
                break;
            case 1:
                secondSemester.calculateGPA();
                break;
            default:
                break;
        }
    }

    public double getGpa(int position){
        switch (position) {
            case 0:
                return firstSemester.getGpa();
            case 1:
                return secondSemester.getGpa();
            default:
                return 0.00;
        }
    }

    public void calculateCgpa() {
        total_grade_units = firstSemester.getTotalGradeUnit() + secondSemester.getTotalGradeUnit();
        total_units = firstSemester.getTotalUnits() + secondSemester.getTotalUnits();
        if (total_grade_units != 0 && total_units != 0) {
            cgpa = (total_grade_units) / (total_units);
        }
        else {
            cgpa = 0.00;
        }
    }

    public double getCgpa() {
        return cgpa;
    }

    public class InnerCalculator {
        private HashMap<String , Double> gradesValueArray = new HashMap<>();
        private HashMap<String, Double> gradeUnits = new HashMap();
        private HashMap<String, Double> credits = new HashMap<>();
        private double totalGradeUnit = 0;
        private double totalUnits = 0;
        private double gpa = 0.00;

        public InnerCalculator(Activity activity) {
            sharedPreferences = activity.getSharedPreferences(activity.getApplication().getPackageName(), Context.MODE_PRIVATE);
            gradesValueArray.put("A", Double.parseDouble(sharedPreferences.getString("edit1","5.0")));
            gradesValueArray.put("B", Double.parseDouble(sharedPreferences.getString("edit2","4.0")));
            gradesValueArray.put("C", Double.parseDouble(sharedPreferences.getString("edit3","3.0")));
            gradesValueArray.put("D", Double.parseDouble(sharedPreferences.getString("edit4","2.0")));
            gradesValueArray.put("E", Double.parseDouble(sharedPreferences.getString("edit5","1.0")));
            gradesValueArray.put("F", Double.parseDouble(sharedPreferences.getString("edit6","0.0")));
        }

        public void setGrades(String grade, double credit, String id) {
            switch (grade) {
                case "A" :
                    gradeUnits.put(id, gradesValueArray.get("A"));
                    credits.put(id, credit);
                    break;
                case "B" :
                    gradeUnits.put(id, gradesValueArray.get("B"));
                    credits.put(id, credit);
                    break;
                case "C" :
                    gradeUnits.put(id, gradesValueArray.get("C"));
                    credits.put(id, credit);
                    break;
                case "D" :
                    gradeUnits.put(id, gradesValueArray.get("D"));
                    credits.put(id, credit);
                    break;
                case "E" :
                    gradeUnits.put(id, gradesValueArray.get("E"));
                    credits.put(id, credit);
                    break;
                case "F" :
                    gradeUnits.put(id,gradesValueArray.get("F"));
                    credits.put(id, credit);
                    break;
                default:
                    gradeUnits.remove(id);
                    credits.remove(id);
                    break;
            }
        }

        public void calculateGPA() {
            totalGradeUnit = 0;
            totalUnits = 0;
            ArrayList<Double> gradeUnit = new ArrayList<>();
            for (Double grade_unit : gradeUnits.values()) {
                gradeUnit.add(grade_unit);
            }
            int i = 0;
            for (Double credit : credits.values()) {
                totalGradeUnit += gradeUnit.get(i) * credit;
                totalUnits += credit;
                i++;
            }
            if (totalGradeUnit != 0 && totalUnits != 0) {
                gpa = totalGradeUnit / totalUnits ;
            }
            else {
                gpa = 0.00;
            }
        }

        public double getTotalUnits() {
            return totalUnits;
        }

        public double getTotalGradeUnit() {
            return totalGradeUnit;
        }

        public double getGpa() {
            return gpa;
        }
    }
}
