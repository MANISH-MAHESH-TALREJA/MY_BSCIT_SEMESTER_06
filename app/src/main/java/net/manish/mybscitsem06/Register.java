package net.manish.mybscitsem06;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Register extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, View.OnClickListener
{
    EditText mName, mPassword, mEmail, mPhone, mConfirmPass, mSurname;
    ACProgressFlower dialog;
    CheckBox checkbox;
    String email, name, phone, pass, confirmPass, surname;
    FirebaseFirestore firebaseFirestore;
    final int LOADER_ID = 1;
    Help help;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        help = new Help();
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("PLEASE WAIT")
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mName = findViewById(R.id.name);
        mName.requestFocus();
        mEmail = findViewById(R.id.email);
        mSurname = findViewById(R.id.surname);
        mPassword = findViewById(R.id.password);
        checkbox = findViewById(R.id.checkBox);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        mPhone=findViewById(R.id.phone);
        mConfirmPass=findViewById(R.id.confirm);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        findViewById(R.id.terms).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.registerBtn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
    }

    public boolean check(String inputString)
    {
        String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~0123456789";
        String[] strlCharactersArray = new String[inputString.length()];
        for (int i = 0; i < inputString.length(); i++)
        {
            strlCharactersArray[i] = Character.toString(inputString.charAt(i));
        }
        int count = 0;
        for (String s : strlCharactersArray) {
            if (specialCharacters.contains(s)) {
                count++;
            }

        }
        return count != 0;
    }
    public boolean check2(String inputString)
    {
        String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String[] strCharactersArray = new String[inputString.length()];
        for (int i = 0; i < inputString.length(); i++)
        {
            strCharactersArray[i] = Character.toString(inputString.charAt(i));
        }
        int count = 0;
        for (String s : strCharactersArray) {
            if (specialCharacters.contains(s)) {
                count++;
            }

        }
        return count != 0;
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
                forceLoad();
                dialog.show();
            }

            @NonNull
            @Override
            public Void loadInBackground()
            {
                final Help help = new Help();
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        FancyToast.makeText(Register.this, "USER IS SUCCESSFULLY CREATED", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name+" "+surname).build();
                        assert user1 != null;
                        user1.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful())
                            {
                                FancyToast.makeText(Register.this, "PROFILE SUCCESSFULLY UPDATED", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                user1.sendEmailVerification().addOnCompleteListener(task11 -> {
                                    if (task11.isSuccessful())
                                    {
                                        FancyToast.makeText(Register.this, "VERIFICATION EMAIL SENT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        DocumentReference documentReference = firebaseFirestore.collection("STUDENTS").document(email);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("NAME", name+" "+surname);
                                        user.put("PASSWORD",pass);
                                        user.put("PHONE",phone);
                                        user.put("STATE",help.purchase("RlJFRU1JVU0="));
                                        user.put("DEVICE",help.purchase("VU5JUVVF"));
                                        documentReference.set(user).addOnSuccessListener(aVoid -> new Handler(Looper.getMainLooper()).postDelayed(() ->
                                        {
                                            FancyToast.makeText(Register.this, "USER REGISTERED IN DATABASE", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                            FirebaseAuth.getInstance().signOut();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                            Typeface face = ResourcesCompat.getFont(Register.this,R.font.syndor);
                                            builder.setMessage("HELLO "+name+", YOU ARE SUCCESSFULLY REGISTERED WITH MY-BSCIT SEMESTER 06.\n\n     AN EMAIL WITH CONFIRMATION LINK HAS BEEN SENT YOUR REGISTERED EMAIL ID.\n\n     KINDLY CONFIRM THE EMAIL ADDRESS BY CLICKING ON THE LINK GIVEN IN EMAIL TO PROCEED TOWARDS LOGIN. ")
                                                    .setCancelable(false)
                                                    .setIcon(R.drawable.correct)
                                                    .setTitle(new Help().typeface(face, "REGISTRATION DONE"))
                                                    .setNegativeButton("OK", (dialog, id) ->
                                                    {
                                                        dialog.cancel();
                                                        Animatoo.animateDiagonal(Register.this);
                                                        finish();
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }, 3000)).addOnFailureListener(e ->
                                        {
                                            dialog.dismiss();
                                            LoaderManager.getInstance(Register.this).destroyLoader(LOADER_ID);
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                            FirebaseAuth.getInstance().signOut();
                                            FancyToast.makeText(Register.this, e.toString().toUpperCase(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        });
                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                        LoaderManager.getInstance(Register.this).destroyLoader(LOADER_ID);
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                        FirebaseAuth.getInstance().signOut();
                                        FancyToast.makeText(Register.this,"UNABLE TO SEND VERIFICATION EMAIL", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    }
                                });
                            }
                            else
                            {
                                dialog.dismiss();
                                LoaderManager.getInstance(Register.this).destroyLoader(LOADER_ID);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                FirebaseAuth.getInstance().signOut();
                                FancyToast.makeText(Register.this,"FAILED TO UPDATE PROFILE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        });
                    }
                    else
                    {
                        dialog.dismiss();
                        LoaderManager.getInstance(Register.this).destroyLoader(LOADER_ID);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        FirebaseAuth.getInstance().signOut();
                        FancyToast.makeText(Register.this,Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()).toUpperCase(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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

    @Override
    public void onBackPressed()
    {
        if(dialog.isShowing())
        {
            FancyToast.makeText(Register.this,"IN ORDER TO AVOID FAILURES DURING REGISTRATION PROCESS, WE HAVE DISABLED THE PRESS OF BACK BUTTON INSIDE THIS ACTIVITY.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        }
        else
        {
            super.onBackPressed();
            Animatoo.animateDiagonal(this);
            finish();
        }
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
        if(myView == findViewById(R.id.registerBtn))
        {
            if(new SampleActivity().isInternetAvailable(getApplicationContext()))
            {
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if (user1 != null)
                {
                    firebaseAuth.signOut();
                    new Help().clearData(getApplicationContext());
                }
                email = mEmail.getText().toString().toUpperCase();
                surname = mSurname.getText().toString().toUpperCase();
                name = mName.getText().toString().toUpperCase().toUpperCase();
                pass = mPassword.getText().toString().toUpperCase();
                confirmPass= mConfirmPass.getText().toString().toUpperCase();
                phone=mPhone.getText().toString().toUpperCase();
                firebaseFirestore = FirebaseFirestore.getInstance();
                if (TextUtils.isEmpty(email))
                {
                    mEmail.setError("EMAIL CANNOT BE EMPTY");
                    return;
                }
                if (TextUtils.isEmpty(surname))
                {
                    mSurname.setError("SURNAME CANNOT BE EMPTY");
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {
                    mPassword.setError("PASSWORD CANNOT BE EMPTY");
                    return;
                }
                if(!checkbox.isChecked())
                {
                    FancyToast.makeText(Register.this,"KINDLY AGREE TO THE TERMS OF USE", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    return;
                }
                if (TextUtils.isEmpty(name))
                {
                    mName.setError("NAME CANNOT BE EMPTY");
                    return;
                }
                if (TextUtils.isEmpty(phone))
                {
                    mPhone.setError("SURNAME CANNOT BE EMPTY");
                    return;
                }
                if (TextUtils.isEmpty(confirmPass))
                {
                    mConfirmPass.setError("SURNAME CANNOT BE EMPTY");
                    return;
                }
                if(!pass.equals(confirmPass))
                {
                    mConfirmPass.setError("BOTH THE PASSWORD DOES NOT MATCH");
                    return;
                }
                if(pass.contains(" "))
                {
                    mPassword.setError("PASSWORD CANNOT CONTAIN SPACE CHARACTERS");
                    return;
                }
                if(surname.contains(" "))
                {
                    mSurname.setError("SURNAME CANNOT CONTAIN SPACE CHARACTERS");
                    return;
                }
                if(confirmPass.contains(" "))
                {
                    mConfirmPass.setError("PASSWORD CANNOT CONTAIN SPACE CHARACTERS");
                    return;
                }
                if(phone.contains(" "))
                {
                    mPhone.setError("PHONE CANNOT CONTAIN SPACE CHARACTERS");
                    return;
                }
                if(!(pass.length() >=6))
                {
                    mPassword.setError("PASSWORD MUST BE OF SIX OR MORE THAN SIX DIGITS");
                    return;
                }
                if(phone.length()!=10)
                {
                    mPhone.setError("PHONE NUMBER SHOULD BE OF 10 DIGITS");
                    return;
                }
                if(check(name))
                {
                    mName.setError("NAME IS IN-APPROPRIATE, IT MUST NOT CONTAIN SPECIAL CHARACTERS");
                    return;
                }
                if(check(surname))
                {
                    mSurname.setError("SURNAME IS IN-APPROPRIATE, IT MUST NOT CONTAIN SPECIAL CHARACTERS");
                    return;
                }
                if(check2(pass))
                {
                    mPassword.setError("PASSWORD MUST BE IN NUMBERS ONLY (MAXIMUM LENGTH MUST BE 6)");
                    return;
                }
                if(check2(confirmPass))
                {
                    mConfirmPass.setError("PASSWORD MUST BE IN NUMBERS ONLY (MAXIMUM LENGTH MUST BE 6)");
                    return;
                }
                if(check2(phone))
                {
                    mPhone.setError("PHONE NUMBER IS IN-APPROPRIATE");
                    return;
                }
                LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
            }
            else
            {
                FancyToast.makeText(Register.this, "INTERNET CONNECTION UNAVAILABLE", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }
        if(myView == findViewById(R.id.terms))
        {
            Typeface face = ResourcesCompat.getFont(Objects.requireNonNull(getApplicationContext()),R.font.syndor);
            AlertDialog alertDialog=new AlertDialog.Builder(Register.this).create();
            alertDialog.setTitle(help.typeface(face, "REDIRECTION"));
            alertDialog.setCancelable(false);
            alertDialog.setMessage("YOU WILL BE REDIRECTED TO THE TERMS OF USE WEBPAGE OF MY-BSCIT SEMESTER 06, IN YOUR DEFAULT WEB BROWSER. DO YOU WISH TO CONTINUE?");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "REDIRECT", (dialog, which) -> {
                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/my-bscit-semester-05")));
                Animatoo.animateDiagonal(this);
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());
            alertDialog.setIcon(R.drawable.emoji);
            alertDialog.show();
            TextView textView = Objects.requireNonNull(alertDialog.getWindow()).findViewById(android.R.id.message);
            Button button1 =  alertDialog.getWindow().findViewById(android.R.id.button1);
            Button button2 = alertDialog.getWindow().findViewById(android.R.id.button2);
            Button button3 = alertDialog.getWindow().findViewById(android.R.id.button3);
            textView.setTypeface(face, Typeface.BOLD);
            button1.setTypeface(face,Typeface.BOLD);
            button2.setTypeface(face,Typeface.BOLD);
            button3.setTypeface(face,Typeface.BOLD);
        }
        if(myView == findViewById(R.id.loginBtn))
        {
            startActivity(new Intent(getApplicationContext(), Login.class));
            Animatoo.animateDiagonal(this);
            finish();
        }
        if(myView == findViewById(R.id.back))
        {
            onBackPressed();
        }

    }
}
