package net.manish.mybscitsem06;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import java.util.ArrayList;

public class WrongQuestion extends AppCompatActivity
{
    public ArrayList<String> wrongQuests = new ArrayList<>();
    public ArrayList<String> selectedAnswers = new ArrayList<>();
    public ArrayList<String> actualAnswers = new ArrayList<>();
    private final ArrayList<ListModel> m_parts = new ArrayList<>();
    ListView listView;
    final ResultActivity resultActivity = new ResultActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_question);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        listView = findViewById(R.id.listView1);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
        wrongQuests = resultActivity.wrongQuests;
        selectedAnswers = resultActivity.selectedAnswers;
        actualAnswers = resultActivity.actualAnswers;
        wrongQuests = getIntent().getStringArrayListExtra("QUESTIONS");
        selectedAnswers = getIntent().getStringArrayListExtra("SELECTED ANSWERS");
        actualAnswers = getIntent().getStringArrayListExtra("ACTUAL ANSWERS");
        String[] strQuestions = new String[wrongQuests.size()];
        String[] strSelectedAnswers = new String[selectedAnswers.size()];
        String[] strActualAnswers = new String[actualAnswers.size()];
        strQuestions = wrongQuests.toArray(strQuestions);
        strSelectedAnswers = selectedAnswers.toArray(strSelectedAnswers);
        strActualAnswers = actualAnswers.toArray(strActualAnswers);
        for(int i=0; i<strQuestions.length;i++)
        {
            m_parts.add(new ListModel(strQuestions[i], strSelectedAnswers[i], strActualAnswers[i]));
        }
        ListAdapter listAdapter = new ListAdapter(this, R.layout.list_row, m_parts);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }

}