package dgapmipt.pda;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class AssistantFragment extends Fragment {
    private RecyclerView recyclerView;
    private Chat chat;
    private ChatAdapter chatAdapter;
    private EditText enterMessageEditText;
    private ImageView sendButton;

    private Jarvis jarvis;

    private int teamNumber;

    public static AssistantFragment newInstance(Jarvis jarvis, Chat chat) {
        AssistantFragment fragment = new AssistantFragment();
        fragment.jarvis = jarvis;
        fragment.chat = chat;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        teamNumber = sharedPreferences.getInt("teamNumber", 0);
        if (jarvis == null) {
            jarvis = new Jarvis(getContext(), teamNumber);
        }
        if (chat == null) {
            chat = new Chat(getContext(), jarvis);
            chat.loadChatFromHistory();
            if (chat.getMessages().size() == 0) chat.sendMessage(false, jarvis.getAnswerMessage(null));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, // like an onCreate method in Activities
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assistant, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        chatAdapter = new ChatAdapter(chat, recyclerView);
        chat.setChatAdapter(chatAdapter);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.requestFocus();
        enterMessageEditText = (EditText) rootView.findViewById(R.id.enter_message);
        sendButton = (ImageView) rootView.findViewById(R.id.send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = clean(enterMessageEditText.getText().toString());
                if (!text.isEmpty()) {
                    chat.sendMessage(true, text);
                    chat.sendMessage(false, jarvis.getAnswerMessage(text));
                    enterMessageEditText.setText("");
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
            }
        });
        return rootView;
    }

    private String clean(String text) {
        text = text.trim();
        text = text.replace(" +", " ");
        return text;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (chat.isChanged())
            chat.saveChatHistory();
        jarvis.saveProgress();
    }
}
