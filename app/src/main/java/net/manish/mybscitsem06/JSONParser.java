package net.manish.mybscitsem06;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser
{
    private final Context context;
    public JSONParser(Context context)
    {
        this.context=context;
    }

    JSONObject makeHttpRequest(String url, String params) {
        JSONObject jObj;
        try
        {
            String retSrc;
            URL url1 = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(params.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(params);
            out.close();
            InputStream in = urlConnection.getInputStream();
            byte[] bytes = new byte[10000];
            StringBuilder x = new StringBuilder();
            int numRead;
            while ((numRead = in.read(bytes)) >= 0)
            {
                x.append(new String(bytes, 0, numRead));
            }
            retSrc=x.toString();
            jObj = new JSONObject(retSrc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(() -> FancyToast.makeText(context, "CONNECTIVITY ISSUE. PLEASE TRY AGAIN LATER",FancyToast.LENGTH_SHORT, FancyToast.WARNING,false).show());
            return null;
        }
        return jObj;
    }
}