package dgapmipt.pda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private RecyclerView recyclerView;
    private Chat chat;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView messageView;
        TextView timeView;


        public ViewHolder(View v) {
            super(v);
            rootView = v;
            messageView = (TextView) rootView.findViewById(R.id.message);
            timeView = (TextView) rootView.findViewById(R.id.time);
        }
    }

    ChatAdapter(Chat chat, RecyclerView recyclerView) {
        this.chat = chat;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (!chat.getMessages().get(position).userIsSender)
            return 0;
        else return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.message_viewholder, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.message_user_viewholder, parent, false);
                break;
            default:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.message_viewholder, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        holder.messageView.setText(chat.getMessages().get(i).text);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        holder.timeView.setText(dateFormat.format(chat.getMessages().get(i).date));
    }

    public void scrollDownChat() {
        recyclerView.smoothScrollToPosition(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        if (chat.getMessages() != null)
            return chat.getMessages().size();
        else return 0;
    }
}