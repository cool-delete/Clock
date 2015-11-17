package lovejazzie.clock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/11/16.
 */
public class StopWatchView extends LinearLayout implements View.OnClickListener {
    Button btnStart, btnPause, btnResume, btnLap, btnReset;
    Button[] btns = {btnStart, btnPause, btnResume, btnLap, btnReset};
    int[] btnid = {R.id.btnSWStart, R.id.btnSWPause, R.id.btnSWResume, R.id.btnSWLap, R.id.btnSWReset};


    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initBtn();
    }

    private void initBtn() {
        for (int i = 0; i < btnid.length; i++) {
            btns[i] = (Button) findViewById(btnid[i]);
            btns[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSWStart:

                break;
        }
    }
}