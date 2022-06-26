package net.manish.mybscitsem06;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class Practical_Tools extends Fragment implements View.OnClickListener
{
    View view;
    final Help help = new Help();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_practical__tools, container, false);
        view.findViewById(R.id.SPM).setOnClickListener(this);
        view.findViewById(R.id.IOT).setOnClickListener(this);
        view.findViewById(R.id.AWP).setOnClickListener(this);
        view.findViewById(R.id.AI).setOnClickListener(this);
        view.findViewById(R.id.LSA).setOnClickListener(this);
        view.findViewById(R.id.EJ).setOnClickListener(this);
        view.findViewById(R.id.NGT).setOnClickListener(this);
        view.findViewById(R.id.EXTRA).setOnClickListener(this);
        return view;
    }
    public void process(String message, String url, String subject, String text, int image)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && help.getInfo("NAME", requireActivity()) != null && help.getInfo("EMAIL", requireActivity()) != null && help.getInfo("PHONE", requireActivity()) != null && help.getInfo("PASSWORD", requireActivity()) != null && help.getInfo("STATE", requireActivity()) != null) {
            Typeface face = ResourcesCompat.getFont(requireActivity(), R.font.syndor);
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
            alertDialog.setTitle(help.typeface(face, "DOWNLOAD APPLICATION"));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(message);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "DOWNLOAD", (dialog, which) ->
            {
                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                Animatoo.animateDiagonal(requireActivity());
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "EMAIL", (dialog, which) ->
            {
                dialog.dismiss();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] TO = new String[]{help.getInfo("EMAIL", requireActivity())};
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.setPackage("com.google.android.gm");
                emailIntent.putExtra(Intent.EXTRA_TEXT, text);
                try {
                    startActivity(Intent.createChooser(emailIntent, "SEND DOWNLOAD LINK"));
                    Animatoo.animateDiagonal(requireActivity());
                } catch (ActivityNotFoundException ex)
                {
                    FancyToast.makeText(requireActivity(), "THERE IS NO EMAIL CLIENT INSTALLED", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
            alertDialog.setIcon(image);
            alertDialog.show();
            TextView textView = Objects.requireNonNull(alertDialog.getWindow()).findViewById(android.R.id.message);
            Button button1 = alertDialog.getWindow().findViewById(android.R.id.button1);
            Button button2 = alertDialog.getWindow().findViewById(android.R.id.button2);
            Button button3 = alertDialog.getWindow().findViewById(android.R.id.button3);
            textView.setTypeface(face, Typeface.BOLD);
            button1.setTypeface(face, Typeface.BOLD);
            button2.setTypeface(face, Typeface.BOLD);
            button3.setTypeface(face, Typeface.BOLD);
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
            process("YOU ARE ABOUT TO DOWNLOAD CISCO PACKET TRACER. KINDLY CHECK THE DETAILS BELOW:\n\n APP : CISCO PACKET TRACER\n\n SIZE : 148.33 MB\n\n VERSION : 7.3.0\n\n\nCLICKING BELOW WILL START DOWNLOADING THE SOFTWARE. YOU CAN ALSO EMAIL THE DOWNLOAD LINK OF THE SOFTWARE BY CLICKING ON EMAIL BUTTON.","https://www.filehorse.com/download-cisco-packet-tracer-64/","DOWNLOAD LINK FOR CISCO PACKET TRACER", "YOU CAN DOWNLOAD CISCO PACKET TRACER VERSION 7.3.0 OF SIZE 148.33 MB (64-BIT) BY CLICKING ON BELOW LINK.\nhttps://www.filehorse.com/download-cisco-packet-tracer-64/", R.drawable.sic_tool);
        }
        else if(myView  == view.findViewById(R.id.IOT))
        {
            process("YOU ARE ABOUT TO DOWNLOAD MICROSOFT POWER BI. KINDLY CHECK THE DETAILS BELOW:\n\n APP : MICROSOFT POWER BI\n\n SIZE : 328 MB\n\n VERSION :  2.88.1385.0\n\n\nCLICKING BELOW WILL START DOWNLOADING THE SOFTWARE. YOU CAN ALSO EMAIL THE DOWNLOAD LINK OF THE SOFTWARE BY CLICKING ON EMAIL BUTTON.","https://www.filehorse.com/download-microsoft-power-bi-desktop-64/","DOWNLOAD LINK FOR MICROSOFT POWER BI","YOU CAN DOWNLOAD MICROSOFT POWER BI VERSION 2.79.5768.663 OF SIZE 299 MB (64-BIT) BY CLICKING ON BELOW LINK.\nhttps://www.filehorse.com/download-microsoft-power-bi-desktop-64/",R.drawable.bi_tool);
        }
        else if(myView  == view.findViewById(R.id.AWP))
        {
            process("YOU ARE ABOUT TO DOWNLOAD QUANTUM GIS (QGIS). KINDLY CHECK THE DETAILS BELOW:\n\n APP : QUANTUM GIS (QGIS)\n\n SIZE : 390 MB\n\n VERSION : 3.16.3\n\n\nCLICKING BELOW WILL START DOWNLOADING THE SOFTWARE. YOU CAN ALSO EMAIL THE DOWNLOAD LINK OF THE SOFTWARE BY CLICKING ON EMAIL BUTTON.","https://www.filehorse.com/download-qgis-64/","DOWNLOAD LINK FOR QUANTUM GIS (QGIS)","YOU CAN DOWNLOAD QUANTUM GIS (QGIS) VERSION 3.12.2 OF SIZE 413 MB (64-BIT) BY CLICKING ON BELOW LINK.\nhttps://www.filehorse.com/download-qgis-64/",R.drawable.pgis_tool);
        }
        else if(myView  == view.findViewById(R.id.AI))
        {
            process("YOU ARE ABOUT TO DOWNLOAD ANDROID STUDIO 4.1.2. KINDLY CHECK THE DETAILS BELOW:\n\n APP : ANDROID STUDIO\n\n SIZE : 896 MB\n\n VERSION : 4.1.2\n\n\nCLICKING BELOW WILL START DOWNLOADING THE SOFTWARE. YOU CAN ALSO EMAIL THE DOWNLOAD LINK OF THE SOFTWARE BY CLICKING ON EMAIL BUTTON.","https://redirector.gvt1.com/edgedl/android/studio/install/4.1.2.0/android-studio-ide-201.7042882-windows.exe","DOWNLOAD LINK FOR ANDROID STUDIO 4.0.1","YOU CAN DOWNLOAD ANDROID STUDIO VERSION 4.0.1 OF SIZE 871 MB (64-BIT) BY CLICKING ON BELOW LINK.\nhttps://redirector.gvt1.com/edgedl/android/studio/install/4.1.2.0/android-studio-ide-201.7042882-windows.exe",R.drawable.amp);
        }
        else if(myView  == view.findViewById(R.id.LSA))
        {
            new Theory_Books().process("SIC - VIVA.PDF","6P2-VIVA-PDF","6P2-VIVA-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EJ))
        {
            new Theory_Books().process("BI - VIVA.PDF","6P3-VIVA-PDF","6P3-VIVA-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.NGT))
        {
            new Theory_Books().process("PGIS - VIVA.PDF","6P4-VIVA-PDF","6P4-VIVA-PASS", true, requireActivity());
        }
        else if(myView  == view.findViewById(R.id.EXTRA))
        {
            new Theory_Books().process("AMP - VIVA.PDF","6P6-VIVA-PDF","6P6-VIVA-PASS", true, requireActivity());
        }
    }
}