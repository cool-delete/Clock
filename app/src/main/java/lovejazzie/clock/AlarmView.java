package lovejazzie.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/11/4.
 */
public class AlarmView extends LinearLayout {

    private static final String ALARM_KEY = "Alarm List";
    private ArrayAdapter<AlarmDate> adapter;
    public static final int A_DATE = 86400000;
    private AlarmManager alarmManager;


    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        IntiAlarmService();
    }

    public AlarmView(Context context) {
        super(context);
        IntiAlarmService();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        InitButtonAndList();
        ReadSavedAlarmList();
    }

    private void InitButtonAndList() {
        ListView lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
        adapter = new ArrayAdapter<AlarmView.AlarmDate>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);//listView用这个方法

        Button btnAddAlarm = (Button) findViewById(R.id.btn_AddAlarm);
        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmButton();
            }
        });
        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        DeleteAlarm(position);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }).setNegativeButton("取消", null).show();


                return true;
            }
        });
    }


    private void IntiAlarmService() {
        if (isInEditMode()) {
            return;
        }
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }


    private void AddAlarmButton() {

        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                CheckAlarmTime(GetAlarmTime(hourOfDay, minute));
            }
        }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
    }


    private void DeleteAlarm(int position) {
        alarmManager.cancel(PendingIntent.getBroadcast(
                getContext(),
                adapter.getItem(position).getid(),
                new Intent(getContext(), AlarmReceiver.class),
                0));
        Log.d(MainActivity.TAG, "删除方法执行了,id是:" + adapter.getItem(position).getid());
        adapter.remove(adapter.getItem(position));
        SaveAlarmTime();
    }


    private Calendar GetAlarmTime(int hourOfDay, int minute) {
        Calendar AlarmTime = Calendar.getInstance();

        AlarmTime.set(Calendar.HOUR, hourOfDay);
        AlarmTime.set(Calendar.MINUTE, minute);
        AlarmTime.set(Calendar.SECOND, 0);
        AlarmTime.set(Calendar.MILLISECOND, 0);
        return AlarmTime;
    }


    private void CheckAlarmTime(Calendar alarmTime) {

        Calendar nowTime = Calendar.getInstance();//当前时间

        if (alarmTime.getTimeInMillis() <= nowTime.getTimeInMillis())//比当前时间少的话
        {
            alarmTime.setTimeInMillis(alarmTime.getTimeInMillis() + A_DATE);
        }//往后推一天

        AlarmDate alarmDate = new AlarmDate(alarmTime.getTimeInMillis());
        adapter.add(alarmDate);
                /*
                把闹钟时间传入AlarmDate对象
                然后用adapter对象的add方法
                把AlarmDate对象[会用这个对象执行对象里面的toString方法]
                传入listView
                */
        SetAlarmService(alarmTime, alarmDate);

        SaveAlarmTime();

    }


    private void SetAlarmService(Calendar alarmTime, AlarmDate alarmDate) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        Log.d(MainActivity.TAG, "按钮按下设置时间" + getContext());
        int code = alarmDate.getid();
        intent.putExtra("getResultCode", code);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime.getTimeInMillis(),
                60*1000,//A_DATE,//第二参:延后多少时间再启动
                PendingIntent.getBroadcast(getContext(),
                        alarmDate.getid(),
                        intent,
                        0)
        );
        intent = null;
        Log.d(MainActivity.TAG, "设置闹钟方法执行了,id是:" + alarmDate.getid());
    }


    private void SaveAlarmTime() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer stringBuffer = new StringBuffer();


        if (adapter.getCount() == 0) {
            editor.putString(ALARM_KEY, null).commit();
            return;
        }


        for (int i = 0; i < adapter.getCount(); i++) {//adapter如果没有内容这里不会执行
        /* 产生bug*/
            stringBuffer.append(adapter.getItem(i).getTime()).append(",");
        }


        String content = stringBuffer.toString().substring(0, stringBuffer.length() - 1);

        editor.putString(ALARM_KEY, content).commit();


    }


    private void ReadSavedAlarmList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sharedPreferences.getString(ALARM_KEY, null);

        if (content != null) {
            String[] timeString = content.split(",");
            for (String string : timeString) {
                adapter.add(new AlarmDate(Long.parseLong(string)));
            }

        }

    }
    // TODO: 2015/11/11 间隔时间太短,会造成正在播放的铃声被再次启动的Activity覆盖 而调用finish方法 尽快修复

    private static class AlarmDate {
        private String timeLabel = "";//这个String对象会作为listView里面的TextView的内容显示出来
        private Calendar date;
        private long time;


        public AlarmDate(Long time) {
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLabel = String.format("%d月%d日,%02d:%02d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
        }

        public long getTime() {
            return time;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        public int getid() {
            return (int) (getTime() / 60 / 1000);
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }//adapter对象会调用这个AlarmDate对象的这个方法
    }


}