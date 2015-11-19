package lovejazzie.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/16.
 */
public class StopWatchView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = MainActivity.TAG;
    Button btnStart, btnPause, btnResume, btnLap, btnReset;
    Button[] btns = {btnStart, btnPause, btnResume, btnLap, btnReset};
    int[] btnId = {R.id.btnSWStart, R.id.btnSWPause, R.id.btnSWResume, R.id.btnSWLap, R.id.btnSWReset};
    TextView tvHour, tvMin, tvSec, tvMSec;
    TextView[] tvs = {tvHour, tvMin, tvSec, tvMSec};
    int[] tvId = {R.id.timeHour, R.id.timeMin, R.id.timeSec, R.id.MinSec};
    int Time = 0;
    int MSec, Sec, Min, Hour;
    Timer timer = new Timer();
    TimerTask showtime = null;
    TimerTask timerTask = null;
    Handler handler;

    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initCom();
    }


    private void initCom() {
        for (int i = 0; i < btnId.length; i++) {
            btns[i] = (Button) findViewById(btnId[i]);
            btns[i].setOnClickListener(this);
        }
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = (TextView) findViewById(tvId[i]);
        }
        initTimerTask();

    }

    private void initTimerTask() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "目前有" + Time + "个10");
                    Time++;
                }
            };
        }
        if (showtime == null) {
            showtime = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);

                }
            };
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSWStart:
                initTimerTask();
                startStopWatch();
                break;
            case R.id.btnSWPause:
                onDestroy();
                break;
            case R.id.btnSWResume:

                break;
            case R.id.btnSWLap:

                break;
            case R.id.btnSWReset:

                break;
        }
    }

    private void startStopWatch() {




        timer.schedule(timerTask, 10, 10);
        timer.schedule(showtime, 200, 200);

    }


    private void freshTime() {//外部显示数值
        MSec = Time % 100;
        tvs[3].setText(String.format("%02d", MSec));
        Log.d(TAG, "目前显示毫秒数应该是" + Time % 100 + ">>>>>>>>>>>");
        Sec = Time / 100 % 60;
        tvs[2].setText(String.format("%02d", Sec));
        Min = Time / 100 / 60 % 60;
        tvs[1].setText(String.format("%02d", Min));
        Hour = Time / 100 / 60 / 60;
        tvs[0].setText(String.format("%02d", Hour));

    }

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                freshTime();
            }
        };
    }

    public void onDestroy() {
        handler.removeMessages(0);
        if (showtime != null) {
            showtime.cancel();
            timerTask.cancel();
            showtime = null;
            timerTask = null;
        }
    }
}