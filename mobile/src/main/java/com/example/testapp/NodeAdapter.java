package com.example.testapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NodeAdapter extends ListAdapter<String, NodeAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem,
                                                  @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            };

    NodeAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView view = (TextView) inflater.inflate(R.layout.textbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setText(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView view;

        ViewHolder(TextView view) {
            super(view);
            this.view = view;
        }

        void setText(String text) {
            view.setText(text);
        }
    }
}
