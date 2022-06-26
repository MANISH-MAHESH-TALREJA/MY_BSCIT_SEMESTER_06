package net.manish.mybscitsem06;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

public class Login extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Void>, View.OnClickListener
{
    EditText mPassword, mEmail;
    Button mLoginBtn;
    FirebaseAuth firebaseAuth;
    TextView mRegisterBtn, mResetBtn;
    boolean doubleBack=false;
    Help help;
    FlatDialog flatDialog;
    final int LOADER_ID = 1;
    ACProgressFlower dialog;
    String myDeviceId;
    final String android1="UNIQUE";
    String name, email, pass, state, phone, androidId;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        help = new Help();
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("PLEASE WAIT")
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mEmail = findViewById(R.id.username);
        mEmail.requestFocus();
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.loginBtn);
        mResetBtn=findViewById(R.id.resetBtn);
        flatDialog = new FlatDialog(Login.this);
        myDeviceId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            if (help.getInfo("EMAIL", getApplicationContext())!=null && help.getInfo("PASSWORD", getApplicationContext())!=null && help.getInfo("PHONE", getApplicationContext())!=null && help.getInfo("NAME", getApplicationContext())!=null && help.getInfo("STATE", getApplicationContext())!=null)
            {
                FancyToast.makeText(Login.this, user.getDisplayName()+" IS SIGNED IN", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                startActivity(new Intent(getApplicationContext(), SampleActivity.class));
                Animatoo.animateDiagonal(Login.this);
            }
        }
        mRegisterBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Loader<Void> onCreateLoader(int id, @Nullable Bundle args)
    {
        return new AsyncTaskLoader<Void>(this)
        {
            @Override
            protected void onStartLoading()
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                mRegisterBtn.setClickable(false);
                mResetBtn.setClickable(false);
                mLoginBtn.setClickable(false);
                forceLoad();
                dialog.show();
            }

            @NonNull
            @Override
            public Void loadInBackground()
            {
                final Help help = new Help();
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        assert user1 != null;
                        boolean emailVerified = user1.isEmailVerified();
                        if(emailVerified)
                        {
                            final DocumentReference documentReference =  firebaseFirestore.collection("STUDENTS").document(email);
                            documentReference.get().addOnCompleteListener(task12 -> {
                                if (task12.isSuccessful())
                                {
                                    final DocumentSnapshot document = task12.getResult();
                                    assert document != null;
                                    if (document.exists())
                                    {
                                        documentReference.addSnapshotListener((documentSnapshot, e) -> {
                                            if (document.get("NAME") != null&& document.get("STATE") != null && document.get("PHONE") != null && document.get("PASSWORD") != null && document.get("DEVICE") != null)
                                            {
                                                assert documentSnapshot != null;
                                                name = documentSnapshot.getString("NAME");
                                                state = documentSnapshot.getString("STATE");
                                                phone = documentSnapshot.getString("PHONE");
                                                androidId=documentSnapshot.getString("DEVICE");
                                                help.saveInfo("NAME",name,Login.this);
                                                help.saveInfo("PHONE",phone,Login.this);
                                                help.saveInfo("STATE",state,Login.this);
                                                help.saveInfo("EMAIL",email,Login.this);
                                                help.saveInfo("PASSWORD",pass,Login.this);
                                                if(Objects.equals(androidId,android1))
                                                {
                                                    if(FirebaseAuth.getInstance().getCurrentUser()!=null && help.getInfo("NAME", Login.this)!=null && help.getInfo("EMAIL", Login.this)!=null && help.getInfo("PHONE", Login.this)!=null && help.getInfo("PASSWORD", Login.this)!=null && help.getInfo("STATE", Login.this)!=null)
                                                    {
                                                        DocumentReference documentReference1 = firebaseFirestore.collection("STUDENTS").document(help.getInfo("EMAIL", Login.this));
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("DEVICE", myDeviceId);
                                                        documentReference1.update(user).addOnCompleteListener(task1 -> {
                                                            if(task1.isSuccessful())
                                                            {
                                                                FancyToast.makeText(Login.this, "DEVICE ID REGISTERED SUCCESSFULLY WITH THE ACCOUNT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                            }
                                                            else
                                                            {
                                                                dialog.dismiss();
                                                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                                LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                                                mRegisterBtn.setClickable(true);
                                                                mResetBtn.setClickable(true);
                                                                mLoginBtn.setClickable(true);
                                                                FancyToast.makeText(Login.this, "FAILED TO REGISTER DEVICE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                help.clearData(Login.this);
                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        dialog.dismiss();
                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                        LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                                        mRegisterBtn.setClickable(true);
                                                        mResetBtn.setClickable(true);
                                                        mLoginBtn.setClickable(true);
                                                        FancyToast.makeText(Login.this, "KINDLY RE-LOGIN", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                        help.clearData(Login.this);
                                                    }
                                                }
                                                else if(Objects.equals(androidId,myDeviceId))
                                                {
                                                    if(FirebaseAuth.getInstance().getCurrentUser()!=null && help.getInfo("NAME", Login.this)!=null && help.getInfo("EMAIL", Login.this)!=null && help.getInfo("PHONE", Login.this)!=null && help.getInfo("PASSWORD", Login.this)!=null && help.getInfo("STATE", Login.this)!=null)
                                                    {
                                                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                                                        Date dateObj = new Date();
                                                        String presentDate = currentDate.format(dateObj);
                                                        help.saveInfo("REFRESH", presentDate, getApplicationContext());
                                                        FancyToast.makeText(Login.this, "LOGIN SUCCESSFUL", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                        Intent newIntent = new Intent(Login.this,SampleActivity.class);
                                                        dialog.dismiss();
                                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(newIntent);
                                                        Animatoo.animateDiagonal(Login.this);
                                                        finish();

                                                    }
                                                    else
                                                    {
                                                        dialog.dismiss();
                                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                        LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                                        mRegisterBtn.setClickable(true);
                                                        mResetBtn.setClickable(true);
                                                        mLoginBtn.setClickable(true);
                                                        FancyToast.makeText(Login.this, "KINDLY RE-LOGIN", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                        help.clearData(Login.this);
                                                    }
                                                }
                                                else
                                                {
                                                    dialog.dismiss();
                                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                    LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                                    mRegisterBtn.setClickable(true);
                                                    mResetBtn.setClickable(true);
                                                    mLoginBtn.setClickable(true);
                                                    FancyToast.makeText(Login.this, "YOU ARE ALREADY LOGGED IN TO OTHER DEVICE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                    help.clearData(Login.this);
                                                }
                                            }
                                            else
                                            {
                                                dialog.dismiss();
                                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                                mRegisterBtn.setClickable(true);
                                                mResetBtn.setClickable(true);
                                                mLoginBtn.setClickable(true);
                                                FancyToast.makeText(Login.this, "SOME ERROR HAD OCCURRED WHILE YOU WERE REGISTERING. DATA WAS NOT PROPERLY SUBMITTED TO DATABASE. KINDLY CONTACT SYSTEM ADMINISTRATOR FOR MORE DETAILS", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                help.clearData(Login.this);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                        LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                        mRegisterBtn.setClickable(true);
                                        mResetBtn.setClickable(true);
                                        mLoginBtn.setClickable(true);
                                        FancyToast.makeText(Login.this, "USER'S RECORD DOES NOT EXIST IN DATABASE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        help.clearData(Login.this);
                                    }
                                }
                                else
                                {
                                    dialog.dismiss();
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                    LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                                    mRegisterBtn.setClickable(true);
                                    mResetBtn.setClickable(true);
                                    mLoginBtn.setClickable(true);
                                    FancyToast.makeText(Login.this, "FAILED TO CONNECT DATABASE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    help.clearData(Login.this);
                                }
                            });
                        }
                        else
                        {
                            dialog.dismiss();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                            firebaseAuth.signOut();
                            mRegisterBtn.setClickable(true);
                            mResetBtn.setClickable(true);
                            mLoginBtn.setClickable(true);
                            FancyToast.makeText(Login.this, "YOUR EMAIL IS NOT VERIFIED. KINDLY VERIFY IT AND THEN CONTINUE.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                    else
                    {
                        dialog.dismiss();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        LoaderManager.getInstance(Login.this).destroyLoader(LOADER_ID);
                        mRegisterBtn.setClickable(true);
                        mResetBtn.setClickable(true);
                        mLoginBtn.setClickable(true);
                        FancyToast.makeText(Login.this, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()).toUpperCase(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void data)
    {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Void> loader)
    {
        loader.reset();
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
        if(myView == findViewById(R.id.loginBtn))
        {
            if(new SampleActivity().isInternetAvailable(getApplicationContext()))
            {
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if (user1 != null)
                {
                    help.clearData(getApplicationContext());
                }
                email = mEmail.getText().toString().toUpperCase();
                pass = mPassword.getText().toString();
                if (TextUtils.isEmpty(email))
                {
                    mEmail.setError("EMAIL CANNOT BE EMPTY");
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {
                    mPassword.setError("PASSWORD CANNOT BE EMPTY");
                    return;
                }
                LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
            }
            else
            {
                FancyToast.makeText(Login.this, "INTERNET CONNECTION UNAVAILABLE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }
        if(myView == findViewById(R.id.resetBtn))
        {
            flatDialog.setTitle("RESET PASSWORD")
                    .setSubtitle("ENTER REGISTERED EMAIL ADDRESS")
                    .setFirstTextFieldHint("EMAIL")
                    .setFirstButtonText("RESET PASSWORD")
                    .setSecondButtonText("CANCEL")
                    .withFirstButtonListner(view -> {
                        if(new SampleActivity().isInternetAvailable(getApplicationContext()))
                        {
                            if(flatDialog.getFirstTextField().isEmpty())
                            {
                                FancyToast.makeText(Login.this, "EMAIL CANNOT BE EMPTY", FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
                                return;
                            }
                            dialog.show();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(flatDialog.getFirstTextField()).addOnCompleteListener(task ->
                            {
                                if (task.isSuccessful())
                                {
                                    dialog.dismiss();
                                    flatDialog.setFirstTextField("");
                                    FancyToast.makeText(Login.this, "PASSWORD RESET LINK SENT SUCCESSFULLY", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    flatDialog.dismiss();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    FancyToast.makeText(Login.this, "FAILED TO SENT PASSWORD RESET LINK", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    flatDialog.dismiss();
                                }
                            });
                        }
                        else
                        {
                            FancyToast.makeText(Login.this, "INTERNET CONNECTION UNAVAILABLE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }

                    })
                    .withSecondButtonListner(view -> flatDialog.dismiss())
                    .show();
        }
        if(myView == findViewById(R.id.registerBtn))
        {
            startActivity(new Intent(getApplicationContext(), Register.class));
            Animatoo.animateDiagonal(Login.this);
        }

    }
    @Override
    public void onBackPressed()
    {
        if(!dialog.isShowing())
        {
            if(doubleBack)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Animatoo.animateDiagonal(Login.this);
                System.exit(0);

            }
            doubleBack=true;
            FancyToast.makeText(Login.this, "PRESS THE BACK BUTTON AGAIN TO CLOSE THE APPLICATION", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBack=false,2000);
        }
        else
        {
            FancyToast.makeText(Login.this, "BACK BUTTON IS DISABLED DURING PROCESSING", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
        }
    }
}