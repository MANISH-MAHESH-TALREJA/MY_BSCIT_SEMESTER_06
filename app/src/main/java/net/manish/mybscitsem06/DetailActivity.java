package net.manish.mybscitsem06;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity
{
    ProgressBar progressBar;
    Toolbar toolbar;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        webview = findViewById(R.id.detailView);
        setSupportActionBar(toolbar);
        webview.setVisibility(View.INVISIBLE);
        webview.getSettings().getJavaScriptEnabled();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                FancyToast.makeText(DetailActivity.this, "NOTICE STARTED LOADING",FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                FancyToast.makeText(DetailActivity.this, "NOTICE LOADED SUCCESSFULLY",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,false).show();
                String javaScript ="javascript:(" +
                        "function() " +
                        "{ " +
                        "var a= document.getElementsByTagName('header');" +
                        "a[0].hidden='true';" +
                        "var a= document.getElementsByTagName('footer');" +
                        "a[0].hidden='true';" +
                        "var a= document.getElementsByClassName('widget PopularPosts');" +
                        "a[0].hidden='true';" +
                        "var a= document.getElementsByClassName('container section');" +
                        "a[0].hidden='true';" +
                        "a=document.getElementsByClassName('page_body');" +
                        "a[0].style.padding='0px';" +
                        "})()";
                webview.loadUrl(javaScript);
            }
        });
        webview.loadUrl(Objects.requireNonNull(getIntent().getStringExtra("url")));
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Animatoo.animateDiagonal(this);
        finish();
    }

}