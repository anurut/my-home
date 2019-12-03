package com.anurut.myHome;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

public class ExternalStorageUtil {

    // Check whether the external storage is mounted or not.
    public static boolean isExternalStorageMounted() {

        String dirState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(dirState))
        {
            return true;
        }else
        {
            return false;
        }
    }

    // Check whether the external storage is read only or not.
    public static boolean isExternalStorageReadOnly() {

        String dirState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(dirState))
        {
            return true;
        }else
        {
            return false;
        }
    }


    // Get private external storage base directory.
    public static String getPrivateExternalStorageBaseDir(Context context, String dirType)
    {
        String ret = "";
        if(isExternalStorageMounted()) {
            File file = context.getExternalFilesDir(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    // Get private cache external storage base directory.
    public static String getPrivateCacheExternalStorageBaseDir(Context context)
    {
        String ret = "";
        if(isExternalStorageMounted()) {
            File file = context.getExternalCacheDir();
            ret = file.getAbsolutePath();
        }
        return ret;
    }


    // Get public external storage base directory.
    public static String getPublicExternalStorageBaseDir()
    {
        String ret = "";
        if(isExternalStorageMounted()) {
            File file = Environment.getExternalStorageDirectory();
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    // Get public external storage base directory.
    public static String getPublicExternalStorageBaseDir(String dirType)
    {
        String ret = "";
        if(isExternalStorageMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    // Get external storage disk space, return MB
    public static long getExternalStorageSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get total block count.
            long count = fileState.getBlockCountLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate total space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }

    // Get external storage left free disk space, return MB
    public static long getExternalStorageLeftSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get free block count.
            long count = fileState.getFreeBlocksLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate free space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }

    // Get external storage available disk space, return MB
    public static long getExternalStorageAvailableSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get available block count.
            long count = fileState.getAvailableBlocksLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate available space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }

    //Save json config file to storage
    public static void saveJsonConfigFile(Activity activity, String fileName, JSONObject jsonObject) {

        String root = getPublicExternalStorageBaseDir();
        File myDir = new File(root + File.separator + Constants.CONFIG_DIRECTORY_PATH);

        if(!myDir.exists()){
            myDir.mkdirs();
        }

        final File file = new File(myDir, fileName);
        String[] toBeScannedStr = {file.getPath()};
        if(file.exists()){
            //file.delete();
        }else {
            try{
                Writer output = new BufferedWriter(new FileWriter(file));
                output.write(jsonObject.toString());
                output.flush();
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MediaScannerConnection.scanFile(activity, toBeScannedStr, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.d("MediaScanner", "Scan Complete" + file.getPath());
            }
        });


    }

    //TODO: Setup application from the config json
    //Read json file from storage
    public static void readJsonFromFile(String fileName, String relativePath) {

        String absolutePath = getPublicExternalStorageBaseDir()+ File.separator +"myHome"+ File.separator +relativePath;

        try {
            File myFile = new File(absolutePath + File.separator + fileName);
            FileInputStream stream = new FileInputStream(myFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            JSONObject jsonObj = new JSONObject(jsonStr);
            Log.d("mqtt", jsonObj.toString());
            Log.d("mqtt", jsonObj.length()+"");
            Iterator iterator = jsonObj.keys();
            while(iterator.hasNext()) {
                String key = (String) iterator.next();
                Log.d("Key>> ", key);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
