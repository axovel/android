package com.axovel.ecommerce.sweetschoice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import com.axovel.ecommerce.sweetschoice.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Umesh Chauhan on 24-12-2015.
 * Axovel Private Limited
 */
public class General {

    // Checking If file Exists or not
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    // Setting File Data
    @SuppressWarnings("static-access")
    public boolean setData(String FILE_NAME, String data, Context context) {
        boolean isSuccess = false;
        FileOutputStream fos;
        try {
            if (fileExists(context, FILE_NAME)) {
                fos = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
                fos.write(String.valueOf(data).getBytes());
                fos.close();
                isSuccess = true;
            } else {
                File file = new File(context.getFilesDir(), FILE_NAME);
                file.createNewFile();
                fos = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
                fos.write(String.valueOf(data).getBytes());
                fos.close();
                isSuccess = true;
            }

        } catch (Exception e) {
            Toast.makeText(context,
                    R.string.file_write_failure,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return isSuccess;
    }

    // Getting File Data
    public String getData(String FILE_NAME, Context context) {
        String data = "";
        FileInputStream fis;
        try {
            fis = context.openFileInput(FILE_NAME);
            byte arr[] = new byte[fis.available()];
            fis.read(arr);
            data = new String(arr);
            fis.close();
        } catch (Exception e) {
            Toast.makeText(context, R.string.file_read_failure, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return data;
    }

    // Check for Internet Connection
    public static boolean isOnline(Context mcontext) {
        ConnectivityManager cm = (ConnectivityManager) mcontext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected()
                && netInfo.isAvailable();
    }
    // Email Check
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
