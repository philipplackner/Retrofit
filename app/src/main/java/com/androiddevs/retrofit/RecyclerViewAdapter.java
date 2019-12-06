package com.androiddevs.retrofit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RetroItem> dataList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<RetroItem> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(dataList.get(position).getTitle());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getThumbnailUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivCoverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private RetroItem lastDeletedItem;
    private int lastDeletedItemIndex;

    public void deleteItem(int position) {
        RetroItem deletedItem = dataList.get(position);
        lastDeletedItem = new RetroItem(deletedItem.getAlbumId(), deletedItem.getId(),
                deletedItem.getTitle(), deletedItem.getUrl(), deletedItem.getThumbnailUrl());
        lastDeletedItemIndex = position;

        dataList.remove(deletedItem);
        notifyItemRemoved(position);
        displaySnackbar();
    }

    private void displaySnackbar() {
        View view = ((Activity) context).findViewById(R.id.constraintLayout);
        Snackbar snackbar = Snackbar.make(view, "Successfully deleted item", Snackbar.LENGTH_LONG);

        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.add(lastDeletedItemIndex, lastDeletedItem);
                notifyItemInserted(lastDeletedItemIndex);
            }
        }).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivCoverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivCoverImage = itemView.findViewById(R.id.ivCoverImage);
        }
    }
}
