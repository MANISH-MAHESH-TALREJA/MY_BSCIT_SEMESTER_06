package net.manish.mybscitsem06;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import java.util.Objects;

public class OrderStatus extends AppCompatActivity implements View.OnClickListener
{
    final String success = "TXN_SUCCESS";
    Button button;
    String result;
    Help help;
    Intent intent;
    TextView name, amount, response, orderID, status;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        help = new Help();
        button=findViewById(R.id.button);
        intent=getIntent();
        name = findViewById(R.id.customer);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);
        response = findViewById(R.id.response);
        orderID = findViewById(R.id.orderID);
        name.setText(help.getInfo("NAME",getApplicationContext()));
        String fees = "₹ "+Objects.requireNonNull(intent.getExtras()).getString("AMOUNT")+" /-";
        amount.setText(fees);
        status.setText(Objects.requireNonNull(intent.getExtras()).getString("STATUS"));
        orderID.setText(Objects.requireNonNull(intent.getExtras()).getString("ORDERID"));
        ImageView mImageView = findViewById(R.id.imageView3);
        result="HURRAH !!! PAYMENT DONE SUCCESSFULLY. NOW YOU CAN USE THE APP.";
        if(Objects.equals(intent.getStringExtra("STATUS"), success))
        {
            response.setText(result.toUpperCase());
            mImageView.setImageResource(R.drawable.correct);
        }
        else
        {
            mImageView.setImageResource(R.drawable.wrong);
            response.setText(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("RESPONSE")).toUpperCase());
            String text = "RETRY PAYMENT";
            button.setText(text);
        }
        button.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(), SampleActivity.class));
        Animatoo.animateDiagonal(this);
        finish();
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
        if(myView  == findViewById(R.id.button))
        {
            if(Objects.equals(intent.getStringExtra("STATUS"), success))
            {
                startActivity(new Intent(getApplicationContext(), SampleActivity.class));
            }
            else
            {
                Intent newIntent = new Intent(getApplicationContext(), Checksum.class);
                newIntent.putExtra("AMOUNT",Objects.requireNonNull(intent.getExtras()).getString("AMOUNT"));
                newIntent.putExtra("MERCHANT",Objects.requireNonNull(intent.getExtras()).getString("MERCHANT"));
                newIntent.putExtra("CHECKSUM",Objects.requireNonNull(intent.getExtras()).getString("CHECKSUM"));
                newIntent.putExtra("VERIFY",Objects.requireNonNull(intent.getExtras()).getString("VERIFY"));
                startActivity(newIntent);
            }
            Animatoo.animateDiagonal(this);
            finish();
        }
        else if(myView  == findViewById(R.id.back))
        {
            onBackPressed();
        }
    }
}
