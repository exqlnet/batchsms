package top.exql.wb.batchsms;

import android.telephony.SmsManager;

public class SmsUtil {

    public static void sendSMS(String phoneNumber,String message){

        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
//        List<String> divideContents = smsManager.divideMessage(message);
//        for (String text : divideContents) {
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//        }
    }

//    public static void sendOneSMS(){
//        SmsManager smsManager = SmsManager.getDefault();
//        String text =
//        smsManager.sendTextMessage();
//    }
}
