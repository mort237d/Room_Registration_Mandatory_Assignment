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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

        if (dataItem.getClass() == Room.class){
            Room room = (Room) data.get(position);
            holder.roomName.setText(room.getName());
            holder.capacityOrUser.setText("# " + room.getCapacity());
            if (room.getRemarks() != null) holder.remarksOrTime.setText("✎ \"" + room.getRemarks() + "\"");
            else holder.remarksOrTime.setText("✎ \"No remarks\"");
            holder.descriptionOrPurpose.setText("✎ \"" + room.getDescription() + "\"");

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
        else if (dataItem.getClass() == Reservation.class){
            Reservation reservation = (Reservation) data.get(position);

            RoomService roomService = ApiUtils.getRoomService();
            Call<Room> GetRoom = roomService.getRoom(reservation.getRoomId());
            GetRoom.enqueue(new Callback<Room>(){

                @Override
                public void onResponse(Call<Room> call, Response<Room> response) {
                    Room room = response.body();
                    holder.roomName.setText("Reservation of " + room.getName());
                }

                @Override
                public void onFailure(Call<Room> call, Throwable t) {
                    Log.e(TAG, "onFailure: ");
                }
            });

            holder.capacityOrUser.setText("☺ \"" + reservation.getUserId() + "\"");

            String pattern = "MM-dd-yyyy HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String fromTime = simpleDateFormat.format(new Date((long) reservation.getFromTime()*1000));
            String toTime = simpleDateFormat.format(new Date((long) reservation.getToTime()*1000));
            holder.remarksOrTime.setText("⏱ " + fromTime + " --> " + toTime);
            holder.descriptionOrPurpose.setText("✎ \"" + reservation.getPurpose() + "\"");
        }
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
        TextView capacityOrUser;
        TextView remarksOrTime;
        TextView descriptionOrPurpose;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            roomName = itemView.findViewById(R.id.room_name);
            capacityOrUser = itemView.findViewById(R.id.capacity_or_user);
            remarksOrTime = itemView.findViewById(R.id.remarks_or_time);
            descriptionOrPurpose = itemView.findViewById(R.id.description_or_purpose);
        }
    }
}
