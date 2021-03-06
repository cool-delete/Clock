package lovejazzie.clock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;
    public static String TAG = "CLOCK";
    private StopWatchView stopWatchView;
    private TimerView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("计时器").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("秒表").setContent(R.id.tabStopWatch));


        stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);
        timerView = (TimerView) findViewById(R.id.tabTimer);
    }

    @Override
    protected void onDestroy() {
        stopWatchView.onDestroy();
        timerView.stopTask();
        Log.d(TAG, "onDestroy() returned ");
        super.onDestroy();
    }
}
