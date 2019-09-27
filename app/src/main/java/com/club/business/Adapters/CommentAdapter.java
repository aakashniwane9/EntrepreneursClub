package com.club.business.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.club.business.OthersProfile;
import com.club.business.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentList> listItems;
    private Context context;

    public CommentAdapter(List<CommentList> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        CommentList commentList=listItems.get(position);

        viewHolder.comment_username.setText(commentList.getComment_username());
        viewHolder.comment_usercomment.setText(commentList.getComment_usercomment());
        viewHolder.comment_timestamp.setText(commentList.getComment_timestamp());


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView comment_username,comment_usercomment,comment_timestamp;
        ImageView comment_dp;
        String key;

            public ViewHolder(@NonNull View itemView){
                super(itemView);
                comment_dp= (CircularImageView) itemView.findViewById(R.id.comment_recycler_profile_image);
                comment_username = (TextView) itemView.findViewById(R.id.comment_recycler_username);
                comment_usercomment = (TextView) itemView.findViewById(R.id.comment_recycler_usercomment);
                comment_timestamp = (TextView) itemView.findViewById(R.id.comment_recycler_timestamp);
            }
        }
    }
