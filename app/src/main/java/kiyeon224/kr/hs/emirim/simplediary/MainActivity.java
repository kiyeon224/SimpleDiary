package kiyeon224.kr.hs.emirim.simplediary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DatePicker date;
    EditText edit;
    Button but;
    String fileName;
    FileInputStream fIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = (DatePicker)findViewById(R.id.date_pick);
        edit = (EditText)findViewById(R.id.edit);
        but = (Button)findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream fOut = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str = edit.getText().toString();
                    fOut.write(str.getBytes());
                    fOut.close();
                    Toast.makeText(MainActivity.this, "정상적으로 " + fileName + "파일이 저장되었습니다.", Toast.LENGTH_LONG).show();
              } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);  // Calender.Date == Calender.DAY_OF_MONTH

        date.init(year, month, day, new DatePicker.OnDateChangedListener() {  //month에 +1 안하는거임!!!! 헷갈리지 말기!
            @Override                                                         //new ~는 date가 바뀌었을 때 실행됨
            public void onDateChanged(DatePicker view, int year, int month, int day) {  //바뀐 날짜가 넘어옴
                fileName = year + "_" + (month+1) + "_" + day + ".txt";
                String readData = readDiary(fileName);
                edit.setText(readData);
                but.setEnabled(true);
            }
        });
    }

    public String readDiary(String fileName) {
        String diaryStr = null;
        try {
            fIn = openFileInput(fileName);
            byte[] buf = new byte[500];
            fIn.read(buf);
            diaryStr = new String(buf).trim();  //trim : 앞에있는 공백을 제거(1)   1_오늘은 신난다_2
            but.setText("수정하기");
        } catch (FileNotFoundException e) {
            edit.setText("일기가 없습니다");
            but.setText("새로 저장");
        } catch (IOException e) {

        }

        try {
            fIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diaryStr;
    }
}
