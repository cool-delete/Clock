package lovejazzie.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/11/4.
 */
public class TimeView extends LinearLayout {
    TextView tvTime;

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeView(Context context) {

        super(context);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText("hello");
        timeHandler.sendEmptyMessage(0);

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            timeHandler.sendEmptyMessage(0);
        } else {
            timeHandler.removeMessages(0);
        }
    }

    private void refreshTime() {
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(String.format
                ("%02d:%02d:%02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),//这些成员变量都是static 不用新建对象就能用
                        calendar.get(Calendar.SECOND)));


    }


    private Handler timeHandler = new Handler() { 
        @Override
        public void handleMessage(Message msg) {
            refreshTime();
            if (getVisibility() == View.VISIBLE) {
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }


    };
}