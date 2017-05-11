package com.example.nick.smartpatroldrone;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by Nick on 11/05/2017.
 */

public class PhotoSaver {
    String filename;
    String finalname;
    Bitmap image;
    Calendar rigthNow;
    MediaPlayer mediaPlayer;
    Context context;
    String imgName;

    public PhotoSaver(Context c, MediaPlayer m){
        this.context = c;
        this.mediaPlayer = m;
        rigthNow = Calendar.getInstance();
        filename = rigthNow.get(Calendar.DAY_OF_MONTH)+"_"+rigthNow.get(Calendar.MONTH)+"_"+rigthNow.get(Calendar.YEAR)+".jpeg";
    }

    public void record(){
        if(Environment.getExternalStorageState() != null){
            try{
                image = mediaPlayer.getCurrentFrame();
                File picture = getOutputMediaFile();
                FileOutputStream fos = new FileOutputStream(picture);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                Toast.makeText(context, "Picture saved in: " + imgName, Toast.LENGTH_LONG).show();
            }catch (FileNotFoundException e){
                Toast.makeText(context, "Picture file creation failed", Toast.LENGTH_LONG).show();
            }catch (IOException e){
                Toast.makeText(context, "Unable to create picture file", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "Internal memory not avaible", Toast.LENGTH_LONG).show();
        }
    }

    private File getOutputMediaFile(){
        rigthNow = Calendar.getInstance();
        finalname = "DronePicture_" + rigthNow.get(Calendar.HOUR)+":"+rigthNow.get(Calendar.MINUTE)+":"+rigthNow.get(Calendar.SECOND)+"_"+finalname;
        //Create media file name
        File mediaFile;
        imgName = Environment.getExternalStorageDirectory()+"/Pictures/"+finalname;
        mediaFile = new File(imgName);
        return mediaFile;
    }
}