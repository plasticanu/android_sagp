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
    private final RecyclerViewInterface recyclerViewInterface;

    public Post_RecyclerViewAdapter(Context context, List<Post> postModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.postModels = postModels;
        this.recyclerViewInterface = recyclerViewInterface;

    }
    @NonNull
    @Override
    public Post_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new Post_RecyclerViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Post_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvAuthor.setText(postModels.get(position).getAuthor());
        holder.tvDate.setText(postModels.get(position).getCreateTime());
        holder.tvContent.setText(postModels.get(position).getContent());
        holder.tvLike.setText(String.valueOf(postModels.get(position).getLikes().size()));
        holder.tvComment.setText(String.valueOf(postModels.get(position).getComments().size()));

    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor,tvDate,tvContent,tvLike,tvComment;


        public MyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.textView_user);
            tvDate = itemView.findViewById(R.id.textView_date);
            tvContent = itemView.findViewById(R.id.textView_content);
            tvLike = itemView.findViewById(R.id.textView_like_count);
            tvComment = itemView.findViewById(R.id.textView_comment_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
