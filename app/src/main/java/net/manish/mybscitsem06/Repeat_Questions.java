package net.manish.mybscitsem06;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Repeat_Questions extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_theory__questions, container, false);
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
            new Theory_Books().process("SQA - MCQ.PDF","601-MCQ-PDF","601-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            new Theory_Books().process("SIC - MCQ.PDF","602-MCQ-PDF","602-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            new Theory_Books().process("BI - MCQ.PDF","603-MCQ-PDF","603-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            new Theory_Books().process("PGIS - MCQ.PDF","604-MCQ-PDF","604-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("EN - MCQ.PDF","605-MCQ-PDF","605-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            new Theory_Books().process("ITSM - MCQ.PDF","606-MCQ-PDF","606-MCQ-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            new Theory_Books().process("CL - MCQ.PDF","607-MCQ-PDF","607-MCQ-PASS", true, requireActivity());
        }
    }
}