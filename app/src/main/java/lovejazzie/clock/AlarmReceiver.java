package lovejazzie.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("闹钟执行了");
        Log.d(MainActivity.TAG, "接受到广播 准备启动play界面");
        int r = intent.getIntExtra("getResultCode", 0);
        Intent intent1 = new Intent(context, AlarmPlay.class);
        intent1.putExtra("getResultCode", r);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        intent1 = null;
    }
}