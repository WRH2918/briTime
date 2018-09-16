package com.example.administrator.brithdaytime;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        ClockView clockView= findViewById(R.id.myClock);
        final EditText inputText = new EditText(MainActivity.this);

        inputText.setFocusable(true);
        clockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);
                final int hour = calendar.get(calendar.HOUR);
                final int minuter = calendar.get(calendar.MINUTE);
                final int second = calendar.get(calendar.SECOND);
                final int millisecond = calendar.get(calendar.MILLISECOND);



                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        textView.setText(year+"-"+(++month)+"-"+day+"-"+hour+"-"+minuter+"-"+second+"-"+millisecond);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        Toast.makeText(getApplicationContext(),year+" "+month+" "+day,Toast.LENGTH_LONG).show();
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(MainActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();





//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("添加时间");
//                builder.setMessage("hello");
//                builder.setView(inputText);
//                builder.setPositiveButton("我知道", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String text = inputText.getText().toString();
//                        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
//                    }
//                });

//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });
    }
}
