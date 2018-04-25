package com.unnamedsoftware.russesamfunnet;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alexander Eilert Berg on 18.04.2018.
 */
public class UploadImage extends AsyncTask<Void,Void,Boolean>
{
    OkHttpClient client;
    Request request;
    File file;
    String url;

    public UploadImage(File file, String url)
    {
        this.file = file;
        this.url = url;
    }


    @Override
    protected Boolean doInBackground(Void... voids)
    {
        try
        {
            client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name",file.getName())
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "multipart/form-data")
                    .post(requestBody)
                    .build();

            System.out.println("--- 4 ---");
            System.out.println(file.getName());
            System.out.println(request.headers());
            System.out.println(request.url());
            System.out.println(request.toString());

         /**   if(client.newCall(request).execute().isSuccessful())
            {
                System.out.println("YAY");
            }*/
            client.newCall(request).enqueue(new Callback()
            {

                @Override
                public void onFailure(Call call, IOException e)
                {
                    System.out.println("----------------------------- Failure -----------------------------");
                    System.out.println(call.toString());
                    e.printStackTrace();                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    System.out.println(response.message());
                    // Upload successful
                    if(response.isSuccessful()) {
                        System.out.println("Upload complete");

                    } else {
                        System.out.println(":(");
                    }
                }
            });


            System.out.println("--- 5 ---");

           return true;
        } catch (Exception ex)
        {
            // Handle the error
        }

        System.out.println("--- 6 ---");

         return false;
    }

}

