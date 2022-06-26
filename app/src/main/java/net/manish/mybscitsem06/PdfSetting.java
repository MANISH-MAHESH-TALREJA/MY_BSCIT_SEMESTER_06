package net.manish.mybscitsem06;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shashank.sony.fancytoastlib.FancyToast;

public class PdfSetting extends AppCompatActivity implements View.OnClickListener
{
    SwitchMaterial mMode, mScroll, mDoubleTap, mFit;
    final Help help = new Help();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_setting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mMode = findViewById(R.id.switchDark);
        mScroll = findViewById(R.id.switchScroll);
        mDoubleTap = findViewById(R.id.switchDouble);
        mFit = findViewById(R.id.switchFit);
        if (help.getSettings("MODE", getApplicationContext())) {
            mMode.setChecked(true);
        }
        if (help.getSettings("SCROLL", getApplicationContext())) {
            mScroll.setChecked(true);
        }
        if (help.getSettings("TAP", getApplicationContext())) {
            mDoubleTap.setChecked(true);
        }
        if (help.getSettings("FIT", getApplicationContext())) {
            mFit.setChecked(true);
        }
        mMode.setOnCheckedChangeListener((buttonView, isChecked) -> help.saveSettings("MODE", isChecked, getApplicationContext()));
        mScroll.setOnCheckedChangeListener((buttonView, isChecked) -> help.saveSettings("SCROLL", isChecked, getApplicationContext()));
        mFit.setOnCheckedChangeListener((buttonView, isChecked) -> help.saveSettings("FIT", isChecked, getApplicationContext()));
        mDoubleTap.setOnCheckedChangeListener((buttonView, isChecked) -> help.saveSettings("TAP", isChecked, getApplicationContext()));
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.saveSetting).setOnClickListener(this);
        findViewById(R.id.resetDefault).setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        Intent i = getIntent();
        Intent intent = new Intent();
        intent.putExtra("NAME",i.getStringExtra("NAME"));
        intent.putExtra("FILE",i.getStringExtra("FILE"));
        intent.putExtra("DATA",i.getStringExtra("DATA"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(getApplicationContext(), PdfActivity.class);
        startActivity(intent);
        Animatoo.animateDiagonal(this);
        finish();
    }
    private long mLastClickTime = 0;
    @Override
    public void onClick(View myView)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(myView  == findViewById(R.id.saveSetting))
        {
            FancyToast.makeText(PdfSetting.this, "SETTINGS SAVED SUCCESSFULLY", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            onBackPressed();
        }
        else if(myView  == findViewById(R.id.resetDefault))
        {
            help.saveSettings("SCROLL", false, getApplicationContext());
            help.saveSettings("MODE", false, getApplicationContext());
            help.saveSettings("FIT", false, getApplicationContext());
            help.saveSettings("TAP", false, getApplicationContext());
            FancyToast.makeText(PdfSetting.this, "SETTINGS CHANGED TO DEFAULT", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            onBackPressed();
        }
        else if(myView  == findViewById(R.id.back))
        {
            onBackPressed();
        }
    }
}
