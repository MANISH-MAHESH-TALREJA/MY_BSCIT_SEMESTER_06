package net.manish.mybscitsem06;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Theory_Notes extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_theory__notes, container, false);
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
            new Theory_Books().process("SQA - NOTES.PDF","601-NOTES-PDF","601-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            new Theory_Books().process("SIC - NOTES.PDF","602-NOTES-PDF","602-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            new Theory_Books().process("BI - NOTES.PDF","603-NOTES-PDF","603-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            new Theory_Books().process("PGIS - NOTES.PDF","604-NOTES-PDF","604-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("EN - NOTES.PDF","605-NOTES-PDF","605-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            new Theory_Books().process("ITSM - NOTES.PDF","606-NOTES-PDF","606-NOTES-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            new Theory_Books().process("CL - NOTES.PDF","607-NOTES-PDF","607-NOTES-PASS", true, requireActivity());
        }
    }
}