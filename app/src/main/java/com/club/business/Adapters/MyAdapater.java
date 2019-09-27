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
import android.widget.Toast;

import com.club.business.Comments;
import com.club.business.OthersProfile;
import com.club.business.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapater extends RecyclerView.Adapter<MyAdapater.ViewHolder> {

    private List<ForumList> listItems;
    private Context context;


    public MyAdapater(List<ForumList> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       View v= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.card_with_image_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        ForumList forumList=listItems.get(position);
        viewHolder.user.setText(forumList.getUser());
        viewHolder.post.setText(forumList.getPost());
        viewHolder.time.setText(forumList.getTime());
        viewHolder.key=forumList.getKey();

        String imageurl = forumList.getImgURL();
        Picasso.get().load(imageurl).into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView display_picture;
        TextView user,post,time;
        ImageView image,share,comment;

        String key;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            display_picture= (CircularImageView) itemView.findViewById(R.id.profile_image);
            user = (TextView) itemView.findViewById(R.id.card_name);
            post = (TextView) itemView.findViewById(R.id.card_desc);
            time = (TextView) itemView.findViewById(R.id.item_time);
            image = (ImageView) itemView.findViewById(R.id.forum_card_image);
            comment = (ImageView) itemView.findViewById(R.id.img_comment);
            
            display_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,OthersProfile.class);
                    intent.putExtra("key",key);
                    v.getContext().startActivity(intent);
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, Comments.class);
                    i.putExtra("key",key);
                    v.getContext().startActivity(i);
                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, Comments.class);
                    i.putExtra("key",key);
                    v.getContext().startActivity(i);
                }
            });



        }
    }
}
