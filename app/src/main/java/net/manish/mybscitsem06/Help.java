package net.manish.mybscitsem06;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import java.io.File;
import java.nio.charset.StandardCharsets;
import at.favre.lib.armadillo.Armadillo;

public class Help
{
    String purchase(String string)
    {
        return new String(Base64.decode(string,0), StandardCharsets.UTF_8);
    }
    void clearData(Context context)
    {
        FirebaseAuth.getInstance().signOut();
        saveInfo("EMAIL", null, context);
        saveInfo("PHONE", null, context);
        saveInfo("STATE", null, context);
        saveInfo("PASSWORD", null, context);
        saveInfo("NAME", null, context);
    }
    void saveInfo(String key, String value, Context context)
    {
        SharedPreferences preferences = Armadillo.create(context, "DATA").encryptionFingerprint(context).build();
        preferences.edit().putString(key, value).apply();
    }
    String getInfo(String key, Context context)
    {
        SharedPreferences prefs = Armadillo.create(context, "DATA").encryptionFingerprint(context).build();
        return prefs.getString(key, null);
    }
    void saveData(String key, String value, Context context)
    {
        SharedPreferences preferences = Armadillo.create(context, "CONTENT").encryptionFingerprint(context).build();
        preferences.edit().putString(key, value).apply();
    }
    String getData(String key, Context context)
    {
        SharedPreferences prefs = Armadillo.create(context, "CONTENT").encryptionFingerprint(context).build();
        return prefs.getString(key, null);
    }
    void saveSecret(String key, String value, Context context)
    {
        SharedPreferences preferences = Armadillo.create(context, "SECRET").encryptionFingerprint(context).build();
        preferences.edit().putString(key, value).apply();
    }
    String getSecret(String key, Context context)
    {
        SharedPreferences prefs = Armadillo.create(context, "SECRET").encryptionFingerprint(context).build();
        return prefs.getString(key, null);
    }
    void saveDownload(String key, String value, Context context)
    {
        SharedPreferences preferences = Armadillo.create(context, "DOWNLOAD").encryptionFingerprint(context).build();
        preferences.edit().putString(key, value).apply();
    }
    String getDownload(String key, Context context)
    {
        SharedPreferences prefs = Armadillo.create(context, "DOWNLOAD").encryptionFingerprint(context).build();
        return prefs.getString(key, null);
    }
    void saveSettings(String key, boolean value, Context context)
    {
        SharedPreferences preferences = Armadillo.create(context, "SETTINGS").encryptionFingerprint(context).build();
        preferences.edit().putBoolean(key, value).apply();
    }
    boolean getSettings(String key, Context context)
    {
        SharedPreferences prefs = Armadillo.create(context, "SETTINGS").encryptionFingerprint(context).build();
        return prefs.getBoolean(key, false);
    }

    void deleteFiles(String directory)
    {
        boolean remove = false;
        File dir = new File(directory);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            assert children != null;
            for (String child : children)
            {
                remove = new File(dir, child).delete();
            }
        }
        Log.d("FILES DELETED", "FILE DELETE : "+remove);
    }

    public void deleteCache(Context context)
    {
        File dir = context.getCacheDir();
        deleteDir(dir);
    }

    private boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory())
        {
            String[] children = dir.list();
            assert children != null;
            for (String child : children)
            {
                boolean success = deleteDir(new File(dir, child));
                if (!success)
                {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
        {
            return dir.delete();
        }
        else
        {
            return false;
        }
    }
    public static class TypefaceSpan extends MetricAffectingSpan
    {
        private final Typeface typeface;
        TypefaceSpan(Typeface typeface)
        {
            this.typeface = typeface;
        }
        @Override public void updateDrawState(TextPaint tp)
        {
            tp.setTypeface(typeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        @Override public void updateMeasureState(TextPaint p)
        {
            p.setTypeface(typeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
    public SpannableString typeface(Typeface typeface, CharSequence string)
    {
        SpannableString s = new SpannableString(string);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }
}
