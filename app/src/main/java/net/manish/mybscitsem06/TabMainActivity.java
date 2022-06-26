package net.manish.mybscitsem06;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;
import java.util.Objects;

public class TabMainActivity extends AppCompatActivity
{
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        CardFlipPageTransformer cardFlipPageTransformer = new CardFlipPageTransformer();
        viewPager.setPageTransformer(true, cardFlipPageTransformer);
        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.navigationBar);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                bubbleNavigationLinearView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        Typeface face = ResourcesCompat.getFont(Objects.requireNonNull(getApplicationContext()),R.font.syndor);
        bubbleNavigationLinearView.setTypeface(face);
        bubbleNavigationLinearView.setNavigationChangeListener((view, position) -> viewPager.setCurrentItem(position, true));
    }

    private void setupViewPager(ViewPager viewPager)
    {
        BottomNavPagerAdapter adapter = new BottomNavPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFragment());
        adapter.addFragment(new SecondFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateDiagonal(TabMainActivity.this);
    }
}
