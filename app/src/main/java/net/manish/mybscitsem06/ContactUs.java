package net.manish.mybscitsem06;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ContactUs extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        view.findViewById(R.id.classroom).setOnClickListener(this);
        view.findViewById(R.id.console).setOnClickListener(this);
        view.findViewById(R.id.telegram).setOnClickListener(this);
        view.findViewById(R.id.youtube).setOnClickListener(this);
        view.findViewById(R.id.instagram).setOnClickListener(this);
        view.findViewById(R.id.gmail).setOnClickListener(this);
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
        if(myView  == view.findViewById(R.id.classroom))
        {
            FancyToast.makeText(requireActivity(), "CLASSROOM CODE : WSTKVX4",FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.classroom.google.com")));
            Animatoo.animateDiagonal(requireActivity());
        }
        else if(myView  == view.findViewById(R.id.console))
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=MANISH+MAHESH+TALREJA")));
            Animatoo.animateDiagonal(requireActivity());
        }
        if(myView  == view.findViewById(R.id.telegram))
        {
            Intent i= new Intent(Intent.ACTION_VIEW,Uri.parse("http://t.me/manishtalreja189"));
            i.setPackage("org.telegram.messenger");
            try
            {
                startActivity(i);
                Animatoo.animateDiagonal(requireActivity());
            }
            catch (ActivityNotFoundException e)
            {
                FancyToast.makeText(requireActivity(), "SORRY, TELEGRAM APP IS NOT INSTALLED ON YOUR MOBILE",FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show();
            }
        }
        else if(myView  == view.findViewById(R.id.youtube))
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC7vfVNEm4Nh9AfM6rTv2oYw")));
            Animatoo.animateDiagonal(requireActivity());
        }
        if(myView  == view.findViewById(R.id.instagram))
        {
            Intent i= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/manish_talreja189"));
            i.setPackage("com.instagram.android");
            try
            {
                startActivity(i);
                Animatoo.animateDiagonal(requireActivity());
            }
            catch (ActivityNotFoundException e)
            {
                FancyToast.makeText(requireActivity(), "SORRY, INSTAGRAM APP IS NOT INSTALLED ON YOUR MOBILE",FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/manish_talreja189"));
                startActivity(intent);
                Animatoo.animateDiagonal(requireActivity());
            }
        }
        else if(myView  == view.findViewById(R.id.gmail))
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            String[] TO = {"manishtalreja189@gmail.com"};
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CONTACT PROF. MANISH MAHESH TALREJA");
            emailIntent.setPackage("com.google.android.gm");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "HELLO RESPECTED MANISH SIR,\n");
            try
            {
                startActivity(Intent.createChooser(emailIntent, "CONTACT VIA MAIL TO MANISH MAHESH TALREJA"));
                Animatoo.animateDiagonal(requireActivity());
            }
            catch (ActivityNotFoundException ex)
            {
                FancyToast.makeText(requireActivity(), "THERE IS NO EMAIL CLIENT INSTALLED",FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show();
            }
        }
    }
}
