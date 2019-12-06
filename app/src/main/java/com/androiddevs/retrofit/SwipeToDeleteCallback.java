package com.androiddevs.retrofit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    Context context;
    RecyclerViewAdapter adapter;
    Drawable trashIcon;
    ColorDrawable backgroundColor;

    public SwipeToDeleteCallback(Context context, RecyclerViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.context = context;
        trashIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        backgroundColor = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.deleteItem(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View view = viewHolder.itemView;

        int margin = (view.getHeight() - trashIcon.getIntrinsicHeight()) / 2;
        int top = view.getTop() + (view.getHeight() - trashIcon.getIntrinsicHeight()) / 2;
        int bottom = top + trashIcon.getIntrinsicHeight();

        if (dX > 0) {
            swipeRight(view, margin, top, bottom, dX);
        } else if (dX < 0) {
            swipeLeft(view, margin, top, bottom, dX);
        } else {
            // no swipe
            backgroundColor.setBounds(0, 0, 0, 0);
        }
        backgroundColor.draw(c);
        trashIcon.draw(c);
    }

    private void swipeRight(View view, int margin, int top, int bottom, float dX) {
        // right swipe
        int left = view.getLeft() + margin;
        int right = view.getLeft() + margin + trashIcon.getIntrinsicWidth();

        // this makes sure that our icon swipes with the item until it reaches its final position.
        // It creates a cool look. If you don't want this you can just comment it out.
        int iconConstraint = (view.getLeft() + ((int) dX) < right + margin)
                ? (int) dX - trashIcon.getIntrinsicWidth() - (margin * 2) : 0;
        left += iconConstraint;
        right += iconConstraint;

        trashIcon.setBounds(left, top, right, bottom);
        backgroundColor.setBounds(view.getLeft(), view.getTop(), view.getLeft() + (int) dX, view.getBottom());
    }

    private void swipeLeft(View view, int margin, int top, int bottom, float dX) {
        // left swipe

        int left = view.getRight() - margin - trashIcon.getIntrinsicWidth();
        int right = view.getRight() - margin;

        int iconConstraint = (view.getRight() + ((int) dX) > left - margin)
                ? (int) dX + trashIcon.getIntrinsicWidth() + (margin * 2) : 0;
        left += iconConstraint;
        right += iconConstraint;

        trashIcon.setBounds(left, top, right, bottom);
        backgroundColor.setBounds(view.getRight() + (int) dX, view.getTop(), view.getRight(), view.getBottom());
    }
}
