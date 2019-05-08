package top.exql.wb.batchsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btSend = findViewById(R.id.button);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查权限
                checkAndRequestPermission();
                try{
                    SmsUtil.sendSMS("10086", "0000");
                    Toast.makeText(MainActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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
}
