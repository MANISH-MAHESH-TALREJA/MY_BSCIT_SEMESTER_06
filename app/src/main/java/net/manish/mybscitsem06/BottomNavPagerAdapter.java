package net.manish.mybscitsem06;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BottomNavPagerAdapter extends FragmentPagerAdapter
{

    private final List<Fragment> mFragmentList = new ArrayList<>();

    BottomNavPagerAdapter(FragmentManager manager)
    {
        super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    void addFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }
}
