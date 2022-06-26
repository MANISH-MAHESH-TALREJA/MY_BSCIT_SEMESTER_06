package net.manish.mybscitsem06;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener
{
    final Help help = new Help();
    String myDeviceId;
    UpdateManager mUpdateManager;
    String name, state, phone, androidId, dbPass;
    FirebaseFirestore firebaseFirestore;
    boolean doubleBack = false;
    private static final int POS_DASHBOARD = 0;
    private static final int POS_BUY_PREMIUM = 2;
    private static final int POS_RATE_APP = 3;
    private static final int POS_SHARE_APP = 4;
    private static final int POS_CONTACT = 6;
    private static final int POS_POLICY = 7;
    private static final int POS_REQUEST = 8;
    private static final int POS_LOGOUT = 11;
    private static final int POS_MORE = 9;
    private static final int POS_CLOSE = 10;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    DrawerAdapter adapter;
    final String pro = new Help().purchase("UFJFTUlVTQ==");
    private SlidingRootNav slidingRootNav;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(()->
        {
            Looper.prepare();
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date dateObj = new Date();
            String presentDate = currentDate.format(dateObj);
            String newDate = help.getInfo("REFRESH",getApplicationContext());
            if(newDate!=null)
            {
                int presentDateValue = Integer.parseInt(presentDate);
                int newDateValue = Integer.parseInt(newDate);
                firebaseFirestore = FirebaseFirestore.getInstance();
                if(presentDateValue>newDateValue)
                {
                    checkStatus();
                }
                new Help().deleteCache(getApplicationContext());
            }
            else
            {
                checkStatus();
            }
        }).start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                new SpaceItem(),
                Objects.equals(new Help().getInfo("STATE", getApplicationContext()),pro)?createItemFor(POS_REQUEST):createItemFor(POS_BUY_PREMIUM),
                createItemFor(POS_RATE_APP),
                createItemFor(POS_SHARE_APP),
                new SpaceItem(),
                createItemFor(POS_CONTACT),
                createItemFor(POS_POLICY),
                createItemFor(POS_MORE),
                new SpaceItem(),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();
    }

    @Override
    public void onItemSelected(int position)
    {
        switch(position)
        {
            case POS_DASHBOARD:
            {
                slidingRootNav.closeMenu();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Dashboard dashboard = new Dashboard();
                fragmentManager.beginTransaction().replace(R.id.container,dashboard).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle("DASHBOARD");
                break;
            }
            case POS_BUY_PREMIUM:
            {
                if(Objects.equals(new Help().getInfo("STATE",getApplicationContext()),pro))
                {
                    slidingRootNav.closeMenu();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    RequestContent requestContent = new RequestContent();
                    fragmentManager.beginTransaction().replace(R.id.container,requestContent).commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("REQUEST");
                    break;
                }
                else
                {
                    slidingRootNav.closeMenu();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    BuyPremium buyPremium = new BuyPremium();
                    fragmentManager.beginTransaction().replace(R.id.container,buyPremium).commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("PREMIUM");
                    break;
                }
            }
            case POS_RATE_APP:
            {
                slidingRootNav.closeMenu();
                adapter.setSelected(POS_DASHBOARD);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.manish.mybscitsem06")));
                Animatoo.animateDiagonal(this);
                break;
            }
            case POS_SHARE_APP:
            {
                slidingRootNav.closeMenu();
                adapter.setSelected(POS_DASHBOARD);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "SHARE MY-BSCIT APP WITH FRIENDS https://play.google.com/store/apps/details?id=net.manish.mybscitsem06";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SHARE MY-BSCIT APP");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "SHARE MY-BSCIT APP"));
                Animatoo.animateDiagonal(this);
                break;
            }
            case POS_CONTACT:
            {
                slidingRootNav.closeMenu();
                FragmentManager fragmentManager = getSupportFragmentManager();
                ContactUs contactUs = new ContactUs();
                fragmentManager.beginTransaction().replace(R.id.container,contactUs).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle("CONTACT US");
                break;
            }
            case POS_POLICY:
            {
                slidingRootNav.closeMenu();
                FragmentManager fragmentManager = getSupportFragmentManager();
                PrivacyPolicy privacyPolicy = new PrivacyPolicy();
                fragmentManager.beginTransaction().replace(R.id.container,privacyPolicy).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle("POLICY");
                break;
            }
            case POS_REQUEST:
            {
                slidingRootNav.closeMenu();
                adapter.setSelected(POS_DASHBOARD);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=MANISH+MAHESH+TALREJA")));
                Animatoo.animateDiagonal(this);
                break;

            }
            case POS_CLOSE:
            {

                Animatoo.animateDiagonal(this);
                System.exit(0);
                break;
            }
            default:
            {
                slidingRootNav.closeMenu();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Dashboard dashboard = new Dashboard();
                fragmentManager.beginTransaction().replace(R.id.container,dashboard).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle("DASHBOARD");
            }
        }
    }


    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position)
    {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles()
    {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons()
    {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res)
    {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onBackPressed()
    {
        if(doubleBack)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Animatoo.animateDiagonal(this);
            System.exit(0);
        }
        doubleBack=true;
        if (slidingRootNav.isMenuOpened())
            slidingRootNav.closeMenu();
        FancyToast.makeText(this, "PRESS THE BACK BUTTON AGAIN TO CLOSE THE APPLICATION", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBack=false,2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("HardwareIds")
    public void checkStatus()
    {
        if(new SampleActivity().isInternetAvailable(getApplicationContext()))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && help.getInfo("EMAIL", getApplicationContext()) != null && help.getInfo("PASSWORD", getApplicationContext()) != null && help.getInfo("PHONE", getApplicationContext()) != null && help.getInfo("STATE", getApplicationContext())!=null && help.getInfo("NAME", getApplicationContext()) != null)
            {
                final DocumentReference documentReference =  firebaseFirestore.collection("STUDENTS").document(help.getInfo("EMAIL",getApplicationContext()));
                documentReference.get().addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        final DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists())
                        {
                            documentReference.addSnapshotListener((documentSnapshot, e) ->
                            {
                                if (document.get("NAME") != null&& document.get("STATE") != null && document.get("PHONE") != null && document.get("PASSWORD") != null && document.get("DEVICE") != null)
                                {
                                    assert documentSnapshot != null;
                                    name = documentSnapshot.getString("NAME");
                                    state = documentSnapshot.getString("STATE");
                                    phone = documentSnapshot.getString("PHONE");
                                    androidId=documentSnapshot.getString("DEVICE");
                                    dbPass=documentSnapshot.getString("PASSWORD");
                                    help.saveInfo("NAME",name,SampleActivity.this);
                                    help.saveInfo("PHONE",phone,SampleActivity.this);
                                    help.saveInfo("STATE",state,SampleActivity.this);
                                    help.saveInfo("PASSWORD",dbPass,SampleActivity.this);
                                    myDeviceId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    if(!Objects.equals(androidId,myDeviceId))
                                    {
                                        help.clearData(getApplicationContext());
                                        FirebaseAuth.getInstance().signOut();
                                        help.deleteFiles(getApplicationContext().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)");
                                        help.deleteFiles(getApplicationContext().getApplicationInfo().dataDir+"/shared_prefs");
                                        Intent newIntent = new Intent(getApplicationContext(),Login.class);
                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(newIntent);
                                        Animatoo.animateDiagonal(this);
                                        finish();
                                    }
                                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                                    Date dateObj = new Date();
                                    String presentDate = currentDate.format(dateObj);
                                    help.saveInfo("REFRESH", presentDate, getApplicationContext());
                                    FancyToast.makeText(SampleActivity.this, "DATA SYNCED SUCCESSFULLY FROM SERVER.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                }
                                else
                                {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                    FancyToast.makeText(SampleActivity.this, "SOME ERROR HAD OCCURRED WHILE YOU WERE REGISTERING. DATA WAS NOT PROPERLY SUBMITTED TO DATABASE. KINDLY CONTACT SYSTEM ADMINISTRATOR FOR MORE DETAILS. UNABLE TO SYNC DATA FROM SERVER", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            });
                        }
                        else
                        {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            FancyToast.makeText(SampleActivity.this, "USER'S RECORD DOES NOT EXIST IN DATABASE. UNABLE TO SYNC DATA FROM SERVER", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                    else
                    {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        FancyToast.makeText(SampleActivity.this, "FAILED TO CONNECT DATABASE. UNABLE TO SYNC DATA FROM SERVER. UNABLE TO SYNC DATA FROM SERVER", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
            }
            else
            {
                FancyToast.makeText(getApplicationContext(), "THERE WAS SOME TECHNICAL ISSUE WHILE HANDLING YOUR DATA. KINDLY RE-LOGIN AND TRY AGAIN. UNABLE TO SYNC DATA FROM SERVER", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        }
        else
        {
            FancyToast.makeText(getApplicationContext(), "INTERNET IS NOT AVAILABLE. UNABLE TO SYNC DATA FROM SERVER", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==R.id.action_more)
        {
            startActivity(new Intent(getApplicationContext(), ViewProfile.class));
            Animatoo.animateDiagonal(this);
        }
        if(item.getItemId()==R.id.acton_refresh)
        {
            checkStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isInternetAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
