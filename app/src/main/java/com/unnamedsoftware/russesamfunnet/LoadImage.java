package com.unnamedsoftware.russesamfunnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alexander Eilert Berg on 05.04.2018.
 */
public class LoadImage extends AsyncTask<String, Void, Bitmap>
{
    private ProgressDialog progressDialog;
    private Context context;
    private ImageView view;

    public LoadImage(Context context, ImageView view)
    {
        this.context = context;
        this.view = view;
    }


    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        // Create a progressdialog
        progressDialog = new ProgressDialog(context);
        // Set progressdialog title
        progressDialog.setTitle("Laster bilde");
        // Set progressdialog message
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        // Show progressdialog
        progressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... URL)
    {

        String imageURL = URL[0];

        Bitmap bitmap = null;
        try
        {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        FileOutputStream outStream = null;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        String fileName = String.format("russesamfunnetProfilePicture.jpg", System.currentTimeMillis());
        File outFile = new File(storageDir, fileName);
        try
        {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try
        {
            outStream.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            outStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        // Set the bitmap into ImageView
        this.view.setImageBitmap(result);
        // Close progressdialog
        progressDialog.dismiss();
    }
}
