package com.unnamedsoftware.russesamfunnet;

import android.app.Activity;
import android.content.Context;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

/**
 * Created by Alexander Eilert Berg on 16.04.2018.
 */
public class Report extends Activity
{
    public void reportUser(String reportMessage, String reportedPost, long userID, long russID, Context context)
    {
        String message = reportMessage + System.lineSeparator() + "Post: " + reportedPost;

        BackgroundMail.newBuilder(context)
                .withUsername("russesamfunnetuserreport@gmail.com")
                .withPassword("bacheloridata")
                .withMailto("russesamfunnet@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Report from: " + userID + " on " + russID)
                .withBody(message)
                .send();
            finish();
    }


    public void reportBug(String message, long userID, Context context)
    {
        BackgroundMail.newBuilder(context)
                .withUsername("russesamfunnetuserreport@gmail.com")
                .withPassword("bacheloridata")
                .withMailto("russesamfunnet@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Bug report from: " + userID)
                .withBody(message)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("_________________ DID IT WORK? _________________");
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        System.out.println("!!!CRY!!!");
                    }
                })
                .send();

        finish();
    }
}
