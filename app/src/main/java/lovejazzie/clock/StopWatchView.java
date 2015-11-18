package lovejazzie.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/16.
 */
public class StopWatchView extends LinearLayout implements View.OnClickListener {
    Button btnStart, btnPause, btnResume, btnLap, btnReset;
    Button[] btns = {btnStart, btnPause, btnResume, btnLap, btnReset};
    int[] btnId = {R.id.btnSWStart, R.id.btnSWPause, R.id.btnSWResume, R.id.btnSWLap, R.id.btnSWReset};
    TextView Hour, Min, Sec, MSec;
    TextView[] tvs = {Hour, Min, Sec, MSec};
    int[] tvId = {R.id.timeHour, R.id.timeMin, R.id.timeSec, R.id.MinSec};
    int Time = 0;



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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSWStart:
                startStopWatch();
                break;
            case R.id.btnSWPause:

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
        handler.sendEmptyMessage(0);
        calculate();
    }

    private void freshTime() {//外部显示数值

        tvs[3].setText(String.format("%03d", MSec));
        tvs[2].setText(String.format("%02d", Sec));
        tvs[1].setText(String.format("%02d", Min));
        tvs[0].setText(String.format("%02d", Hour));
    }

    private void calculate() {//内部计算数值
        Time++;
        int MSec = Time % 1000;
        int Sec = Time / 1000 % 60;
        int Min = Time / 1000 / 60 % 60;
        int Hour = Time / 1000 / 60 / 60;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            freshTime();
            sendEmptyMessageDelayed(0, 1);
        }
    };
}