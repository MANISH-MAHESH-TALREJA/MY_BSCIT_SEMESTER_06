package net.manish.mybscitsem06;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.Objects;

public class Theory_Books extends Fragment implements View.OnClickListener
{
    Help help = new Help();
    final String pro = help.purchase("UFJFTUlVTQ==");
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_theory__books, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
        view.findViewById(R.id.EJ).setOnClickListener(this);
        view.findViewById(R.id.NGT).setOnClickListener(this);
        return view;
    }

    public void process(String file, String link, String pass, boolean premium,Activity activity)
    {
        help = new Help();
        if(premium)
        {
            if(Objects.equals(help.getInfo("STATE", activity), pro) && FirebaseAuth.getInstance().getCurrentUser()!=null && help.getInfo("NAME", activity)!=null && help.getInfo("EMAIL", activity)!=null && help.getInfo("PHONE", activity)!=null && help.getInfo("PASSWORD", activity)!=null && help.getInfo("STATE", activity)!=null)
            {
                new ViewPDF().process(activity,file,link,pass);
            }
            else
            {
                FancyToast.makeText(activity, "SORRY! YOU CAN'T ACCESS THIS STUDY MATERIAL AS YOU ARE NOT A PREMIUM MEMBER. KINDLY PURCHASE THE MEMBERSHIP TO AVAIL THE BENEFITS.",FancyToast.LENGTH_SHORT,FancyToast.ERROR, false).show();
            }
        }
        else
        {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null && help.getInfo("NAME", activity)!=null && help.getInfo("EMAIL", activity)!=null && help.getInfo("PHONE", activity)!=null && help.getInfo("PASSWORD", activity)!=null && help.getInfo("STATE", activity)!=null)
            {
                new ViewPDF().process(activity,file,link,pass);
            }
            else
            {
                FancyToast.makeText(activity, "SORRY! YOU CAN'T ACCESS THIS STUDY MATERIAL AS YOU ARE NOT A PREMIUM MEMBER. KINDLY PURCHASE THE MEMBERSHIP TO AVAIL THE BENEFITS.",FancyToast.LENGTH_SHORT,FancyToast.ERROR, false).show();
            }
        }
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
            process("SQA - BOOK.PDF","601-BOOK-PDF","601-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            process("SIC - BOOK.PDF","602-BOOK-PDF","602-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            process("BI - BOOK.PDF","603-BOOK-PDF","603-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            process("PGIS - BOOK.PDF","604-BOOK-PDF","604-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            process("EN - BOOK.PDF","605-BOOK-PDF","605-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            process("ITSM - BOOK.PDF","606-BOOK-PDF","606-BOOK-PASS", false, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            process("CL - BOOK.PDF","607-BOOK-PDF","607-BOOK-PASS", false, requireActivity());
        }
    }
}