package net.manish.mybscitsem06;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.io.File;
import java.util.Objects;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener, OnPageErrorListener
{
    public static final String MY_PREFS_NAME = "PDF PAGE NUMBER";
    File file;
    Intent intent;
    String password;
    PDFView pdfView;
    int newNumber = 0;
    final String name = "PAGE ";
    SharedPreferences prefs;

    @Override
    public void onPageChanged(int page, int pageCount)
    {
        setTitle(String.format("%s %s / %s", name, page + 1, pageCount));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(intent.getStringExtra("NAME"), page);
        editor.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        intent=getIntent();
        pdfView=findViewById(R.id.pdfView);
        LinearLayout layout = findViewById(R.id.layout);
        if(new Help().getSettings("MODE", getApplicationContext()))
        {
            layout.setBackgroundColor(getResources().getColor(R.color.black));
        }
        else
        {
            layout.setBackgroundColor(getResources().getColor(R.color.white));
        }
        file=new File (Objects.requireNonNull(intent.getStringExtra("FILE")));
        password=intent.getStringExtra("DATA");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("PDF VIEWER");
        pdf(file,password);
    }
    public void pdf(File file,String password)
    {
        Help help = new Help();
        if (prefs.contains(intent.getStringExtra("NAME")))
        {
            newNumber = prefs.getInt(intent.getStringExtra("NAME"), 0);
        }
        try
        {
            pdfView.fromFile(file)
                    .password(password)
                    .swipeHorizontal(help.getSettings("SCROLL", getApplicationContext()))
                    .onPageChange(this)
                    .onPageError(this)
                    .defaultPage(newNumber)
                    .enableDoubletap(help.getSettings("TAP", getApplicationContext()))
                    .fitEachPage(help.getSettings("FIT", getApplicationContext()))
                    .nightMode(help.getSettings("MODE", getApplicationContext()))
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }
        catch (Exception e)
        {
            startActivity(new Intent(getApplicationContext(), SampleActivity.class));
            Animatoo.animateDiagonal(this);
            finish();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==R.id.action_more)
        {
            Intent data = getIntent();
            Intent intent = new Intent();
            intent.putExtra("NAME",data.getStringExtra("NAME"));
            intent.putExtra("FILE", data.getStringExtra("FILE"));
            intent.putExtra("DATA", data.getStringExtra("DATA"));
            intent.setClass(getApplicationContext(),  PdfSetting.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Animatoo.animateDiagonal(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPageError(int page, Throwable t)
    {
        FancyToast.makeText(PdfActivity.this,"CANNOT LOAD PAGE"+page,FancyToast.LENGTH_SHORT,FancyToast.INFO, false).show();
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }
}
