package top.exql.wb.batchsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_LIST_VIEW = 1;

    private Boolean isSending = false;
    private List<String> data = new ArrayList<String>(){{
        String[] str = new String[]{"test1", "test2", "test3", "test4"};
        for(String s: str)add(s);
    }};
    private Integer alreadySend = 0;
    private Integer sendFailed = 0;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_LIST_VIEW:
                    ListView listView = findViewById(R.id.list_view);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, data);
                    listView.setAdapter(adapter);
                    toastNotice(String.format("成功：%s，失败：%s", alreadySend, sendFailed));
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btSend = findViewById(R.id.button);
        Button btImport = findViewById(R.id.buttonImport);


        btImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView = findViewById(R.id.list_view);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);

//                TextView resultStatusView = findViewById(R.id.resultStatus);
//                resultStatusView.setText(String.format("成功导入：%s", String.valueOf(data.length)));
                Toast.makeText(MainActivity.this, String.format("成功导入：%s", String.valueOf(data.size())), Toast.LENGTH_SHORT).show();
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (!isSending){
                    // 检查权限
                    isSending = true;
                    checkAndRequestPermission();

                    Thread sendSMSThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 获取手机号码
                            TextView editPhoneNum = findViewById(R.id.editPhoneNum);
                            String phoneNum = String.valueOf(editPhoneNum.getText());
                            Log.i("info", phoneNum);

                            while(data.size() > 0){
                                // 开始发送
                                try{
                                    SmsUtil.sendSMS("10086", "0000");
                                    alreadySend = alreadySend + 1;
                                }catch (Exception e){
                                    sendFailed = sendFailed + 1;
                                    e.printStackTrace();
                                }

                                data.remove(0);

                                // 重新渲染ListView
                                Message msg = new Message();
                                msg.what = UPDATE_LIST_VIEW;
                                handler.sendMessage(msg);

                                // 获取发送间隔时间
                                TextView editInterval = (TextView) findViewById(R.id.editInterval);
                                Integer interval = Integer.valueOf(String.valueOf(editInterval.getText()))*1000;
                                try {
                                    Thread.sleep(interval);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            Thread.currentThread().interrupt();
                            alreadySend = 0; sendFailed = 0;
                        }
                    });
                    sendSMSThread.start();

                }
            }
        });
    }


    public void checkAndRequestPermission(){
        if(!(checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 666);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults){
        Map<String, Integer> perms = new HashMap<>();
        int index = 0;
        for (String permission : permissions){
            perms.put(permission, grantResults[index++]);
        }
        for(int i = 0; i < perms.size(); i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"错误：权限没有完全获取！", Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
            }
        }
    }

    public void toastNotice(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
