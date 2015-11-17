package lovejazzie.clock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/11.
 */
public class TimerView extends LinearLayout implements TextWatcher, View.OnClickListener {
    private Button btnStart, btnPause, btnResume, btnReset;
    private EditText etHour, etMin, etSec;
    private Button[] btns = {btnStart, btnPause, btnResume, btnReset};
    private int[] btnId = {R.id.btnStart, R.id.btnPause, R.id.btnResume, R.id.btnReset};
    private EditText[] editTexts = {etHour, etMin, etSec};
    private int[] etId = {R.id.et_House, R.id.et_Min, R.id.et_Sec};
    private int allTimes;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private static final int WHAT_TIME_UP = 1;
    private String TAG = MainActivity.TAG;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context) {

        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAll();
    }

    private void initAll() {


        for (int i = 0; i < btnId.length; i++) {
            btns[i] = (Button) findViewById(btnId[i]);
        }


        for (int i = 0; i < btns.length; i++) {
            btns[i].setOnClickListener(this);
        }
        changeBtnStatus(0, false);
        for (int i = 0; i < etId.length; i++) {
            editTexts[i] = (EditText) findViewById(etId[i]);
            editTexts[i].setText(R.string.初始值);
            editTexts[i].addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() == 0) {
            Log.d(TAG, "无内容检查");
            changeBtnStatus(0, false);
            return;

        }
        int ss = Integer.parseInt(s.toString());


        if (ss > 59) {
            for (int i = 0; i < etId.length; i++) {
                editTexts[i].setText("59");
            }

        }
        if (ss < 0) {
            for (int i = 0; i < etId.length; i++) {
                editTexts[i].setText(R.string.初始值);
            }
        }


        //
        //            allTimes = Integer.parseInt(editTexts[0].getText().toString()) * 60 * 60
        //                    + Integer.parseInt(editTexts[1].getText().toString()) * 60
        //                    + Integer.parseInt(editTexts[2].getText().toString());
        if (isOk()) {
            Log.d(TAG, "检查其余两个edittext");
            changeBtnStatus(1, true);
        } else if (timerTask == null) {
            changeBtnStatus(0, false);
        }


    }

    private boolean isOk() {
        if (!(TextUtils.isEmpty(editTexts[0].getText().toString())
                || TextUtils.isEmpty(editTexts[1].getText().toString())
                || TextUtils.isEmpty(editTexts[2].getText().toString()))) {

            return Integer.parseInt(editTexts[0].getText().toString()) != 0 ||
                    Integer.parseInt(editTexts[1].getText().toString()) != 0 ||
                    Integer.parseInt(editTexts[2].getText().toString()) != 0
                            && timerTask == null;
        } else {
            return false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void changeBtnStatus(int status, boolean able) {
        switch (status) {
            case 0:
                Log.d(TAG, "changeBtnStatus() returned " + 0);
                btns[0].setVisibility(VISIBLE);
                btns[0].setEnabled(able);
                btns[1].setVisibility(GONE);
                btns[2].setVisibility(GONE);
                btns[3].setVisibility(GONE);
                break;


            case 1:
                if (!isOk()) {
                    return;
                } else {
                    Log.d(TAG, "changeBtnStatus() returned " + 1);
                    btns[0].setEnabled(able);
                    break;
                }


            case 2:
                Log.d(TAG, "changeBtnStatus() returned " + 2);
                btns[0].setVisibility(GONE);
                btns[1].setVisibility(VISIBLE);
                btns[2].setVisibility(GONE);
                break;


            case 3:
                Log.d(TAG, "changeBtnStatus() returned " + 3);
                btns[0].setVisibility(GONE);
                btns[1].setVisibility(GONE);
                btns[2].setVisibility(VISIBLE);
                btns[3].setVisibility(VISIBLE);

                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStart:
                startTimer();
                Log.d(TAG, "onClick() returned " + btnStart);
                changeBtnStatus(2, false);
                break;
            case R.id.btnPause:
                stopTask();
                changeBtnStatus(3, false);
                break;
            case R.id.btnResume:
                startTimer();
                changeBtnStatus(2, false);
                break;
            case R.id.btnReset:
                stopTask();
                resetTimer();
                changeBtnStatus(0, false);
                break;

        }

    }

    private void resetTimer() {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setEnabled(true);//解锁输入框
            Log.d(TAG, "解锁输入框");
            editTexts[i].setText(R.string.初始值);
            editTexts[i].setCursorVisible(true);
        }
    }

    private void startTimer() {
        Log.d(TAG, "锁定输入框");
        for (int i = 0; i < etId.length; i++) {
            editTexts[i].setEnabled(false);//锁定输入框
            editTexts[i].setTextColor(Color.parseColor("#000000"));

        }


        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(WHAT_TIME_UP);
                }
            };


            timer.schedule(timerTask, 1000, 1000);


        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_TIME_UP:
                    showTimer();
                    break;
            }
        }
    };

    private void showTimer() {
        int sec = Integer.parseInt(editTexts[2].getText().toString());
        if (sec > 0) {
            editTexts[2].setText(String.format("%02d", (sec - 1)));
        }
        if (sec <= 0) {
            int min = Integer.parseInt(editTexts[1].getText().toString());
            if (min > 0) {
                editTexts[1].setText(String.format("%02d", min - 1));
                editTexts[2].setText(String.format("%02d", 59));
            } else {
                editTexts[1].setText(R.string.初始值);
                editTexts[2].setText(R.string.初始值);

            }
            if (min <= 0) {
                int hour = Integer.parseInt(editTexts[0].getText().toString());
                if (hour > 0) {
                    editTexts[0].setText(String.format("%02d", (hour - 1)));
                    editTexts[1].setText(String.format("%02d", 59));
                    editTexts[2].setText(String.format("%02d", 59));
                } else {
                    stopTask();
                    resetTimer();
                    showDialog();
                    changeBtnStatus(0, false);
                }
            }
        }

    }

    private void showDialog() {
        new AlertDialog.Builder(getContext()).setTitle("倒数完了~~").setMessage("这个是什么消息").setNegativeButton("ok", null).show();
    }

    private void stopTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;

        }
    }
}