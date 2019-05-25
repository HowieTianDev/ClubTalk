package com.howietian.clubtalk.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.howietian.clubtalk.R;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {
    private Context mContext;
    private List<String> mTypeLists;
    private OnTypeItemClickListener mListener;

    public TypeAdapter(Context context, List<String> typeLists, OnTypeItemClickListener listener) {
        mContext = context;
        mTypeLists = typeLists;
        mListener = listener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text_single, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        holder.fill(mTypeLists.get(position),mListener);
    }

    @Override
    public int getItemCount() {
        if (mTypeLists == null) {
            return 0;
        }
        return mTypeLists.size();
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_type);
        }

        private void fill(final String text, final OnTypeItemClickListener listener) {
            mTextView.setText(text);

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(text);
                    }
                }
            });
        }
    }

    public interface OnTypeItemClickListener {
        void onClick(String type);
    }
}
