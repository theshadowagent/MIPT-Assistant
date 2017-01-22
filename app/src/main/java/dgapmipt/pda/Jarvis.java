package dgapmipt.pda;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Jarvis {
    public Context context;
    public ChatAdapter chatAdapter;

    public static int tasksLength = 11; // должно быть 11
    private static int defaultStage = 0;
    private static int defaultRound = 1;
    private static String[] QRTasks = {"GGKLIV", "BUAXPA", "RCAXFD", "HWRBGA", "JFUJIV", "NLPVAI", "YLXJTX", "BOERHS", "DGKRDK", "BFCIIT", "VINJKB"};
    private Task[] tasks;
    private List<String[]> cards;
    private Task currentTask;

    private int round;

    private int stage;
    private int teamNumber;
    private static int[][] taskSequences = {{0, 3, 4, 5, 6, 1, 8, 7, 9, 2, 10},
                                            {0, 2, 3, 4, 5, 6, 1, 8, 7, 9, 10},
                                            {0, 9, 2, 3, 4, 5, 6, 1, 8, 7, 10},
                                            {0, 7, 9, 2, 3, 4, 5, 6, 1, 8, 10},
                                            {0, 8, 7, 9, 2, 3, 4, 5, 6, 1, 10},
                                            {0, 1, 8, 7, 9, 2, 3, 4, 5, 6, 10},
                                            {0, 6, 1, 8, 7, 9, 2, 3, 4, 5, 10},
                                            {0, 5, 6, 1, 8, 7, 9, 2, 3, 4, 10}};
    public static int NFCTaskNumber = 8;
    public int[] currentTaskSequence;

    Jarvis(Context context, int teamNumber) {
        this.context = context;
        this.teamNumber = teamNumber;
        this.currentTaskSequence = taskSequences[teamNumber];
        loadProgress();
        loadTasks();
        currentTask = tasks[currentTaskSequence[stage]];
    }

    private void loadTasks() {
        tasks = new Task[tasksLength];
        //String[] taskNames = context.getResources().getStringArray(R.array.taskNamesArray);
        String[] taskDescriptions = context.getResources().getStringArray(R.array.descriptionTasks);
        for (int i = 0; i < tasksLength; i++) {
            tasks[i] = new Task();
            tasks[i].id = i;
            tasks[i].code = QRTasks[i];
            //tasks[i].name = taskNames[i];
            tasks[i].description = taskDescriptions[i];
        }
    }

    public Task getNextTask() {
        return tasks[currentTaskSequence[stage+1]];
    }

    public boolean codeExists(String incomingMessage) {
        return incomingMessage.contains(currentTask.code);
    }

    public String getAnswerMessage(String incomingMessage) {
        if (incomingMessage == null) {
            return context.getString(R.string.firstMessage);
        } else if (incomingMessage.toLowerCase().equals(context.getString(R.string.nextRound))) {
            if (round < 5 && currentTask.id == NFCTaskNumber) {
                round += 1;
                return context.getString(R.string.roundNext) + " " +round + ".";
            } else return context.getString(R.string.donotknow);
        }

        Task task;
        boolean end = false;
        if (stage + 1 < tasksLength) {
            task = getNextTask();
        }
        else {
            task = currentTask;
            end = true;
        }

        if (incomingMessage.contains(currentTask.code)) {
            if (!end) {
                stage += 1;
                currentTask = task;
                return task.description;
            } else return context.getString(R.string.end);
        } else return context.getString(R.string.donotknow);
    }

    public String getNFCMessage(String id) {
        if (cards == null) {
            AssetManager assetManager = context.getAssets();
            InputStream csvStream = null;
            try {
                csvStream = assetManager.open("cards_data.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream, Charset.forName("UTF-8"));
            CSVReader csvReader = new CSVReader(csvStreamReader);
            try {
                csvReader.readNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cards = new ArrayList<String[]>();
            String[] nextLine;
            try {
                while ((nextLine = csvReader.readNext()) != null) {
                    cards.add(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < cards.size(); i++) {
            String[] card = cards.get(i);
            if (id.equals(card[1]))
                return card[1 + round];
        }
        return context.getString(R.string.NFCnotrecognized);
    }

    public void loadProgress() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        stage = sharedPreferences.getInt("stage", defaultStage);
        round = sharedPreferences.getInt("round", defaultRound);
    }

    public void saveProgress() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("round", round);
        editor.putInt("stage", stage);
        editor.apply();
    }

    public void setChatAdapter(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }
}
