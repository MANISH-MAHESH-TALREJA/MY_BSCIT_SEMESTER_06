package net.manish.mybscitsem06;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Practical_Manuals extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_practical__manuals, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
        view.findViewById(R.id.EJ).setOnClickListener(this);
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
            new Theory_Books().process("BLACK BOOK.PDF","6P1-MANUAL-PDF","6P1-MANUAL-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            new Theory_Books().process("SIC - MANUAL.PDF","6P2-MANUAL-PDF","6P2-MANUAL-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            new Theory_Books().process("BI - MANUAL.PDF","6P3-MANUAL-PDF","6P3-MANUAL-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            new Theory_Books().process("PGIS - MANUAL.PDF","6P4-MANUAL-PDF","6P4-MANUAL-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("EN - MANUAL.PDF","6P5-MANUAL-PDF","6P5-MANUAL-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            new Theory_Books().process("AMP - MANUAL.PDF","6P6-MANUAL-PDF","6P6-MANUAL-PASS", false, requireActivity());
        }
    }
}