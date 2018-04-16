package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.JSONObjectParser;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.UserProfile;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>
{
    private List<FeedEntity> posts;
    private Context context;
    private String url;
    private Long userID;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView poster, post;
        private CircularImageView userImage;
        private RelativeLayout relativeLayout;

        public ViewHolder(View view)
        {
            super(view);
            poster = view.findViewById(R.id.rvPoster);
            post = view.findViewById(R.id.rvPost);
            userImage = view.findViewById(R.id.userProfilePicture);
            relativeLayout = view.findViewById(R.id.feedPostRow);
        }
    }

    public FeedAdapter(List<FeedEntity> feedPosts, Context context, String url, Long userID)
    {
        this.posts = feedPosts;
        this.context = context;
        this.url = url;
        System.out.println("--- From feed to feedAdapter: " + userID);
        this.userID = userID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_post_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final FeedEntity feedPost = posts.get(position);
        final Long russid = feedPost.getRussId().getRussId();
        final View view = holder.relativeLayout;
        holder.poster.setText(feedPost.getPoster());
        holder.post.setText(feedPost.getMessage());
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                final Long id = feedPost.getFeedId();
                System.out.println(id);
                optionMenu(view, id, position - 1, russid);
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtra("russ_entity", russid);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public void clear()
    {
        final int size = posts.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                posts.remove(0);
            }

            notifyDataSetChanged();
        }
    }


    private void optionMenu(View view, Long id, int p, Long russID)
    {
        final Long feedID = id;
        final int position = p;
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);

        System.out.println("+++++++++++++++++++++++++" + russID);
        System.out.println("+++++++++++++++++++++++++" + userID);
        if (russID.equals(userID))
        {
            PopupMenu popup = new PopupMenu(this.context, view);
            popup.setGravity(Gravity.RIGHT);
            popup.getMenuInflater()
                    .inflate(R.menu.option_menu_feed, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                public boolean onMenuItemClick(MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.RemoveMessage:
                            deletePost(feedID, position);
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
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private void deletePost(Long postId, int position)
    {
        String newUrl = this.url + postId;
        System.out.println(newUrl);
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
            }).execute(new URL(newUrl));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        Iterator it = posts.iterator();
        while (it.hasNext())
        {
            FeedEntity feedEntity = (FeedEntity) it.next();
            if (feedEntity.getFeedId().equals(postId))
            {
                posts.remove(feedEntity);
                notifyDataSetChanged();
                return;
            }
        }
    }
}
