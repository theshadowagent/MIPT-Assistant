package dgapmipt.pda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private int teamNumber;

    private EditText teamNumberView;
    private Button startButton;
    private int realTeamNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        teamNumber = sharedPreferences.getInt("teamNumber", -1);
        Intent intent;
        if (teamNumber != -1) {
            intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("teamNumber", teamNumber);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_start);
            startButton = (Button) findViewById(R.id.buttonStart);
            teamNumberView = (EditText) findViewById(R.id.teamNumberEditText);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamNumber = Integer.valueOf(teamNumberView.getText().toString()) - 1;
                    if (teamNumber < 0 || teamNumber > 7) {
                        teamNumberView.setError(getString(R.string.errorTeamNumber));
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("teamNumber", teamNumber);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("teamNumber", teamNumber);
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        }
    }
