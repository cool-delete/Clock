package lovejazzie.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Administrator on 2015/11/17.
 */
public class AlarmPlay extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_play);
        playMusic();
        StopAlarm();
        // TODO: 2015/11/18 好单调 在xml里面增加按钮或滑动
    }

    private void StopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(this,
                getIntent().getIntExtra("getResultCode", 0),
                new Intent(this, AlarmReceiver.class), 0));
    }

    private void playMusic() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}