package top.exql.wb.batchsms;

import android.telephony.SmsManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmsUtil {

    public static void sendSMS(String phoneNumber, String message) {

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
    public static List<String> getText(Integer textId){
        HttpURLConnection connection;
        BufferedReader in = null;
        List<String> resultText = new ArrayList<>();
        try {
            URL url = new URL("http://hackweek.tk/getText/" + textId);
            connection = (HttpURLConnection) url.openConnection();
//        } catch (Exception e) {

        StringBuilder result = new StringBuilder();

//        try {
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();


            Map<String, List<String>> map = connection.getHeaderFields();
            //遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
                resultText.add(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        } finally {        // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        System.out.println(resultText);

        return resultText;
    }
}
