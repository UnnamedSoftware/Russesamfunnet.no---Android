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
public class UploadImage extends AsyncTask
{
    public void uploadImage(File image, String imageName, String url)
    {
        final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/*");
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_JPEG, image))
                .build();

        Request request = new Request.Builder().url(url)
                .post(requestBody).build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException
            {
                if (!response.isSuccessful())
                {
                    throw new IOException("Unexpected code " + response);
                }

                System.out.println(response);
            }

        });
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        return null;
    }
}
