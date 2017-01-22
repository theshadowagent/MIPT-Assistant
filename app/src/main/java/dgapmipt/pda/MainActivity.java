package dgapmipt.pda;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int teamNumber;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private NfcAdapter nfcAdapter;

    private Jarvis jarvis;
    private Chat chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTeamNumber(getIntent());

        initialize();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        assert viewPager != null;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            private void hiddenKeyboard(View v) {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        checkNFC();
    }

    private void checkNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Dialog dialog = new SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNoNFC,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        } else if (!nfcAdapter.isEnabled()) {
            Dialog dialog = new SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNFCNotEnabled,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    public void setTeamNumber(Intent intent) {
        teamNumber = intent.getIntExtra("teamNumber", 0);
    }

    private void handleIntent(Intent intent) {
        String scanResult = intent.getStringExtra("scanResult");
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            String id = bytesToHexString(tag.getId());
            chat.sendMessage(false, jarvis.getNFCMessage(id));
        } else if (scanResult != null) {
            chat.sendMessage(true, scanResult);
            chat.sendMessage(false, jarvis.getAnswerMessage(scanResult));
        }
    }

    private void initialize() {
        if (jarvis == null) {
            jarvis = new Jarvis(this, teamNumber);
        }
        if (chat == null) {
            chat = new Chat(this, jarvis);
            chat.loadChatFromHistory();
            if (chat.getMessages().size() == 0) chat.sendMessage(false, jarvis.getAnswerMessage(null));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkNFC();

        handleIntent(getIntent());

        initialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jarvis.saveProgress();
    }

    // Transform bytes to string to get Tag ID; no idea how this works
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AssistantFragment.newInstance(jarvis, chat);
                default:
                    return AssistantFragment.newInstance(jarvis, chat);
                case 1:
                    return MapsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.sectionThree);
                case 1:
                    return getString(R.string.sectionTwo);
            }
            return null;
        }
    }
}
