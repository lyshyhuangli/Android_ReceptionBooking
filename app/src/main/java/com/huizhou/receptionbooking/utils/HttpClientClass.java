package com.huizhou.receptionbooking.utils;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/8/30.
 */


public class HttpClientClass
{
    public static String httpPost(Object req, String method)
    {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            String spec = "http://" + ServerConfig.RESTFUL_API_IP + ":" + ServerConfig.RESTFUL_API_PORT + "/restfulApi/" + method;
            URL url = new URL(spec);
            urlConnection = (HttpURLConnection) url.openConnection();

             /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

             /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setRequestProperty("Charsert", "UTF-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);
            //wr.writeBytes(jsonString);
            wr.write(jsonString.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            // try to get response
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200)
            {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = inputStream.read(buffer)) != -1)
                {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                inputStream.close();
                baos.close();
                // 返回字符串
                String response = new String(baos.toByteArray());
                return response;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    /**
     * POST请求操作
     */
    public static String httpPost3(String params, String method)
    {
        String result = null;

        try
        {

            // 请求的地址
            String spec = "http://" + ServerConfig.RESTFUL_API_IP + ":" + ServerConfig.RESTFUL_API_PORT + "/restfulApi/" + method;
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
//            // 传递的数据
//            String data = "username=" + URLEncoder.encode(userName, "UTF-8")
//                    + "&userpass=" + URLEncoder.encode(userPass, "UTF-8");
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            urlConnection.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );
            // 设置请求的头
            urlConnection.setRequestProperty(
                    "Content-Length",
                    String.valueOf(params.getBytes().length)
            );
            // 设置请求的头
            urlConnection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0"
                    );

            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            if (urlConnection.getResponseCode() == 200)
            {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1)
                {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                result = new String(baos.toByteArray());

                System.out.println("链接........." + result);

//                // 通过runOnUiThread方法进行修改主线程的控件内容
//                LoginActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 在这里把返回的数据写在控件上 会出现什么情况尼
//                        tv_result.setText(result);
//                    }
//                });

            }
            else
            {
                System.out.println("链接失败.........");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
