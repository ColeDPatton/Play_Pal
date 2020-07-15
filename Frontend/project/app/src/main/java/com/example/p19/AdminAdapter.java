package com.example.p19;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.p19.ViewHolder;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder>{

    private ArrayList<User> userList;
    private Context context;

    /**
     * Constructor for the RecyclerView Adapter
     * @param userList List of users to be added into the RecyclerView
     * @param context Context for where the RecyclerView should be placed
     */
    public AdminAdapter(ArrayList<User> userList, Context context){
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_user_item, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int num = i;
        viewHolder.tvName.setText(userList.get(i).getName());
        viewHolder.tvDesc.setText(userList.get(i).getGamertag());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra("user", userList.get(num).toJsonString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * This class assigns the data to the corresponding fields nested within the RecyclerView
     */

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

}
