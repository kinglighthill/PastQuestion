package com.kca.www.pastquestion;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "FlagQuiz Activity";
    private static final int NUMBER_OF_QUESTIONS = 10;
    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Set<String> regionsSet;
    private String correctAnswer;
    private String answer;
    private static int totalGuesses;
    private int correctAnswers;
    private int guessRows;
    private SecureRandom random;
    private Handler handler;
    private Animation shakeAnimation;
    private LinearLayout quizLinearLayout;
    private TextView questionNumberTextView;
    private WebView questionWebView;
    private LinearLayout[] guessLinearLayouts;
    private TextView answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        quizLinearLayout = (LinearLayout) findViewById(R.id.quizLinearLayout);
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        questionWebView = (WebView) findViewById(R.id.questionWebView);
        guessLinearLayouts = new LinearLayout[3];
        guessLinearLayouts[0] = (LinearLayout) findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout) findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = (LinearLayout) findViewById(R.id.row3LinearLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }
        questionNumberTextView.setText(getString(R.string.question, 1, NUMBER_OF_QUESTIONS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
        return true;
    }

    public void updateGuessRows() {
    }

    public void resetQuiz() {
        /*AssetManager assets = getActivity().getAssets();
        fileNameList.clear();
        try {
            for (String region : regionsSet) {
                String[] paths = assets.list(region);
                for (String path : paths)
                    fileNameList.add(path.replace(".png", ""));
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error loading image file names", exception);
        }
        correctAnswers = 0;
        totalGuesses = 0;
        quizCountriesList.clear();
        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();
        while (flagCounter <= FLAGS_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfFlags);
            String filename = fileNameList.get(randomIndex);
            if (!quizCountriesList.contains(filename)) {
                quizCountriesList.add(filename);
                ++flagCounter;
            }
        }
        loadNextFlag();*/
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = (Button) v;
            String guess = guessButton.getText().toString();
        }
    };
}
