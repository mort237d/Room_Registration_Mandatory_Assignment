package com.example.roomregistration_mandatoryassignment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewSimpleAdapter<T> extends RecyclerView.Adapter<RecyclerViewSimpleAdapter<T>.ViewHolder> {
    private static final String LOG_TAG = "ROOMS";
    private final List<T> data;
    private final int viewId = View.generateViewId();

    private Context mContext;

    public RecyclerViewSimpleAdapter(Context mContext, List<T> data) {
        this.mContext = mContext;
        this.data = data;
        Log.d(LOG_TAG, data.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        Log.d(LOG_TAG, view.toString());
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T dataItem = data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        Log.d(LOG_TAG, "onBindViewHolder called " + position);

        Room room = (Room) data.get(position);
        holder.roomName.setText(room.toString());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick: " + data.get(position));

                Room room = (Room) data.get(position);
                Log.d(LOG_TAG, room.toString());
                Intent intent = new Intent(v.getContext(), SingleRoomActivity.class);
                intent.putExtra(SingleRoomActivity.ROOM, room);
                Log.d(LOG_TAG, "putExtra " + room.toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parentLayout;
        TextView roomName;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            roomName = itemView.findViewById(R.id.room_name);
        }
    }
}
