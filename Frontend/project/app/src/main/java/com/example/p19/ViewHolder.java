package com.example.p19;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvDesc;
        public LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.userName);
            tvDesc = itemView.findViewById(R.id.desc);
            layout = itemView.findViewById(R.id.userLayout);
        }
    }

