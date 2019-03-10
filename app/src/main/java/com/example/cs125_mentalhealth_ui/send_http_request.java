package com.example.cs125_mentalhealth_ui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class send_http_request {

    protected boolean connect_api(String username, String password, String action, String connectSucceed)
    {
        while(true) {
            try {
                String api_url = "http://health.ordinaryzone.com" + action;
                String url_param = "username=" + username + "&password=" + password;
                byte[] postData = url_param.getBytes("UTF-8");
                int postDataLen = postData.length;
                URL login_url = new URL(api_url);

                HttpURLConnection con = (HttpURLConnection) login_url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", Integer.toString(postDataLen));

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(postData);

                String resp = response(con.getInputStream());
                if (resp.contains(connectSucceed)) {
                    System.out.println(resp);
                    return true;
                }
                return false;
            } catch (Exception e) {
                System.out.println("FAILED Connection");
            }
        }
    }

    protected String response(InputStream in){
        BufferedReader reader = null;
        String resp = "";
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                resp += line;
            }
            return resp;
        }catch(IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
