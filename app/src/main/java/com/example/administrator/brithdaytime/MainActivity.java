package com.example.administrator.brithdaytime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;
    private String brithday = null;
    private static final String FILENAME = "info";
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        textView1 = findViewById(R.id.textView3);
        shared = getSharedPreferences(FILENAME, MODE_PRIVATE);
        textView.setText(shared.getString("brithday","未定义"));

        ClockView clockView = findViewById(R.id.myClock);
        clockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final Calendar calendar = Calendar.getInstance();
                final Date now = calendar.getTime();//当前时间
                final String d = sdf.format(now);

                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);
//                final int hour = calendar.get(calendar.HOUR);
//                final int minuter = calendar.get(calendar.MINUTE);
//                final int second = calendar.get(calendar.SECOND);
//                final int millisecond = calendar.get(calendar.MILLISECOND);

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        brithday = year + "-" + (++month) + "-" + day +" 00:00:00";//将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        try {
                            Date brithdayDate = sdf.parse(brithday);
                            calendar.setTime(brithdayDate);//设置时间

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long a = calendar.getTime().getTime();
                        long b = now.getTime();
//                        String b = now.toString();
                        long liveDate = (b - a);
                        String liveDate1 = sdf.format(liveDate);

                             //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        sharePreferenceBrith(brithday);
                        textView.setText(shared.getString("brithday","未定义"));
//                        brithday = sharedPreferences.getString("brithday", "");
//                        textView.setText("生日:"+brithday);
                        Toast.makeText(getApplicationContext(), d, Toast.LENGTH_LONG).show();
                        System.out.println("a=" + brithday);
                        System.out.println("b=" + b);
                        System.out.println("liveDate=" + liveDate1);
                        Toast.makeText(getApplicationContext(), year + " " + month + " " + day, Toast.LENGTH_LONG).show();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String brithdayText = textView.getText().toString();
                if (brithdayText == null) {
                    handler.sendEmptyMessage(0121);
                } else {
                    Message m = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("brithyear", brithdayText);
                    m.setData(bundle);
                    m.what = 0x121;
                    handler.sendMessage(m);
                }

            }
        }, 0, 1000);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x121) {
//                SimpleDateFormat format = new SimpleDateFormat("");
                Bundle b = msg.getData();
                String brith = b.getString("brithyear");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String now = simpleDateFormat.format(Calendar.getInstance().getTime());

                String oldernum = older(now, brith);


                String a = String.valueOf((Calendar.getInstance().getTimeInMillis()) / 1000);
                textView1.setText(oldernum);
            }
        }
    };

    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }
    public static long differentDaysByMillisecond(Date date, Date date1){
        long mills = date1.getTime() - date.getTime();
        return mills;
    }

    public static String older(String now, String brithyear) {//多少岁
        Date dateNow = null, datebrithyear = null, date1 = null, firstyear = null, lastdatebrith=null;
        String nowdatebrith, lastbrith;
        String nowyear = now.substring(0,4);
        String brthyear = brithyear.substring(4);
        nowdatebrith = nowyear+brthyear;//当前年份的生日日期
        lastbrith = String.valueOf(Integer.valueOf(nowyear)-1)+brthyear;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateNow = format.parse(now);
            datebrithyear = format.parse(brithyear);
            date1 = format.parse(nowdatebrith);
            firstyear = format.parse("1970-1-1 08:00:00");
            lastdatebrith = format.parse(lastbrith);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int brityNowDay = differentDays(datebrithyear, dateNow);//出生年生日到现在相差天数
        int brtyday = differentDays(datebrithyear, date1);//出生年生日到当前年生日
        int older = Integer.valueOf(nowyear)-Integer.valueOf(brithyear.substring(0,4));
        if(brityNowDay<brtyday){
            older -= 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.YEAR, -1);
        long othermills = differentDaysByMillisecond(firstyear, calendar.getTime());
        long mills = (System.currentTimeMillis()-othermills)/1000;
        long brithmills = differentDaysByMillisecond(lastdatebrith, date1);//从当前出生日期到下一年的出生日期的毫秒数
        System.out.println("aaaa"+older+"|"+brithmills+"|"
                +datebrithyear+"|"+mills+"|"+System.currentTimeMillis()/1000+"----"+othermills/1000+"++++"+calendar.getTime());
        System.out.println("datebrithyear:"+datebrithyear+"date1："+date1+"datenow:"+dateNow+"nowdatebrith:"+nowdatebrith+"calendar:"+calendar.getTime());
        if(mills==(brithmills/1000)){
            mills=0;
        }
        return String.valueOf(older)+"."+String.valueOf(mills);
    }
    public SharedPreferences sharePreferenceBrith(String brithday){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("brithday",brithday);
        editor.commit();
        return shared;
    }
}
