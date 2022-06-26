package net.manish.mybscitsem06;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.net.ssl.HttpsURLConnection;
import cc.cloudist.acplibrary.ACProgressBaseDialog;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SubjectsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, View.OnClickListener
{
    private static final int LOADER_ID = 0;
    String mName, mLink;
    final Help help = new Help();
    final String pro = help.purchase("UFJFTUlVTQ==");
    String storeData;
    ACProgressBaseDialog progress;

    public void process(final String name, final String link)
    {
        final Help help = new Help();
        mName = name;
        mLink = link;
        if(new SampleActivity().isInternetAvailable(getApplicationContext()))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (Objects.equals(help.getInfo("STATE", getApplicationContext()), pro) && user != null && help.getInfo("EMAIL", getApplicationContext()) != null && help.getInfo("PASSWORD", getApplicationContext()) != null && help.getInfo("PHONE", getApplicationContext()) != null && help.getInfo("STATE", getApplicationContext()) != null && help.getInfo("NAME", getApplicationContext()) != null)
            {
                LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
            }
            else
            {
                FancyToast.makeText(getApplicationContext(), "SORRY! YOU CAN'T ACCESS THIS STUDY MATERIAL AS YOU ARE NOT A PREMIUM MEMBER. KINDLY PURCHASE THE MEMBERSHIP TO AVAIL THE BENEFITS.",FancyToast.LENGTH_SHORT,FancyToast.ERROR, false).show();  }
        }
        else
        {
            FancyToast.makeText(getApplicationContext(), "INTERNET IS NOT AVAILABLE NOR FILE IS DOWNLOADED FOR OFFLINE MODE.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        progress = new ACProgressFlower.Builder(this).direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.WHITE).text("PLEASE WAIT").fadeColor(Color.DKGRAY).build();
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        findViewById(R.id.SPM).setOnClickListener(SubjectsActivity.this);
        findViewById(R.id.IOT).setOnClickListener(this);
        findViewById(R.id.AWP).setOnClickListener(this);
        findViewById(R.id.AI).setOnClickListener(this);
        findViewById(R.id.LSA).setOnClickListener(this);
        findViewById(R.id.EJ).setOnClickListener(this);
        findViewById(R.id.NGT).setOnClickListener(this);
    }
    public URL createURL(String url)
    {
        URL newUrl;
        try
        {
            newUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
        return  newUrl;
    }
    public String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";
        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        try
        {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.connect();
            inputStream = httpsURLConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(httpsURLConnection!=null)
            {
                httpsURLConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    public String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }
    @NonNull
    @Override
    public Loader<Void> onCreateLoader(int id, @Nullable Bundle args)
    {
        return new AsyncTaskLoader<Void>(this)
        {
            @Nullable
            @Override
            public Void loadInBackground()
            {
                final String[] jsonResponse = {null};
                try
                {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    final DocumentReference documentReference = firebaseFirestore.collection("SEMESTER 06").document("DATA");
                    documentReference.get().addOnCompleteListener(task ->
                    {
                        if(task.isSuccessful())
                        {
                            final DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists())
                            {
                                documentReference.addSnapshotListener((documentSnapshot, e) ->
                                {
                                    if (document.get(mLink) != null)
                                    {
                                        assert documentSnapshot != null;
                                        storeData = documentSnapshot.getString(mLink);
                                        URL url = createURL(storeData);
                                        new Thread(() ->
                                        {
                                            try
                                            {
                                                assert url != null;
                                                jsonResponse[0] = makeHttpRequest(url);
                                                Intent intent= new Intent(getApplicationContext(), QuizQuestions.class);
                                                intent.putExtra("NAME",mName);
                                                intent.putExtra("DATA",jsonResponse[0]);
                                                startActivity(intent);
                                                Animatoo.animateDiagonal(SubjectsActivity.this);
                                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                progress.dismiss();
                                            }
                                            catch (IOException ee)
                                            {
                                                ee.printStackTrace();
                                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
                                                progress.dismiss();
                                                FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

                                            }
                                        }).start();
                                    }
                                    else
                                    {
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                        LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
                                        progress.dismiss();
                                        FancyToast.makeText(getApplicationContext(), "CONTENT IS CURRENTLY UNAVAILABLE, IT WILL BE AVALABLE SHORTLY", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                        
                                    }
                                });
                            }
                        }
                        else
                        {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
                            progress.dismiss();
                            FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            
                        }
                    }).addOnFailureListener(e -> {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
                        progress.dismiss();
                        FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                        
                    });
                }
                catch (Exception e)
                {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    progress.dismiss();
                    LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
                    FancyToast.makeText(getApplicationContext(), "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                return  null;
            }

            @Override
            protected void onStartLoading()
            {
                forceLoad();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                progress.show();

            }

            @Override
            public void deliverResult(@Nullable Void data)
            {
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void data)
    {
        LoaderManager.getInstance(SubjectsActivity.this).destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Void> loader)
    {
        loader.reset();
    }

    @Override
    public void onBackPressed()
    {
        if(progress.isShowing())
        {
            FancyToast.makeText(getApplicationContext(), "YOU ARE NOT ALLOWED TO PRESS BACK BUTTON WHILE LOADING...", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            return;
        }
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }

    private long mLastClickTime = 0;
    @Override
    public void onClick(View view)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(view  == findViewById(R.id.SPM))
        {
            process("USIT601 (SQA) - QUIZ","601-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.IOT))
        {
            process("USIT602 (SIC) - QUIZ","602-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.AWP))
        {
            process("USIT603 (BI) - QUIZ","603-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.AI))
        {
            process("USIT604 (PGIS) - QUIZ","604-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.LSA))
        {
            process("USIT605 (EN) - QUIZ","605-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.EJ))
        {
            process("USIT606 (ITSM) - QUIZ","606-QUIZ-LINK");
        }
        else if(view  == findViewById(R.id.NGT))
        {
            process("USIT607 (CL) - QUIZ","607-QUIZ-LINK");
        }
    }


}