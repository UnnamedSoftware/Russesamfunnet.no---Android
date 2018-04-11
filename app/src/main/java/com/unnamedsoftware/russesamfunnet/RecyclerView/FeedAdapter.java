package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Context;
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
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>
{
    private List<FeedEntity> posts;
    private Context context;

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

    public FeedAdapter(List<FeedEntity> feedPosts, Context context)
    {
        this.posts = feedPosts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_post_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        FeedEntity feedPost = posts.get(position);
        final View view = holder.relativeLayout;
        holder.poster.setText(feedPost.getPoster());
        holder.post.setText(feedPost.getMessage());
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                optionMenu(view);
                return false;
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


    private void optionMenu(View view)
    {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(50);
        PopupMenu popup = new PopupMenu(this.context,view);
        popup.setGravity(Gravity.RIGHT);
        popup.getMenuInflater()
                .inflate(R.menu.option_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.Rapporter:
                        Toast.makeText(context, "One", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.SlettMelding:
                        Toast.makeText(context, "Two", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.SlettBruker:
                        Toast.makeText(context, "Three", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }


}
