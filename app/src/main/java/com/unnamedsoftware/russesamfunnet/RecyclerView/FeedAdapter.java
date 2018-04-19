package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.JSONObjectParser;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.Report;
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
    private Long groupId = null;
    private Long userID;
    private String removeUserUrl;
    private ImageLoader imageLoader;

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
            userImage = view.findViewById(R.id.feedUserProfilePicture);
            relativeLayout = view.findViewById(R.id.feedPostRow);
        }
    }

    public FeedAdapter(List<FeedEntity> feedPosts, Context context, String url, Long userID, Long groupId, String removeUserUrl, ImageLoader imageLoader)
    {
        this.removeUserUrl = removeUserUrl;
        this.groupId = groupId;
        this.posts = feedPosts;
        this.context = context;
        this.url = url;
        System.out.println("--- From feed to feedAdapter: " + userID);
        this.userID = userID;
        this.imageLoader = imageLoader;
    }

    public FeedAdapter(List<FeedEntity> feedPosts, Context context, String url, Long userID, ImageLoader imageLoader)
    {
        this.posts = feedPosts;
        this.context = context;
        this.url = url;
        System.out.println("--- From feed to feedAdapter: " + userID);
        this.userID = userID;
        this.imageLoader = imageLoader;
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
        final CircularImageView imageView = holder.userImage;
        holder.poster.setText(feedPost.getPoster());
        holder.post.setText(feedPost.getMessage());

        if (feedPost.getRussId().getProfilePicture() != null)
        {
            imageLoader.loadImage(feedPost.getRussId().getProfilePicture(), new SimpleImageLoadingListener()
            {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                {
                    imageView.setImageBitmap(loadedImage);
                }
            });
        }
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


    private void optionMenu(View view, Long id, int p, final Long russID)
    {
        final Long feedID = id;
        final int position = p;
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);

        System.out.println("+++++++++++++++++++++++++" + russID);
        System.out.println("+++++++++++++++++++++++++" + userID);
        if (groupId != null)
        {
            System.out.println(russID);
            System.out.println(userID);
            if (russID.equals(userID))
            {
                PopupMenu popup = new PopupMenu(this.context, view);
                popup.setGravity(Gravity.RIGHT);
                popup.getMenuInflater()
                        .inflate(R.menu.option_menu_group_feed, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.LeaveGroup:
                                removeFromGroup(userID);
                                break;

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
                        .inflate(R.menu.option_menu_group_feed_not_user_message, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.RemoveUser:
                                removeFromGroup(russID);
                                break;

                            case R.id.Rapporter:
                                reportPost(russID);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        } else if (russID.equals(userID))
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
                            reportPost(russID);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private void removeFromGroup(Long russId)
    {
        String removeUrl = removeUserUrl + russId;

        System.out.println(removeUrl);
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

    private void reportPost(final Long russID)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.report_dialog);
        final EditText editText = dialog.findViewById(R.id.reportMessage);
        Button submitButton = dialog.findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Report report = new Report();
                report.reportUser(editText.getText().toString(), userID, russID, context);
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
