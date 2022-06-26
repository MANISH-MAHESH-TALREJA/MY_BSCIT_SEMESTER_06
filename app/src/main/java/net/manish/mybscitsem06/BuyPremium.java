package net.manish.mybscitsem06;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import cc.cloudist.acplibrary.ACProgressBaseDialog;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class BuyPremium extends Fragment
{
    long mLastClickTime = 0;
    String amount, merchant, verify, checksum;
    ACProgressBaseDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_buy_premium, container, false);
        Button premium = view.findViewById(R.id.buyPremium);
        progress = new ACProgressFlower.Builder(requireActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("PLEASE WAIT")
                .fadeColor(Color.DKGRAY).build();
        premium.setOnClickListener(v ->
        {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000)
            {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            Typeface face = ResourcesCompat.getFont(requireActivity(),R.font.syndor);
            builder.setMessage("YOU ARE ABOUT TO BUY THE PREMIUM FEATURE OF MY-BSCIT FOR (SEMESTER 06) FOR ACADEMIC YEAR 2020-2021. PROCEED TO PAY RS. 150")
                    .setCancelable(false)
                    .setIcon(R.drawable.emoji)
                    .setTitle(new Help().typeface(face, "PAYMENT INFO"))
                    .setPositiveButton( "PAY (RS. 150)", (dialog, id) -> {
                        Help help = new Help();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && help.getInfo("EMAIL", requireActivity()) != null && help.getInfo("PASSWORD", requireActivity()) != null && help.getInfo("PHONE", requireActivity()) != null && help.getInfo("STATE", requireActivity()) != null && help.getInfo("NAME", requireActivity()) != null)
                        {
                            if(new SampleActivity().isInternetAvailable(requireActivity()))
                            {
                                new Thread(()->{
                                    Looper.prepare();
                                    try
                                    {
                                        requireActivity().runOnUiThread(() ->
                                        {
                                            progress.show();
                                            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                                        });
                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        final DocumentReference documentReference = firebaseFirestore.collection("PAYMENTS").document("PAYTM");
                                        documentReference.get().addOnCompleteListener(task -> {
                                            if(task.isSuccessful())
                                            {
                                                final DocumentSnapshot document = task.getResult();
                                                assert document != null;
                                                if (document.exists())
                                                {
                                                    documentReference.addSnapshotListener((documentSnapshot, e) -> {
                                                        if (document.get("AMOUNT") != null && document.get("CHECKSUM") != null && document.get("MERCHANT") != null && document.get("VERIFY") != null)
                                                        {
                                                            assert documentSnapshot != null;
                                                            amount = documentSnapshot.getString("AMOUNT");
                                                            merchant = documentSnapshot.getString("MERCHANT");
                                                            checksum = documentSnapshot.getString("CHECKSUM");
                                                            verify = documentSnapshot.getString("VERIFY");
                                                            if(amount!=null && merchant!=null && checksum!=null && verify!=null)
                                                            {

                                                                Intent intent = new Intent(requireActivity(), Checksum.class);
                                                                intent.putExtra("AMOUNT", amount);
                                                                intent.putExtra("MERCHANT", merchant);
                                                                intent.putExtra("CHECKSUM", checksum);
                                                                intent.putExtra("VERIFY",  verify);
                                                                startActivity(intent);
                                                                Animatoo.animateDiagonal(requireActivity());
                                                                requireActivity().runOnUiThread(() ->
                                                                {
                                                                    progress.dismiss();
                                                                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                                });
                                                            }
                                                        }
                                                        else
                                                        {
                                                            requireActivity().runOnUiThread(() ->
                                                            {
                                                                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                                FancyToast.makeText(requireActivity(), "FAILED TO FETCH DATA",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                                                                progress.dismiss();
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                requireActivity().runOnUiThread(() ->
                                                {
                                                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                                    FancyToast.makeText(requireActivity(), "FAILED TO FETCH DATA",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                                                    progress.dismiss();
                                                });
                                            }
                                        }).addOnFailureListener(e -> requireActivity().runOnUiThread(() ->
                                        {
                                            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                            FancyToast.makeText(requireActivity(), "FAILED TO FETCH DATA",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                                            progress.dismiss();
                                        }));
                                    }
                                    catch (Exception e)
                                    {
                                        requireActivity().runOnUiThread(() ->
                                        {
                                            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                            FancyToast.makeText(requireActivity(), "SOME ERROR OCCURRED",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                                            progress.dismiss();
                                        });
                                    }
                                }).start();
                            }
                            else
                            {
                                FancyToast.makeText(requireActivity(), "NO ACTIVE INTERNET CONNECTION",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                            }
                        }
                        else
                        {
                            FancyToast.makeText(requireActivity(), "THERE WAS SOME TECHNICAL ISSUE WHILE HANDLING YOUR DATA. KINDLY RE-LOGIN AND TRY AGAIN",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show();
                        }

                    }).setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        return view;
    }
}
