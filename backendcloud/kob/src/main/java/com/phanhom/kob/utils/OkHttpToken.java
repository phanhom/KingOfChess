package com.phanhom.kob.utils;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import java.io.*;
import java.util.List;
import java.util.Map;

public class OkHttpToken {
    public static final String API_KEY = "XZLWugk4m92fpBvp2KcWrhmi";
    public static final String SECRET_KEY = "qi4CuedAvASgOtEFxtz2IjFO30rkssjX";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static boolean checkText(String text) throws IOException{
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "text=" + text);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseStr = response.body().string();

        // 解析 JSON 数据
        JSONObject jsonObject = JSONObject.parseObject(responseStr);

        // 提取 conclusion 字段
        String conclusion = jsonObject.getString("conclusion");

        if ("不合规".equals(conclusion)) {
            System.out.println("不合规");
            return false;
        }
        System.out.println("合规");
        return true;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSONObject.parseObject(response.body().string()).getString("access_token");
    }
}
