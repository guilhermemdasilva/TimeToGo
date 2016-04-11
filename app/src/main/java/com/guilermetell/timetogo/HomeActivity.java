package com.guilermetell.timetogo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class HomeActivity extends FragmentActivity implements TimePickerDialogCloseListener {

    public static final String SELECTED_HOUR = "SELECTED_HOUR";
    public static final String SELECTED_MINUTE = "SELECTED_MINUTE";
    public static final String SELECTED_WORK_HOUR = "SELECTED_WORK_HOUR";
    public static final String SELECTED_WORK_MINUTE = "SELECTED_WORK_MINUTE";
    public static final int UNKNOWN_TIME = 0;

    private Button entranceBtn;
    private Button workHoursBtn;
    private TextView timeToGoTV;

    private void doUpdate() {
        int entranceHour = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_HOUR, UNKNOWN_TIME);
        int entranceMinute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_MINUTE, UNKNOWN_TIME);
        int workHour = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_WORK_HOUR, 8);
        int workMinute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_WORK_MINUTE, 13);
        entranceBtn.setText(twentyFourHourTime(entranceHour, entranceMinute));
        entranceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v, SELECTED_HOUR);
            }
        });
        workHoursBtn.setText(twentyFourHourTime(workHour, workMinute));
        workHoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v, SELECTED_WORK_HOUR);
            }
        });
        int outHour = (entranceHour + workHour + 1);
        int outMinute = (entranceMinute + workMinute);
        if (outMinute > 60) {
            outMinute = entranceMinute + workMinute - 60;
            outHour++;
        }
        if (outHour > 24)
            outHour -= 24;
        timeToGoTV.setText(twentyFourHourTime(outHour, outMinute));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        entranceBtn = (Button) findViewById(R.id.entrance_btn);
        workHoursBtn = (Button) findViewById(R.id.workhours_btn);
        timeToGoTV = (TextView) findViewById(R.id.timeToGo);
        doUpdate();
//        createHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static String twentyFourHourTime(int hour, int minute) {
        String time;
        time = (hour < 10) ? "0"+hour+":" : hour+":";
        time += (minute < 10) ? "0"+minute : minute+"";
        return time;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private void showTimePickerDialog(View v, String id) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        doUpdate();
    }

//    private void createHandler() {
//        Thread thread = new Thread() {
//            public void run() {
//                Looper.prepare();
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Calendar c = Calendar.getInstance();
//                        int hour = c.get(Calendar.HOUR);
//                        int minute = c.get(Calendar.MINUTE);
//                        int entranceHour = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_HOUR, UNKNOWN_TIME);
//                        int entranceMinute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SELECTED_MINUTE, UNKNOWN_TIME);
//                        if (hour >= entranceHour && minute >= entranceMinute) {
//                            Vibrator v = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
//                            v.vibrate(500);
//                        }
//                        handler.removeCallbacks(this);
//                        Looper.myLooper().quit();
//                    }
//                }, 2000);
//
//                Looper.loop();
//            }
//        };
//        thread.start();
//    }
}
