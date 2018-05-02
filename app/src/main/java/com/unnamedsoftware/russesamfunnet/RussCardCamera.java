package com.unnamedsoftware.russesamfunnet;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 09.04.2018.
 */
public class RussCardCamera extends AppCompatActivity
{
    private CropImageView cropImageView;
    private Uri cropImageUri;
    private Bitmap cropped;
    private String url;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        cropImageView = findViewById(R.id.CropImageView);

        Button button = findViewById(R.id.chooseDifferent);
        button.setText("Ta nytt bilde");

        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropImageClick(View view)
    {
        this.cropped = cropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            cropImageView.setImageBitmap(cropped);
    }

    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view)
    {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            Uri imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri))
            {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                cropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions)
            {
                cropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (cropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            cropImageView.setImageUriAsync(cropImageUri);
        } else
        {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
    public Intent getPickImageChooserIntent()
    {

        // Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam)
        {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null)
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// the main intent is the last in the list
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents)
        {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))
            {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri()
    {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null)
        {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent data)
    {
        boolean isCamera = true;
        if (data != null && data.getData() != null)
        {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri)
    {
        try
        {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e)
        {
            if (e.getCause() instanceof ErrnoException)
            {
                return true;
            }
        } catch (Exception e)
        {
        }
        return false;
    }


    /**
     * Changes the image
     *
     * @param view
     * @throws IOException
     */
    public void onApproveImage(View view) throws IOException
    {
        Bitmap bitmap = cropImageView.getCroppedImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        String filename = String.format(((Global) this.getApplication()).getRussId() + "russesamfunnetRussCard.jpg");

        this.file = new File(this.getCacheDir(), filename);
        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(file))
        {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.url = "http://158.38.101.162:8080/upload/";
        try {
            UploadImage uploadImage = new UploadImage(file, url);
            uploadImage.execute();
            setImageName();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    private void setImageName() throws IOException
    {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "setRussCard?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&pictureName=" + file.getName());
        } else
        {
            System.out.println(((Global) this.getApplication()).getAccessToken());
            newUrl = getString(R.string.url) + "setRussCard?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&pictureName=" + file.getName();
        }
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {

                }
            }).execute(new URL(newUrl));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }
}