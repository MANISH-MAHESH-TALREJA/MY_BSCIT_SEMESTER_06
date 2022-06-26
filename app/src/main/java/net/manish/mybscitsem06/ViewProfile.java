package net.manish.mybscitsem06;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener
{
    TextView name, state, email, phone;
    Help help;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        help = new Help();
        name = findViewById(R.id.profileName);
        name.setText(help.getInfo("NAME", getApplicationContext()).toUpperCase());
        phone = findViewById(R.id.profilePhone);
        phone.setText(help.getInfo("PHONE", getApplicationContext()).toUpperCase());
        email = findViewById(R.id.profileEmail);
        email.setText(help.getInfo("EMAIL", getApplicationContext()).toUpperCase());
        state = findViewById(R.id.profileState);
        state.setText(help.getInfo("STATE", getApplicationContext()).toUpperCase());
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.btnLogout).setOnClickListener(this);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }

    long mLastClickTime = 0;
    @Override
    public void onClick(View myView)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(myView==findViewById(R.id.btnLogout))
        {
            help.clearData(getApplicationContext());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null)
            {
                FirebaseAuth.getInstance().signOut();
            }
            help.deleteFiles(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)");
            help.deleteFiles(getApplicationContext().getApplicationInfo().dataDir+"/shared_prefs");
            Intent newIntent = new Intent(getApplicationContext(),Login.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            Animatoo.animateDiagonal(this);
            finish();
        }
        if(myView == findViewById(R.id.back))
        {
            onBackPressed();
        }
    }
}
