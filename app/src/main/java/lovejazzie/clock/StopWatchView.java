package lovejazzie.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/16.
 */
public class StopWatchView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = MainActivity.TAG;
    private Button btnStart, btnPause, btnResume, btnLap, btnReset;
    private Button[] btns = {btnStart, btnPause, btnResume, btnLap, btnReset};
    private int[] btnId = {R.id.btnSWStart, R.id.btnSWPause, R.id.btnSWResume, R.id.btnSWLap, R.id.btnSWReset};
    private TextView tvHour, tvMin, tvSec, tvMSec;
    private TextView[] tvs = {tvHour, tvMin, tvSec, tvMSec};
    private int[] tvId = {R.id.timeHour, R.id.timeMin, R.id.timeSec, R.id.MinSec};
    private int Time = 0;
    private int MSec, Sec, Min, Hour;
    private Timer timer = new Timer();
    private TimerTask showtime = null;
    private TimerTask timerTask = null;
    private Handler handler;
    private ArrayAdapter<String> myAdapter;

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
        changBtnStatus(0);
    }


    private void initCom() {
        for (int i = 0; i < btnId.length; i++) {
            btns[i] = (Button) findViewById(btnId[i]);
            btns[i].setOnClickListener(this);
        }
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = (TextView) findViewById(tvId[i]);
        }
        ListView listView = (ListView) findViewById(R.id.lvWatchTimeList);
        myAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item);
        listView.setAdapter(myAdapter);
        initTimerTask();

    }

    private void initTimerTask() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
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
                changBtnStatus(1);
                break;
            case R.id.btnSWPause:
                onDestroy();
                changBtnStatus(2);
                break;
            case R.id.btnSWResume:
                initTimerTask();
                startStopWatch();
                changBtnStatus(3);
                break;
            case R.id.btnSWLap:
                freshTime();
                myAdapter.insert(String.format("%02d:%02d:%02d.%02d", Hour, Min, Sec, MSec), 0);//放到最上面

                break;
            case R.id.btnSWReset:
                myAdapter.clear();
                for (TextView tv : tvs) {
                    tv.setText(R.string.初始值);
                }
                changBtnStatus(0);
                break;
        }
    }

    private void changBtnStatus(int i) {
        switch (i) {
            case 0:
                for (int k = 1; k < btns.length; k++) {
                    btns[k].setVisibility(GONE);

                }
                break;
            case 1:
                btns[0].setVisibility(GONE);
                btns[1].setVisibility(VISIBLE);
                btns[3].setVisibility(VISIBLE);
                break;
            case 2:
                btns[1].setVisibility(GONE);
                btns[3].setVisibility(GONE);
                btns[2].setVisibility(VISIBLE);
                btns[4].setVisibility(VISIBLE);
                break;
            case 3:
                btns[1].setVisibility(VISIBLE);
                btns[3].setVisibility(VISIBLE);
                btns[4].setVisibility(GONE);
                btns[2].setVisibility(GONE);
                break;

        }
    }

    private void startStopWatch() {
        timer.schedule(timerTask, 10, 10);
        timer.schedule(showtime, 200, 200);

    }
/* 分别计时
 * 按下lap按钮重新计时
 * 在下面的list的右边显示
 * 同时左边显示那个最快
 * 那个最慢
 * */

    private void freshTime() {//外部显示数值
        MSec = Time % 100;
        tvs[3].setText(String.format("%02d", MSec));
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