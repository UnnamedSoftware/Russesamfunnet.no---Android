package com.unnamedsoftware.russesamfunnet;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 18.04.2018.
 */
public class UploadImage extends AbstractAsyncTask<UploadImage.PostData, Integer, String>
{
    private final static String BOUNDARY = "*****";
    private final static String CRLF = "\r\n";

    public UploadImage(OnPostExecute<String> callback)
    {
        super(callback);
    }

    @Override
    protected String doInBackground(PostData... pictures)
    {
        StringBuilder result = new StringBuilder();


        for (PostData pd : pictures)
        {
            try
            {
                // Setup request
                HttpURLConnection con = (HttpURLConnection) pd.getUrl().openConnection();
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                // Start content wrapper
                DataOutputStream daos = new DataOutputStream(con.getOutputStream());
                daos.writeBytes("--" + BOUNDARY + CRLF);
                daos.writeBytes("Content-Type: image/jpeg");
                daos.writeBytes(CRLF);
                daos.writeBytes("Content-Disposition: form-data; name=\"" +
                        pd.getName() + "\"; filename=\"" +
                        pd.getFileName() + "\"" + CRLF);
                daos.writeBytes(CRLF);
                daos.flush();

                // Copy stream
                int len;
                try (InputStream is = new BufferedInputStream(pd.getInputStream()))
                {
                    byte[] buff = new byte[2048];
                    while ((len = is.read(buff)) != -1)
                    {
                        daos.write(buff, 0, len);
                    }
                }

                // End content wrapper
                daos.writeBytes(CRLF);
                daos.writeBytes("--" + BOUNDARY + "--" + CRLF);

                // Flush buffers
                daos.close();

                // Get response
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    char[] cbuff = new char[1024];
                    while ((len = br.read(cbuff)) != -1)
                    {
                        result.append(cbuff, 0, len);
                    }
                    br.close();
                }

                con.disconnect();
            } catch (IOException e)
            {

                Log.e("PostPicture", "doInBackground: ", e);
            }
        }

        return result.toString();
    }

    public static class PostData
    {
        URL url;
        InputStream inputStream;
        String name;
        String fileName;

        public URL getUrl()
        {
            return url;
        }

        public InputStream getInputStream()
        {
            return inputStream;
        }

        public String getName()
        {
            return name;
        }

        public String getFileName()
        {
            return fileName;
        }
    }
}