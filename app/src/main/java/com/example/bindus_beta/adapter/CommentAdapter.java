package com.example.bindus_beta.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bindus_beta.R;
import com.example.bindus_beta.data.Comment;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comment> commentList;
    String name; //db의 텍스트가 내꺼인지 상대거인지 확인하기 위해 로그인된 아이디와 텍스트의 아이디 일치여부를 체크.
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvComment;
        public TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.comment_name);
            tvComment = itemView.findViewById(R.id.comment_content);
            tvTime = itemView.findViewById(R.id.comment_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(ArrayList<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
//        Log.d("ChatAdapter", email+"/"+String.valueOf(mChat.size()));
    }

    @Override
    public int getItemViewType(int position) {
        // position 위치의 아이템 타입 리턴.
//        if (mChat.get(position).getEmail().equals(stEmail)){ //아이디가 내이메일과 같다면 1을 리턴 아니면 2를리턴
//            return MY_MESSAGE;
//        } else {
//            return YOUR_MESSAGE;
//        }
        return position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvName.setText(commentList.get(position).getWriter());
        holder.tvComment.setText(commentList.get(position).getContent());
//        String time= commentList.get(position).getTime().toString();
        long sec= commentList.get(position).getTime().getSeconds()*1000;
        holder.tvTime.setText(String.valueOf(getTimeDate(sec)));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}

