package com.unnamedsoftware.russesamfunnet;

import android.content.Context;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 25.04.2018.
 */
public class GetTop3Scoreboard
{
    private Context context;
    private Long groupID;

    public GetTop3Scoreboard(Long groupID, Context context)
    {
        this.groupID = groupID;
        this.context = context;
    }

    public void getTop3()
    {
        String url;


        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (context.getString(R.string.url) + "scoreboardGroupTop3?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupId=" + groupID);
        } else
        {
            System.out.println(((Global) context.getApplicationContext()).getAccessToken());
            url = (context.getString(R.string.url) + "scoreboardGroupTop3?accessToken=" + ((Global) context.getApplicationContext()).getAccessToken() + "&type=russesamfunnet&groupId=" + groupID);
        }

        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray) throws JSONException
                {
                    addScoreboardToList(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        //return scoreboardEntityList;
    }

    private void addScoreboardToList(JSONArray jsonArray)
    {
        List<ScoreboardEntity> scoreboardEntityList;
        JSONArray users = null;
        try
        {
            users = jsonArray;
            if (jsonArray == null) System.out.println("Scoreboard DEADBEEF");
            System.out.println("Hi?");
            System.out.println(users.length());
            int length = users.length();
            for (int i = 0; i < length && i <= 2; i++)
            {
                JSONObject u = users.getJSONObject(i);

                Integer scoreboardId = Integer.valueOf(u.getString("scoreboardId"));
                Integer points = Integer.valueOf(u.getString("points"));
                Integer position = Integer.valueOf(u.getString("position"));

                JSONObject newRussObject = u.getJSONObject("russId");

                Long russId = Long.valueOf(newRussObject.getString("russId"));
                String russStatus = newRussObject.getString("russStatus");
                String firstName = newRussObject.getString("firstName");
                String lastName = newRussObject.getString("lastName");
                String email = newRussObject.getString("email");
                String russPassword = newRussObject.getString("russPassword");
                String profilePicture = newRussObject.getString("profilePicture");
                String russCard = newRussObject.getString("russCard");
                String russRole = newRussObject.getString("russRole");
                Integer russYear = Integer.valueOf(newRussObject.getString("russYear"));

                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear, profilePicture, russCard);
                System.out.println(russId);
                ScoreboardEntity user = new ScoreboardEntity(scoreboardId, points, position, russ);
              //  scoreboardEntityList.add(user);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       // return scoreboardEntityList;
    }


}
