package com.adham.saveme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolderT> {

    Context context;
    List<ChatMessage> chatMessages;
    String senderId;

    public static final int VIEW_TYPE_SENT = 1 ;
    public static final int VIEW_TYPE_RECEIVED =2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages, String senderId) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }


    @NonNull
    @Override
    public MyViewHolderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new MyViewHolderT(LayoutInflater.from(context).inflate(R.layout.item_container_sent_message, parent, false));
        } else {
            return new MyViewHolderT(LayoutInflater.from(context).inflate(R.layout.item_container_recieved_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderT holder, int position) {

        holder.messageTxt.setText(chatMessages.get(position).getMessage());
        holder.dateTxt.setText(chatMessages.get(position).getDateTime());

    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class MyViewHolderT extends RecyclerView.ViewHolder {

        TextView messageTxt, dateTxt;

        public MyViewHolderT(View itemView) {
            super(itemView);

            //to display image, food title, name of the person
            messageTxt = itemView.findViewById(R.id.textMessage);
            dateTxt = itemView.findViewById(R.id.textDateTime);

        }
    }

}
