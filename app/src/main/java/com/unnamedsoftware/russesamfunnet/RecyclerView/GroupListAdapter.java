package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.AddUserToGroup;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.GetTop3Scoreboard;
import com.unnamedsoftware.russesamfunnet.Global;
import com.unnamedsoftware.russesamfunnet.GroupHub;
import com.unnamedsoftware.russesamfunnet.JSONObjectParser;
import com.unnamedsoftware.russesamfunnet.JSONParser;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.Scoreboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>
{
    private List<GroupEntity> groupEntitiesList;
    private Context context;
    private Long userID;
    private String removeUserUrl;
    private List<ScoreboardEntity> scoreboardEntityList = new ArrayList<>();
    private Bitmap userImage;
    private HashMap<String, Bitmap> images = new HashMap<>();

    private HashMap<GroupEntity, List<ScoreboardEntity>> scoreboardMap;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView groupName;
        private RelativeLayout layout;
        private List<CircularImageView> userTopList;

       // private CircularImageView userTop1;
       // private CircularImageView userTop2;
       // private CircularImageView userTop3;


        public ViewHolder(View view)
        {
            super(view);
            userTopList = new ArrayList<>();
            this.groupName = view.findViewById(R.id.glaGroupName);
            this.layout = view.findViewById(R.id.glaGroupListRowLayout);
            this.userTopList.add((CircularImageView) view.findViewById(R.id.userTop1View));
            this.userTopList.add((CircularImageView) view.findViewById(R.id.userTop2View));
            this.userTopList.add((CircularImageView) view.findViewById(R.id.userTop3View));
            //this.userTop1 = view.findViewById(R.id.userTop1View);
            //this.userTop2 = view.findViewById(R.id.userTop2View);
            //this.userTop3 = view.findViewById(R.id.userTop3View);
        }
    }


    public GroupListAdapter(List<GroupEntity> groupEntityList, Context context, Long userID,String removeUserUrl, HashMap<GroupEntity, List<ScoreboardEntity>> scoreboardMap, HashMap<String, Bitmap> images)
    {
        this.images = images;
        this.scoreboardMap = scoreboardMap;
        this.removeUserUrl = removeUserUrl;
        this.groupEntitiesList = groupEntityList;
        this.context = context;
        this.userID = userID;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final GroupEntity groupEntity = groupEntitiesList.get(position);
        final View view = holder.itemView;
        holder.groupName.setText(groupEntity.getSearchName());
        try {
            List<ScoreboardEntity> scoreboard = scoreboardMap.get(groupEntity);
            Iterator it = scoreboard.iterator();
            int i = 0;
            while (it.hasNext()) {
                try {
                    ScoreboardEntity scoreboardEntity = (ScoreboardEntity) it.next();
                    if (!scoreboardEntity.getRussId().getProfilePicture().equals("null")) {
                        holder.userTopList.get(i).setImageBitmap(images.get(scoreboardEntity.getRussId().getProfilePicture()));
                    } else {
                        holder.userTopList.get(i).setImageResource(R.drawable.default_user);
                    }
                    i++;
                } catch (Exception e) {
                    i++;
                }
            }
        } catch (Exception e)
        {
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), GroupHub.class);
                intent.putExtra("groupName", groupEntity.getSearchName());
                intent.putExtra("groupID", groupEntity.getGroupID());
                v.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                optionMenu(view, groupEntity.getGroupID());
                return false;
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return groupEntitiesList.size();
    }

    private void optionMenu(final View view, final Long groupID)
    {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);

        /*System.out.println("+++++++++++++++++++++++++" + russID);
        System.out.println("+++++++++++++++++++++++++" + userID);
        russID == userID*/

        if (true)
        {
            PopupMenu popup = new PopupMenu(this.context, view);
            popup.setGravity(Gravity.RIGHT);
            popup.getMenuInflater()
                    .inflate(R.menu.option_menu_add_user, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                public boolean onMenuItemClick(MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.AddUser:
                            Intent intent = new Intent(view.getContext(), AddUserToGroup.class);
                            intent.putExtra("groupID", groupID);
                            view.getContext().startActivity(intent);
                            break;

                        case R.id.LeaveGroup:
                            removeFromGroup(userID, groupID);
                            updateList(groupID);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        } else
        {
            PopupMenu popup = new PopupMenu(this.context, view);
            popup.setGravity(Gravity.RIGHT);
            popup.getMenuInflater()
                    .inflate(R.menu.option_menu_feed_not_user_message, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                public boolean onMenuItemClick(MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.Rapporter:
                            Toast.makeText(context, "One", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.LeaveGroup:
                            removeFromGroup(userID, groupID);
                            updateList(groupID);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private void removeFromGroup(Long russId, Long groupID)
    {
        String removeUrl = getRemoveUrl(groupID) + russId;

        System.out.println("Remove URL: " + removeUrl);
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    if (jsonObject != null)
                    {
                        try
                        {
                            System.out.println(jsonObject.getString("response"));
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }).execute(new URL(removeUrl));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


    }

    private String getRemoveUrl(Long groupID)
    {
        String url;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = this.removeUserUrl  + "&type=facebook&groupId=" + groupID + "&russId=";

        } else
        {
            url = this.removeUserUrl + "&type=russesamfunnet&groupId=" + groupID +"&russId=";
        }
        return url;
    }

    private void updateList(Long groupID)
    {
        Iterator it = groupEntitiesList.iterator();
        while (it.hasNext())
        {
            GroupEntity groupEntity = (GroupEntity) it.next();
            if (groupEntity.getGroupID().equals(groupID))
            {
                groupEntitiesList.remove(groupEntity);
                notifyDataSetChanged();
                return;
            }
        }
    }


}
