package net.manish.mybscitsem06;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Practical_Slips extends Fragment implements  View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_practical__slips, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
        view.findViewById(R.id.EJ).setOnClickListener(this);
        view.findViewById(R.id.NGT).setOnClickListener(this);
        return view;
    }
    private long mLastClickTime = 0;
    @Override
    public void onClick(View myView)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(myView  == view.findViewById(R.id.SPM))
        {
            new Theory_Books().process("SQA - TEXT.PDF","601-TEXT-PDF","601-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            new Theory_Books().process("SIC - TEXT.PDF","602-TEXT-PDF","602-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            new Theory_Books().process("BI - TEXT.PDF","603-TEXT-PDF","603-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            new Theory_Books().process("PGIS - TEXT.PDF","604-TEXT-PDF","604-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("EN - TEXT.PDF","605-TEXT-PDF","605-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            new Theory_Books().process("ITSM - TEXT.PDF","606-TEXT-PDF","606-TEXT-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            new Theory_Books().process("CL - TEXT.PDF","607-TEXT-PDF","607-TEXT-PASS", true, requireActivity());
        }
    }
}