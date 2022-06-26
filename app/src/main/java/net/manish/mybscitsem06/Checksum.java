package net.manish.mybscitsem06;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback
{
    String customerID, orderID, merchantID, email, amount, url, verify;
    Help help;
    String CHECKSUM ="";
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        help = new Help();
        dialog = new ProgressDialog(Checksum.this);
        orderID = DateFormat.getDateTimeInstance().format(new Date()).replace(",", "").replace(" ", "").replace(":", "").toUpperCase();
        customerID = help.getInfo("PHONE", Checksum.this) + new SimpleDateFormat("MMddHHmmss",Locale.getDefault()).format(Calendar.getInstance().getTime());
        email = help.getInfo("EMAIL", Checksum.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Intent intent = getIntent();
        verify = intent.getStringExtra("VERIFY");
        amount = intent.getStringExtra("AMOUNT");
        url = intent.getStringExtra("CHECKSUM");
        merchantID  = intent.getStringExtra("MERCHANT");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        firebaseFirestore= FirebaseFirestore.getInstance();
        new Thread(() -> 
        {
            Looper.prepare();
            runOnUiThread(() ->
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                dialog.setMessage("PLEASE WAIT");
                dialog.show();
            });
            JSONParser jsonParser = new JSONParser(this);
            String param= "MID="+merchantID+ "&ORDER_ID=" + orderID+ "&CUST_ID="+customerID+ "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+"&WEBSITE=DEFAULT"+ "&CALLBACK_URL="+verify+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, param);
            if(jsonObject != null)
            {
                try
                {
                    CHECKSUM =jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                }
                catch (JSONException e)
                {
                    runOnUiThread(() -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
                    e.printStackTrace();
                }
            }
            runOnUiThread(() ->
            {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            });
            PaytmPGService Service = PaytmPGService.getProductionService();
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("MID", merchantID);
            paramMap.put("ORDER_ID", orderID);
            paramMap.put("CUST_ID", customerID);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amount);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL" ,verify);
            paramMap.put("CHECKSUMHASH" , CHECKSUM);
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            runOnUiThread(() -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
            PaytmOrder Order = new PaytmOrder(paramMap);
            Service.initialize(Order,null);
            Service.startPaymentTransaction(this, true, true, this);
        }).start();
    }
    @Override
    public void onTransactionResponse(Bundle bundle)
    {
        final String payStatus = bundle.getString("STATUS");
        String paySuccess = "TXN_SUCCESS";
        final String payResponse = bundle.getString("RESPMSG");
        assert payStatus != null;
        if(payStatus.equals(paySuccess))
        {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final Help help = new Help();
            if(user != null && help.getInfo("EMAIL", Checksum.this) != null && help.getInfo("PASSWORD", Checksum.this) != null && help.getInfo("PHONE", Checksum.this) != null && help.getInfo("STATE", Checksum.this) != null && help.getInfo("NAME", Checksum.this) != null)
            {
                DocumentReference documentReference = firebaseFirestore.collection("STUDENTS").document(email);
                Map<String, Object> state = new HashMap<>();
                state.put("STATE", "PREMIUM");
                state.put("ORDERID",orderID);
                documentReference.update(state).addOnSuccessListener(aVoid ->
                {
                    help.saveInfo("STATE","PREMIUM", Checksum.this);
                    FancyToast.makeText(Checksum.this, "CONGRATULATIONS !!! PREMIUM MEMBERSHIP ACTIVATED FRO ACADEMIC YEAR 2020-2021",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    Intent intent=new Intent(Checksum.this, OrderStatus.class);
                    intent.putExtra("ORDERID",orderID);
                    intent.putExtra("STATUS",payStatus);
                    intent.putExtra("RESPONSE",payResponse);
                    intent.putExtra("AMOUNT",amount);
                    intent.putExtra("MERCHANT", merchantID);
                    intent.putExtra("CHECKSUM", url);
                    intent.putExtra("VERIFY", verify);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Animatoo.animateDiagonal(this);
                    finish();
                }).addOnFailureListener(e -> FancyToast.makeText(Checksum.this, e.toString().toUpperCase(),FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show());
            }
        }
        else
        {
            Intent intent = new Intent(Checksum.this, OrderStatus.class);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ORDERID",orderID);
            intent.putExtra("STATUS",payStatus);
            intent.putExtra("RESPONSE",payResponse);
            intent.putExtra("AMOUNT",amount);
            intent.putExtra("MERCHANT", merchantID);
            intent.putExtra("CHECKSUM", url);
            intent.putExtra("VERIFY", verify);
            startActivity(intent);
            Animatoo.animateDiagonal(this);
            finish();
        }
    }
    @Override
    public void networkNotAvailable()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : NETWORK NOT AVAILABLE",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void clientAuthenticationFailed(String s)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : CLIENT AUTHENTICATION FAILED",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void someUIErrorOccurred(String s)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : UI ERROR OCCURED",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : ERROR LOADING WEB PAGE",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void onBackPressedCancelTransaction()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : BACK BUTTON PRESSED",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        FancyToast.makeText(Checksum.this, "PAYMENT FAILED : TRANSACTION CANCELLED",FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
        Animatoo.animateDiagonal(this);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Animatoo.animateDiagonal(this);
        finish();
    }

}