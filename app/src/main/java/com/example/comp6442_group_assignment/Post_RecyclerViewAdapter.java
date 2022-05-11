package com.example.comp6442_group_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/** Custom recyclerView Adapter class
 * @Author Jiyuan Chen u7055573
 */
public class Post_RecyclerViewAdapter extends RecyclerView.Adapter<Post_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Post> postModels;

    public Post_RecyclerViewAdapter(Context context, List<Post> postModels){
        this.context = context;
        this.postModels = postModels;

    }
    @NonNull
    @Override
    public Post_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new Post_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Post_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvAuthor.setText(postModels.get(position).getAuthor());
        holder.tvDate.setText(postModels.get(position).getCreateTime());
        holder.tvContent.setText(postModels.get(position).getContent());
        holder.tvLike.setText(String.valueOf(postModels.get(position).getLikes()));


    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor,tvDate,tvContent,tvLike;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.textView_user);
            tvDate = itemView.findViewById(R.id.textView_date);
            tvContent = itemView.findViewById(R.id.textView_content);
            tvLike = itemView.findViewById(R.id.textView_like_count);
        }
    }
}