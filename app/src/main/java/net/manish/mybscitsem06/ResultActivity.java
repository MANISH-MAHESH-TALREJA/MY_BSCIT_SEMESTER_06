package net.manish.mybscitsem06;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity
{
    int score=0;
    long mLastClickTime = 0;
    Button buttonWrongQuestions;
    public ArrayList<String> wrongQuests = new ArrayList<>();
    public ArrayList<String> selectedAnswers = new ArrayList<>();
    public ArrayList<String> actualAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ImageButton img = findViewById(R.id.imageView);
        buttonWrongQuestions = findViewById(R.id.buttonWrongQuestions);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        rotate.setRepeatCount(1);
        img.startAnimation(rotate);
        TextView txtCorrectAns = findViewById(R.id.txtCorrectAns);
        final Bundle b = getIntent().getExtras();
        score = b.getInt("SCORE");
        String correctAnswers = "TOTAL ANSWERED : "+b.getInt("ATTEMPTED QUESTIONS") + "\n" + "CORRECT ANS : " + score + "\nWRONG ANS : " + (b.getInt("ATTEMPTED QUESTIONS") - score);
        txtCorrectAns.setText(correctAnswers);
        wrongQuests = getIntent().getStringArrayListExtra("QUESTIONS");
        selectedAnswers = getIntent().getStringArrayListExtra("SELECTED ANSWERS");
        actualAnswers = getIntent().getStringArrayListExtra("ACTUAL ANSWERS");
        double percentage = (score*100)/(double) getIntent().getIntExtra("TOTAL",0);
        DecimalFormat df = new DecimalFormat("##.###");
        TextView textViewPercentage = findViewById(R.id.textViewPercentage);
        String percent = ""+df.format(percentage)+" %";
        textViewPercentage.setText(percent);

        if(wrongQuests.size()>0)
        {
            buttonWrongQuestions.setOnClickListener(view ->
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
                {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(ResultActivity.this, WrongQuestion.class);
                intent.putStringArrayListExtra("QUESTIONS", wrongQuests);
                intent.putStringArrayListExtra("SELECTED ANSWERS", selectedAnswers);
                intent.putStringArrayListExtra("ACTUAL ANSWERS", actualAnswers);
                startActivity(intent);
                Animatoo.animateDiagonal(ResultActivity.this);
                finish();
            });
        }
        else
        {
            String ok = "OK";
            buttonWrongQuestions.setText(ok);
            buttonWrongQuestions.setOnClickListener(v ->
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
                {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                onBackPressed();
            });
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }
}
