package dgapmipt.pda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Chat {
    private Context context;
    private List<Message> messages;
    private ChatAdapter chatAdapter;
    private boolean changed = false;
    private DBHelper dbHelper;
    private Jarvis jarvis;

    Chat(Context context, Jarvis jarvis) {
        this.context = context;
        this.jarvis = jarvis;
        dbHelper = new DBHelper(this.context);
    }

    public void sendMessage(boolean isUserSender, String text) {
        Date date = Calendar.getInstance().getTime();
        Message message = new Message(isUserSender, text, date);
        if (messages == null) messages = new ArrayList<>();
        messages.add(message);
        if (!isUserSender) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
        changed = true;
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
            chatAdapter.scrollDownChat();
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setChatAdapter(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    public void loadChatFromHistory() {
        messages = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_CHAT, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int textIndex = cursor.getColumnIndex(DBHelper.KEY_MESSAGE);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int userSenderIndex = cursor.getColumnIndex(DBHelper.KEY_USER_SENDER);
            do {
                boolean isUserSender = (cursor.getInt(userSenderIndex) == 1);
                Message message = new Message(isUserSender, cursor.getString(textIndex), new Date(cursor.getLong(dateIndex)));
                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (chatAdapter != null) chatAdapter.notifyDataSetChanged();
    }

    public void saveChatHistory() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CHAT, null, null);

        for (int i = 0; i < messages.size(); i++) {
            int isUserSender = (messages.get(i).userIsSender) ? 1 : 0;
            String text = messages.get(i).text;
            Date date = messages.get(i).date;

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_MESSAGE, text);
            contentValues.put(DBHelper.KEY_DATE, date.getTime());
            contentValues.put(DBHelper.KEY_USER_SENDER, isUserSender);

            database.insert(DBHelper.TABLE_CHAT, " ", contentValues);
        }
    }

    public boolean isChanged() {
        return changed;
    }


}
