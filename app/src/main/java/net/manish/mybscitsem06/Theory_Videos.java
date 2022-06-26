package net.manish.mybscitsem06;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Theory_Videos extends Fragment implements View.OnClickListener
{
    final Help help = new Help();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_theory__videos, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
        view.findViewById(R.id.EJ).setOnClickListener(this);
        view.findViewById(R.id.NGT).setOnClickListener(this);
        return view;
    }
    public void process(String url)
    {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null && help.getInfo("NAME", requireActivity())!=null && help.getInfo("EMAIL", requireActivity())!=null && help.getInfo("PHONE", requireActivity())!=null && help.getInfo("PASSWORD", requireActivity())!=null && help.getInfo("STATE", requireActivity())!=null)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            Animatoo.animateDiagonal(requireActivity());
        }
        else
        {
            FancyToast.makeText(requireActivity(), "SORRY! YOU CAN'T ACCESS THIS STUDY MATERIAL AS YOU ARE NOT A PREMIUM MEMBER. KINDLY PURCHASE THE MEMBERSHIP TO AVAIL THE BENEFITS.",FancyToast.LENGTH_SHORT,FancyToast.ERROR, false).show();
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
            process("https://www.youtube.com/playlist?list=PLy9U5GDpYZVPYwx2SBmxsFODDnBnsfG9w");
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            process("https://www.youtube.com/playlist?list=PLYwpaL_SFmcArHtWmbs_vXX6soTK3WEJw");
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            process("https://www.youtube.com/playlist?list=PLPN-43XehstOe0CxcXaYeLTFpgD2IiluP");
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            process("https://www.youtube.com/playlist?list=PL3MO67NH2XxLAFn3jc7gOhXLD9YFx-oew");
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            process("https://www.youtube.com/playlist?list=PLZhINA5ev6sDj7_RBtlDenCSDSpjqkshL");
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            process("https://www.youtube.com/playlist?list=PLB54CED506CFECDC3");
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            process("https://www.youtube.com/playlist?list=PLB54CED506CFECDC3");
        }
    }
}