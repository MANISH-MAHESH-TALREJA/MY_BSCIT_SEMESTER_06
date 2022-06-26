package net.manish.mybscitsem06;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import yanzhikai.textpath.PathView;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.PenPainter;

public class QuizQuestions extends AppCompatActivity
{
    List<QuizModel> quizQuestions;
    public int score=0;
    int currentQuestionNumber =1;
    String subjectName;
    QuizModel currentQuestion;
    int answeredQuestions = 0;
    TextView textViewQuestion;
    RadioGroup choicesGroup;
    RadioButton option01, option02, option03, option04;
    FloatingActionButton nextButton;
    Button submitButton;
    final Random random = new Random();
    final ArrayList<Integer> list = new ArrayList<>();
    TextView textTime;
    public final ArrayList<String> attemptedQuestions = new ArrayList<>();
    public final ArrayList<String> selectedAnswer = new ArrayList<>();
    public final ArrayList<String> actualAnswer = new ArrayList<>();
    int number;
    int mySize, properSize;
    ProgressBar progressBar;
    int progress = 1;
    TextView questionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestions.this);
            Typeface face = ResourcesCompat.getFont(QuizQuestions.this,R.font.syndor);
            builder.setMessage("YOU ARE ABOUT TO EXIT THE QUIZ. CLICKING ON OK WILL RESET YOUR ANSWERS.\n\nARE YOU SURE TO EXIT ??")
                    .setCancelable(false)
                    .setIcon(R.drawable.emoji)
                    .setTitle(new Help().typeface(face, "EXIT QUIZ ???"))
                    .setPositiveButton( "OK, EXIT", (dialog, id) ->
                    {
                        finish();
                        Animatoo.animateDiagonal(this);
                    }).setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        submitButton = findViewById(R.id.nextQuestion);
        submitButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestions.this);
            Typeface face = ResourcesCompat.getFont(QuizQuestions.this,R.font.syndor);
            builder.setMessage("YOU ARE ABOUT TO SUBMIT THE QUIZ. CLICKING ON YES WILL SUBMIT ALL YOUR ANSWERS.\n\nARE YOU SURE TO SUBMIT ??")
                    .setCancelable(false)
                    .setIcon(R.drawable.quiz)
                    .setTitle(new Help().typeface(face, "EXIT QUIZ ???"))
                    .setPositiveButton( "OK, SUBMIT", (dialog, id) -> showResult()).setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        Intent intent = getIntent();
        subjectName = intent.getStringExtra("NAME");
        SyncTextPathView welcome = findViewById(R.id.subjectName);
        welcome.setPathPainter(new PenPainter());
        welcome.setFillColor(true);
        welcome.setText(subjectName);
        welcome.startAnimation(0,1, PathView.REVERSE,-1);
        questionNumber = findViewById(R.id.questionNumber);
        number=0;
        textTime = findViewById(R.id.textViewTime);

        try
        {
            quizQuestions = getQuizQuestions(intent.getStringExtra("DATA"));
            mySize = quizQuestions.size();
            properSize = mySize/3;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        @SuppressWarnings("IntegerMultiplicationImplicitCastToLong") final CounterClass timer = new CounterClass((properSize*60)*1000, 1000);
        timer.start();
        for(int i=0;i<properSize+1;i++)
        {
            while(true)
            {
                int next = random.nextInt(mySize);
                if(!list.contains(next))
                {
                    list.add(next);
                    break;
                }
            }
        }
        currentQuestion = quizQuestions.get(list.get(0));
        textViewQuestion = findViewById(R.id.textView1);
        option01 = findViewById(R.id.radio0);
        option02 = findViewById(R.id.radio1);
        option03= findViewById(R.id.radio2);
        option04 = findViewById(R.id.radio3);
        nextButton = findViewById(R.id.floatingActionButton);
        setQuestionView();
        choicesGroup = findViewById(R.id.radioGroup);
        nextButton.setEnabled(false);
        choicesGroup.setOnCheckedChangeListener((radioGroup, i) ->
        {
            if(i== R.id.radio0 || i == R.id.radio1 || i==R.id.radio2 || i == R.id.radio3)
                nextButton.setEnabled(true);
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(properSize);
        progressBar.setProgress(1);
        nextButton.setOnClickListener(v ->
        {
            progress = progress+1;
            answeredQuestions++;
            progressBar.setProgress(progress);
            RadioButton answer = findViewById(choicesGroup.getCheckedRadioButtonId());
            if (currentQuestion.getAnswer().contentEquals(answer.getText()))
            {
                score++;
            }
            else
            {
                attemptedQuestions.add(number, currentQuestion.getQuestion());
                selectedAnswer.add(number, answer.getText().toString());
                actualAnswer.add(number, currentQuestion.getAnswer());
                number++;
            }
            choicesGroup.clearCheck();
            nextButton.setEnabled(false);
            if (currentQuestionNumber < properSize+1)
            {
                if (currentQuestionNumber == properSize)
                {
                    nextButton.setEnabled(false);
                }
                currentQuestion = quizQuestions.get(list.get(currentQuestionNumber));
                setQuestionView();
            }
            else
            {
                timer.onFinish();
                timer.cancel();
            }
        });
    }

    public class CounterClass extends CountDownTimer
    {
        public CounterClass(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {

            String hms = String.format(Locale.getDefault(),"%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            textTime.setText(hms);
        }

        @Override
        public void onFinish()
        {
           showResult();
        }
    }

    public void showResult()
    {
        Intent intent = new Intent(QuizQuestions.this, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("SCORE", score);
        b.putInt("ATTEMPTED QUESTIONS",answeredQuestions);
        intent.putExtra("TOTAL", properSize);
        intent.putStringArrayListExtra("QUESTIONS", attemptedQuestions);
        intent.putStringArrayListExtra("SELECTED ANSWERS", selectedAnswer);
        intent.putStringArrayListExtra("ACTUAL ANSWERS", actualAnswer);
        intent.putExtras(b);
        startActivity(intent);
        Animatoo.animateDiagonal(this);
        finish();
    }


    private void setQuestionView()
    {
        textViewQuestion.setText(currentQuestion.getQuestion());
        option01.setText(currentQuestion.getOption01());
        option02.setText(currentQuestion.getOption02());
        option03.setText(currentQuestion.getOption03());
        option04.setText(currentQuestion.getOption04());
        String display = currentQuestionNumber <10? "0" + currentQuestionNumber + " / ": currentQuestionNumber + " / ";
        display += (list.size()-1) <10 ? "0" + (list.size()-1): (list.size()-1);
        questionNumber.setText(display);
        currentQuestionNumber++;
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestions.this);
        Typeface face = ResourcesCompat.getFont(QuizQuestions.this,R.font.syndor);
        builder.setMessage("YOU ARE ABOUT TO EXIT THE QUIZ. CLICKING ON OK WILL RESET YOUR ANSWERS.\n\nARE YOU SURE TO EXIT ??")
                .setCancelable(false)
                .setIcon(R.drawable.emoji)
                .setTitle(new Help().typeface(face, "EXIT QUIZ ???"))
                .setPositiveButton( "OK, EXIT", (dialog, id) ->
                {
                    finish();
                    Animatoo.animateDiagonal(QuizQuestions.this);
                }).setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private List<QuizModel> getQuizQuestions(String jsonResponse) throws JSONException
    {
        List<QuizModel> newList = new ArrayList<>();
        JSONObject object = new JSONObject(String.valueOf(jsonResponse));
        JSONArray quizArray = object.getJSONArray("DATA");
        JSONObject questionsObject = quizArray.getJSONObject(0);
        JSONObject optionsObject = quizArray.getJSONObject(1);
        JSONObject answersObject = quizArray.getJSONObject(2);
        for(int i = 0;i<questionsObject.length();i++)
        {
            JSONObject miniObject = optionsObject.getJSONObject(String.valueOf(i+1));
            newList.add(new QuizModel(questionsObject.getString(String.valueOf(i+1)),miniObject.getString("a"),miniObject.getString("b"),miniObject.getString("c"),miniObject.getString("d"),answersObject.getString(String.valueOf(i+1))));
        }
        return newList;
    }

}
