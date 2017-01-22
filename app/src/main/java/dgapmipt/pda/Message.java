package dgapmipt.pda;

import java.util.Date;


public class Message {
    public boolean userIsSender;
    public String text;
    public Date date;

    public Message(boolean isUserSender, String text, Date date) {
        this.userIsSender = isUserSender;
        this.text = text;
        this.date = date;
    }

}
