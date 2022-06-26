package net.manish.mybscitsem06;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import androidx.core.content.res.ResourcesCompat;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.huxq17.download.Pump;
import com.huxq17.download.core.DownloadListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;
import cc.cloudist.acplibrary.ACProgressBaseDialog;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.Request;

@SuppressWarnings("ALL")
public class ViewPDF
{
    String mFile, mLink, mPass;
    String storeFile, storePassword;
    Context mContext;
    String pro;
    ACProgressBaseDialog progress;
    ProgressDialog progressDialog;
    public void process(final Context context, final String file, final String link, final String pass)
    {
        final Help help = new Help();
        pro = help.purchase("UFJFTUlVTQ==");
        mContext = context;
        mFile = file;
        mLink = link;
        mPass = pass;
        Typeface face = ResourcesCompat.getFont(context,R.font.syndor);
        progress = new ACProgressFlower.Builder(context).direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.WHITE).text("PLEASE WAIT").fadeColor(Color.DKGRAY).build();
        progressDialog = new ProgressDialog(context);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(help.typeface(face, file));
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.sem06);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (dialog, which) ->
        {
            progressDialog.dismiss();Pump.deleteById("02062000");
            ((Activity) mContext).runOnUiThread(() -> ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (new File(String.valueOf(Uri.parse(mContext.getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/" + file))).exists())
        {
            Intent i = new Intent();
            i.putExtra("NAME", file);
            i.putExtra("FILE",String.valueOf(Uri.parse(mContext.getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/" + file)));
            i.putExtra("DATA",help.purchase(help.getSecret(pass, context)));
            i.setClass(context, PdfActivity.class);
            context.startActivity(i);
            Animatoo.animateDiagonal(context);
        }
        else
        {
            if(new SampleActivity().isInternetAvailable(context))
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && help.getInfo("EMAIL", context) != null && help.getInfo("PASSWORD", context) != null && help.getInfo("PHONE", context) != null && help.getInfo("STATE", context)!=null && help.getInfo("NAME", context) != null)
                {
                    new ProcessPDF(ViewPDF.this).execute();
                }
                else
                {
                    FancyToast.makeText(context, "THERE WAS SOME TECHNICAL ISSUE WHILE HANDLING YOUR DATA. KINDLY RE-LOGIN AND TRY AGAIN", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
            }
            else
            {
                FancyToast.makeText(context, "INTERNET IS NOT AVAILABLE NOR FILE IS DOWNLOADED FOR OFFLINE MODE.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        }
    }


    public static class ProcessPDF extends AsyncTask<Void, Void, Void>
    {
        private final WeakReference<ViewPDF> activityReference;
        Help help = new Help();
        ProcessPDF(ViewPDF context)
        {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute()
        {
            ViewPDF activity = activityReference.get();
            ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED));
            super.onPreExecute();
            activity.progress.show();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            final ViewPDF activity = activityReference.get();
            try
            {
                FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
                final DocumentReference documentReference1 = firebaseFirestore1.collection("STUDENTS").document(help.getInfo("EMAIL", activity.mContext));
                documentReference1.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        final DocumentSnapshot documentSnapshot1 = task.getResult();
                        assert  documentSnapshot1!=null;
                        if(documentSnapshot1.exists())
                        {
                            documentReference1.addSnapshotListener((value, e) ->
                            {
                                    if (documentSnapshot1.get("STATE") != null && documentSnapshot1.get("PHONE") != null && documentSnapshot1.get("PASSWORD") != null && documentSnapshot1.get("DEVICE") != null)
                                    {
                                        assert value != null;
                                        String state, androidId, myDeviceId;
                                        state = value.getString("STATE");
                                        androidId = value.getString("DEVICE");
                                        help.saveInfo("DEVICE", androidId, activity.mContext);
                                        help.saveInfo("STATE", state, activity.mContext);
                                        myDeviceId= Settings.Secure.getString(activity.mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                                        if(Objects.equals(androidId,myDeviceId))
                                        {
                                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                            final DocumentReference documentReference = firebaseFirestore.collection("SEMESTER 06").document("DATA");
                                            documentReference.get().addOnCompleteListener(task1 ->
                                            {
                                                if(task1.isSuccessful())
                                                {
                                                    final DocumentSnapshot document = task1.getResult();
                                                    assert document != null;
                                                    if (document.exists())
                                                    {
                                                        documentReference.addSnapshotListener((documentSnapshot, ee) ->
                                                        {
                                                            if (document.get(activity.mLink) != null && document.get(activity.mPass) != null)
                                                            {
                                                                assert documentSnapshot != null;
                                                                activity.storeFile = documentSnapshot.getString(activity.mLink);
                                                                activity.storePassword = documentSnapshot.getString(activity.mPass);
                                                                help.saveData(activity.mLink,activity.storeFile, activity.mContext);
                                                                help.saveSecret(activity.mPass,activity.storePassword, activity.mContext);
                                                                if(activity.storeFile!=null && activity.storePassword!=null)
                                                                {
                                                                    activity.progress.dismiss();
                                                                    if(activity!=null)
                                                                    {
                                                                        activity.progressDialog.show();
                                                                        String downloadUrl = help.getData(activity.mLink,activity.mContext);
                                                                        Pump.newRequest(downloadUrl, activity.mContext.getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/" + activity.mFile).listener(new DownloadListener()
                                                                        {
                                                                            @Override
                                                                            public void onProgress(final int progress)
                                                                            {
                                                                                ((Activity)activity.mContext).runOnUiThread(() -> activity.progressDialog.setProgress(progress));
                                                                            }

                                                                            @Override
                                                                            public void onSuccess()
                                                                            {
                                                                                activity.progressDialog.dismiss();
                                                                                help.saveDownload(activity.mFile,activity.storePassword,activity.mContext);
                                                                                Intent i = new Intent();
                                                                                i.putExtra("NAME",activity.mFile);
                                                                                i.putExtra("FILE",String.valueOf(Uri.parse(activity.mContext.getApplicationInfo().dataDir+"/MY-BSCIT(SEM-06)/" + activity.mFile)));
                                                                                i.putExtra("DATA",help.purchase(help.getSecret(activity.mPass, activity.mContext)));
                                                                                i.setClass(activity.mContext, PdfActivity.class);
                                                                                activity.mContext.startActivity(i);
                                                                                Animatoo.animateDiagonal(activity.mContext);
                                                                                FancyToast.makeText(activity.mContext, "DOWNLOAD FINISHED", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                                                ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                                                            }

                                                                            @Override
                                                                            public void onFailed()
                                                                            {
                                                                                activity.progressDialog.dismiss();
                                                                                ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                                                                FancyToast.makeText(activity.mContext, "DOWNLOAD FAILED", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                            }
                                                                        }).forceReDownload(true)
                                                                                .threadNum(1)
                                                                                .setRequestBuilder(new Request.Builder())
                                                                                .setRetry(3, 200)
                                                                                .setId("02062000")
                                                                                .submit();
                                                                    }
                                                                }
                                                            }
                                                            else
                                                            {
                                                                ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                                                FancyToast.makeText(activity.mContext, "CONTENT IS CURRENTLY UNAVAILABLE, IT WILL BE AVALABLE SHORTLY", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                                                activity.progress.dismiss();
                                                            }
                                                        });
                                                    }
                                                }
                                                else
                                                {
                                                    ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                                    FancyToast.makeText(activity.mContext, "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                                    activity.progress.dismiss();
                                                }
                                            }).addOnFailureListener(eee -> {
                                                ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                                FancyToast.makeText(activity.mContext, "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                                activity.progress.dismiss();
                                            });
                                        }
                                        else
                                        {
                                            ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                            FancyToast.makeText(activity.mContext, "YOUR DEVICE DOESNOT MATCH WITH OUR RECORDS OR YOU ARE NOT A PREMIUM MEMBER.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                            activity.progress.dismiss();
                                        }
                                    }
                                    else
                                    {
                                        ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                                        FancyToast.makeText(activity.mContext, "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                        activity.progress.dismiss();
                                    }
                                });
                            }
                        else
                        {
                            ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                            FancyToast.makeText(activity.mContext, "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            activity.progress.dismiss();
                        }
                        }
                    else
                    {
                        ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                        FancyToast.makeText(activity.mContext, "FAILED TO FETCH DATA", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                        activity.progress.dismiss();
                    }
                    });
                }
            catch (Exception e)
            {
                activity.progress.dismiss();
                ((Activity)activity.mContext).runOnUiThread(() -> ((Activity) activity.mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                FancyToast.makeText(activity.mContext, "SOME PROBLEM OCCURED", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
            return null;
        }
    }


}
