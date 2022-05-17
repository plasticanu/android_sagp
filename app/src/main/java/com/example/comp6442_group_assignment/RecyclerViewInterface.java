package com.example.comp6442_group_assignment;

public interface RecyclerViewInterface {
    //used to perform item click event
    void onItemClick(int position);
    //used to perform like post event
    void onLikeClick(int position);
    //used to perform load more event
    void onLoadMore(int position);
    //user to perform go to the top
    void onGoTop(int position);
}
