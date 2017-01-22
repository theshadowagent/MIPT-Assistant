package dgapmipt.pda;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class SimpleDialogBuilder extends AlertDialog.Builder {

    public SimpleDialogBuilder(Context context, int messageId, Boolean cancelable, int posTextId,
                               int negTextId, DialogInterface.OnClickListener posButton,
                               DialogInterface.OnClickListener negButton) {
        super(context);
        setMessage(context.getText(messageId));
        setCancelable(cancelable);
        setNegativeButton(context.getText(posTextId), posButton);
        setPositiveButton(context.getText(negTextId), negButton);
    }

    public SimpleDialogBuilder(Context context, int titleId, int messageId, Boolean cancelable,
                               int posTextId, int negTextId, DialogInterface.OnClickListener posButton,
                               DialogInterface.OnClickListener negButton) {
        super(context);
        setTitle(context.getText(titleId));
        setMessage(context.getText(messageId));
        setCancelable(cancelable);
        setNegativeButton(context.getText(posTextId), posButton);
        setPositiveButton(context.getText(negTextId), negButton);
    }

    public SimpleDialogBuilder(Context context, CharSequence message, Boolean cancelable, CharSequence posText,
                               CharSequence negText, DialogInterface.OnClickListener posButton,
                               DialogInterface.OnClickListener negButton) {
        super(context);
        setMessage(message);
        setCancelable(cancelable);
        setNegativeButton(posText, posButton);
        setPositiveButton(negText, negButton);
    }

    public SimpleDialogBuilder(Context context, CharSequence title, CharSequence message, Boolean cancelable,
                               CharSequence posText, CharSequence negText,
                               DialogInterface.OnClickListener posButton,
                               DialogInterface.OnClickListener negButton) {
        super(context);
        setTitle(title);
        setMessage(message);
        setCancelable(cancelable);
        setNegativeButton(posText, posButton);
        setPositiveButton(negText, negButton);
    }

    public SimpleDialogBuilder(Context context) {
        super(context);
    }

    public SimpleDialogBuilder(Context context, int titleId, int messageId) {
        super(context);
        setTitle(context.getText(titleId));
        setMessage(context.getText(messageId));
    }

    public SimpleDialogBuilder(Context context, CharSequence title, CharSequence message) {
        super(context);
        setTitle(title);
        setMessage(message);
    }

    public SimpleDialogBuilder(Context context, int titleId, int messageId,
                               DialogInterface.OnClickListener posButton) {
        super(context);
        setTitle(context.getText(titleId));
        setMessage(context.getText(messageId));
        setNegativeButton("OK", posButton);
    }

    public SimpleDialogBuilder(Context context, CharSequence title, CharSequence message,
                               DialogInterface.OnClickListener posButton) {
        super(context);
        setTitle(title);
        setMessage(message);
        setNegativeButton("OK", posButton);
    }
}
