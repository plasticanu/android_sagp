package com.example.comp6442_group_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        View itemView;

        if(viewType == R.layout.recycler_view_row){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        }

        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_button, parent, false);
        }
        return new Post_RecyclerViewAdapter.MyViewHolder(itemView,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Post_RecyclerViewAdapter.MyViewHolder holder, int position) {
        if(position == postModels.size()) {

        }
        else {
            holder.tvAuthor.setText(postModels.get(position).getAuthor());
            holder.tvDate.setText(postModels.get(position).getCreateTime());
            holder.tvContent.setText(postModels.get(position).getContent());
            holder.tvLike.setText(String.valueOf(postModels.get(position).getLikes().size()));
            holder.tvComment.setText(String.valueOf(postModels.get(position).getComments().size()));
        }

    }

    @Override
    public int getItemCount() {
        return postModels.size()+1;
    }
    public void update(List<Post> data){
        this.postModels.clear();
        this.postModels.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == postModels.size()) ? R.layout.recycler_view_button : R.layout.recycler_view_row;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor,tvDate,tvContent,tvLike,tvComment;
        ImageButton likeButton;
        Button button;
        public MyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);


            tvAuthor = itemView.findViewById(R.id.textView_user);
            tvDate = itemView.findViewById(R.id.textView_date);
            tvContent = itemView.findViewById(R.id.textView_content);
            tvLike = itemView.findViewById(R.id.textView_like_count);
            tvComment = itemView.findViewById(R.id.textView_comment_count);
            likeButton = itemView.findViewById(R.id.button_like);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            if(likeButton!=null) {
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (recyclerViewInterface != null) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                recyclerViewInterface.onLikeClick(pos);
                            }
                        }
                    }
                });
            }

            button = itemView.findViewById(R.id.button_load_more);
            if(button!=null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (recyclerViewInterface != null) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                recyclerViewInterface.onLoadMore(pos);
                            }
                        }
                    }
                });
            }

        }
    }
}
