package com.projects.orderon;

import com.projects.orderon.models.Store;

public interface RecyclerViewInterface {

    void onItemClick(int position);
    void onStoreClick(int position, Store store);
}
