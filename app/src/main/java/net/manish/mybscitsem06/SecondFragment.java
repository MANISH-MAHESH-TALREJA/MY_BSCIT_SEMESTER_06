package net.manish.mybscitsem06;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.tompee.funtablayout.FlipTabAdapter;
import com.tompee.funtablayout.FunTabLayout;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;

@SuppressWarnings("deprecation")
public class SecondFragment extends Fragment implements FlipTabAdapter.IconFetcher
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ViewPager viewPager =view.findViewById(R.id.viewpager_content1);
        setupViewPager(viewPager);
        CardFlipPageTransformer cardFlipPageTransformer = new CardFlipPageTransformer();
        viewPager.setPageTransformer(true, cardFlipPageTransformer);
        FunTabLayout tabLayout = view.findViewById(R.id.tablayout1);
        FlipTabAdapter.Builder builder = new FlipTabAdapter.Builder(getContext()).
                setViewPager(viewPager).
                setTabPadding(10, 10, 10, 10).
                setTabTextAppearance(R.style.PopTabText).
                setTabBackgroundResId(R.drawable.ripple).
                setTabIndicatorColor(Color.RED).
                setIconFetcher(this).
                setIconDimension(50).
                setDefaultIconColor(Color.WHITE);
        tabLayout.setUpWithAdapter(builder.build());
        return view;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        {
            @NonNull
            @Override
            public Fragment getItem(int position)
            {
                Fragment fragment = new Fragment();
                switch (position)
                {
                    case 0:
                        fragment = new Practical_Manuals();
                        break;
                    case 1:
                        fragment = new Practical_Slips();
                        break;
                    case 2:
                        fragment =  new Practical_Tools();
                        break;
                    case 3:
                        fragment =  new Theory_Videos();
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount()
            {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                String title = "";
                switch (position)
                {
                    case 0:
                        title = "MANUAL";
                        break;
                    case 1:
                        title = "TEXT";
                        break;
                    case 2:
                        title = "TOOLS";
                        break;
                    case 3:
                        title = "VIDEOS";
                        break;
                }
                return title;
            }
        });

    }

    @Override
    public int getIcon(int position)
    {
        int resource = R.drawable.sem06;
        switch (position)
        {
            case 0:
                resource = R.drawable.ic_baseline_book_24;
                break;
            case 1:
                resource = R.drawable.ic_baseline_speaker_notes_24;
                break;
            case 2:
                resource = R.drawable.ic_baseline_settings_24;
                break;
            case 3:
                resource = R.drawable.ic_baseline_ondemand_video_24;
                break;
        }
        return resource;
    }
}
