package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView poster, post;
        private CircularImageView userImage;

        public ViewHolder(View view)
        {
            super(view);
            poster = view.findViewById(R.id.rvPoster);
            post = view.findViewById(R.id.rvPost);
            userImage = view.findViewById(R.id.userProfilePicture);
        }
    }

    public FeedAdapter(List<FeedEntity> feedPosts)
    {
        this.posts = feedPosts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_post_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        FeedEntity feedPost = posts.get(position);
        holder.poster.setText(feedPost.getPoster());
        holder.post.setText(feedPost.getMessage());
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public void clear() {
        final int size = posts.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                posts.remove(0);
            }

            notifyDataSetChanged();
        }
    }


}
