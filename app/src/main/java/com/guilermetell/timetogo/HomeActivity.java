package com.guilermetell.timetogo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends FragmentActivity implements TimePickerDialogCloseListener {

    public static final String SELECTED_HOUR = "SELECTED_HOUR";
    public static final String SELECTED_MINUTE = "SELECTED_MINUTE";
    public static final String SELECTED_WORK_HOUR = "SELECTED_WORK_HOUR";
    public static final String SELECTED_WORK_MINUTE = "SELECTED_WORK_MINUTE";
    public static final int UNKNOWN_TIME = 0;

    private Button entranceBtn;
    private Button workHoursBtn;
    private Button cameraWorkBtn;
    private ShimmerTextView timeToGoTV;
    private Shimmer shimmer;

    private PendingIntent pendingIntent;

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
        setupAlarm(outHour, outMinute);
    }

    public void setupAlarm(int hour, int minute) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pendingIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        entranceBtn = (Button) findViewById(R.id.entrance_btn);
        workHoursBtn = (Button) findViewById(R.id.workhours_btn);
        timeToGoTV = (ShimmerTextView) findViewById(R.id.timeToGo);
        cameraWorkBtn = (Button) findViewById(R.id.camerawork_btn);
        cameraWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
        doUpdate();
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name));
        if (!folder.exists())
            folder.mkdir();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer = new Shimmer();
        shimmer.start(timeToGoTV);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
    }

    private static String twentyFourHourTime(int hour, int minute) {
        String time;
        time = (hour < 10) ? "0"+hour+":" : hour+":";
        time += (minute < 10) ? "0"+minute : minute+"";
        return time;
    }

    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat now = new SimpleDateFormat("dd-MM-yy");
        File photo = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name), now.format(new Date()).toString()+".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    try {
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
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

}
