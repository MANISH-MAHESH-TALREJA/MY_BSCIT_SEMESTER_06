package net.manish.mybscitsem06;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//import com.appolica.flubber.Flubber;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import yanzhikai.textpath.SyncTextPathView;

public class Start extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        /*TextView text = findViewById(R.id.mainText);
        ImageView image = findViewById(R.id.startImage);
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_UP)
                .duration(3000)
                .createFor(text)
                .start();
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_DOWN)
                .duration(3000)
                .createFor(image)
                .start();*/
        SyncTextPathView syncTextPathView3 = findViewById(R.id.fancy_text3);
        syncTextPathView3.startAnimation(0,1);
        SyncTextPathView syncTextPathView4 = findViewById(R.id.fancy_text4);
        syncTextPathView4.startAnimation(0,1);
        new Handler(Looper.getMainLooper()).postDelayed(() ->
        {
            Intent newIntent = new Intent(getApplicationContext(),Login.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            Animatoo.animateDiagonal(this);
            finish();

        },4000);
    }

    @Override
    public void onBackPressed()
    {

    }
}
