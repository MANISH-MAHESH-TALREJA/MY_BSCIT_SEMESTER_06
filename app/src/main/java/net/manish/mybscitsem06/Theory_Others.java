package net.manish.mybscitsem06;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Theory_Others extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_theory__others, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
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
            new Theory_Books().process("KEY - MAY 2019.PDF","KEY-MAY19-PDF","FREEMIUM", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            new Theory_Books().process("QUESTION BANKS.PDF","QB-PDF","FREEMIUM", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            new Theory_Books().process("QP - APRIL 2019.PDF","QP-APR19-PDF","FREEMIUM", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            new Theory_Books().process("QP - NOVEMBER 2019.PDF","QP-NOV19-PDF","FREEMIUM", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("TYBSCIT SYLLABUS.PDF","SYLLABUS-PDF","FREEMIUM", false, requireActivity());
        }
    }
}