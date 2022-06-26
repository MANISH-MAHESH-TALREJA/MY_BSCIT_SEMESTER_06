package net.manish.mybscitsem06;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import yanzhikai.textpath.PathView;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.PenPainter;

public class Dashboard extends Fragment implements View.OnClickListener
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            SyncTextPathView welcome = view.findViewById(R.id.welcomeText);
            welcome.setPathPainter(new PenPainter());
            welcome.setFillColor(true);
            welcome.startAnimation(0,1, PathView.REVERSE,500);
        }
        view.findViewById(R.id.quiz).setOnClickListener(this);
        view.findViewById(R.id.notice).setOnClickListener(this);
        view.findViewById(R.id.download).setOnClickListener(this);
        view.findViewById(R.id.pdf).setOnClickListener(this);
        return view;
    }

    long mLastClickTime = 0;
    @Override
    public void onClick(View myView)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(myView==view.findViewById(R.id.quiz))
        {
            if(new SampleActivity().isInternetAvailable(requireActivity()))
            {
                startActivity(new Intent(requireActivity(),SubjectsActivity.class));
                Animatoo.animateDiagonal(requireActivity());
            }
            else
            {
                FancyToast.makeText(requireActivity(), "INTERNET CONNECTION UNAVAILABLE",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
            }
        }
        if(myView == view.findViewById(R.id.notice))
        {
            if(new SampleActivity().isInternetAvailable(requireActivity()))
            {
                startActivity(new Intent(requireActivity(),Blogger.class));
                Animatoo.animateDiagonal(requireActivity());
            }
            else
            {
                FancyToast.makeText(requireActivity(), "INTERNET CONNECTION UNAVAILABLE",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
            }
        }
        if(myView==view.findViewById(R.id.download))
        {
            List<FilesModel> itemsModelList = new ArrayList<>();
            File dir = new File(requireActivity().getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/");
            File[] fileList = dir.listFiles();
            String[] files;
            if(fileList!=null)
            {
                if(fileList.length>0)
                {
                    files = new String[fileList.length];
                    for (int i = 0; i < files.length; i++)
                    {
                        files[i] = fileList[i].getName();
                    }
                    for (String s : files)
                    {
                        if(!s.startsWith(".") || s.contains(".temp"))
                        {
                            FilesModel itemsModel = new FilesModel(s);
                            if(itemsModel.getName().startsWith(".") || itemsModel.getName().contains(".temp"))
                            {
                                continue;
                            }
                            itemsModelList.add(itemsModel);
                        }
                    }
                }
                if(itemsModelList.size()>0)
                {
                    startActivity(new Intent(requireActivity(), Notices.class));
                    Animatoo.animateDiagonal(requireActivity());
                }
                else
                {
                    FancyToast.makeText(requireActivity(), "NO DOWNLOADED FILES", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
            }

            else
            {
                FancyToast.makeText(requireActivity(), "NO DOWNLOADED FILES", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        }
        if(myView == view.findViewById(R.id.pdf))
        {
            startActivity(new Intent(requireActivity(), TabMainActivity.class));
            Animatoo.animateDiagonal(requireActivity());
        }

    }
}

