package net.manish.mybscitsem06;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;

public class RequestContent extends Fragment implements View.OnClickListener
{
    View view;
    String mName, mDescription;
    long mLastClickTime = 0;
    EditText name, description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_request_content, container, false);
        view.findViewById(R.id.requestBtn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View myView) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(myView == view.findViewById(R.id.requestBtn))
        {
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            mName = name.getText().toString();
            mDescription = description.getText().toString();
            if (TextUtils.isEmpty(mName))
            {
                name.setError("NAME CANNOT BE EMPTY");
                return;
            }
            if (TextUtils.isEmpty(mDescription))
            {
                description.setError("DESCRIPTION NAME CANNOT BE EMPTY");
                return;
            }
            String message="HELLO PROF. MANISH MAHESH TALREJA,\n\tI "+new Help().getInfo("NAME", requireActivity())+" WOULD LIKE TO REQUEST YOU FOR THE DOCUMENT NAMED AS "+mName.toUpperCase()+". SIR, HERE IS A SHORT DESCRIPTION OF IT : "+mDescription.toUpperCase()+". SIR I WILL BE THANKFUL IF THE CONTENT IS PROVIDED IN THE APP.\n\nTHANKING YOU SIR\nYOUR'S TRULY\n"+new Help().getInfo("NAME", requireActivity())+"\n\nSIR YOU CAN CONTACT ME ON "+new Help().getInfo("PHONE", requireActivity());
            String[] TO = {"manishtalreja189@gmail.com"};
            String[] CC = {""};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "REQUEST FOR CONTENT");
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            try
            {
                startActivity(Intent.createChooser(emailIntent, "SEND REQUEST MAIL TO MANISH MAHESH TALREJA"));
                Animatoo.animateDiagonal(requireActivity());
            }
            catch (android.content.ActivityNotFoundException ex)
            {
                FancyToast.makeText(requireActivity(), "THERE IS NO EMAIL CLIENT INSTALLED", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        }
    }
}
