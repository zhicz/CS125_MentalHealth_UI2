package com.example.cs125_mentalhealth_ui;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

public class send_http_request {
    private String resp;
    private String phone = "";
    private String major = "";
    private String age = "";
    private int gender = 0;
    private String city = "";
    private String state = "";
    private String country = "";

    public void insert_recom_rating(int user_id, JSONObject data, String session, String recom_type){
        String api_url = "http://health.ordinaryzone.com";
        int count = 0;
        while(true) {
            try {
                count++;
                String action = "/data/" + user_id + "/insert";
                String url_param = "user_id=" + user_id + "&session=" + session +
                        "&data=" + data.toString() +
                        "&data_type=" + recom_type;

                byte[] postData = url_param.getBytes("UTF-8");
                int postDataLen = postData.length;
                URL insert_data_url = new URL(api_url + action);

                HttpURLConnection con = (HttpURLConnection) insert_data_url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", Integer.toString(postDataLen));

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(postData);

                resp = response(con.getInputStream());
                if (resp.contains("Data Insert Successful")) {
                    System.out.println("Data Insert Successful");
                }else{
                    System.out.println("Unnable to insert data");
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                if(count >= 10){
                    break;
                }
            }
        }
    }

    public JSONObject get_recom(int user_id, String session){
        String api_url = "http://health.ordinaryzone.com/user/" + user_id + "/recomendation";
        String url_param = "user_id=" + user_id + "&session=" + session;
        int count = 0;
        while(true) {
            try {
                byte[] postData = url_param.getBytes("UTF-8");
                int postDataLen = postData.length;
                URL recom_url= new URL(api_url);

                HttpURLConnection con = (HttpURLConnection) recom_url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(false);
                con.setDoInput(true);
                con.setRequestProperty("charset", "utf-8");

                con.setRequestProperty("Content-Length", Integer.toString(postDataLen));
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(postData);

                resp = response(con.getInputStream());

                System.out.println("Received Response" + resp);
                if (resp.contains("Recomendation Successful")) {
                    JSONObject recom_data = new JSONObject(resp).getJSONObject("data");
                    System.out.println(recom_data.getString("link"));
                    return recom_data;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                count++;
                if(count >= 10){
                    return null;
                }
            }
        }
    }

    public boolean update_user_preference(JSONObject info, String preference){
        String api_url = "http://health.ordinaryzone.com";
        int count = 0;
        while(true) {
            try {
                String action = "/user/" + info.getInt("id") + "/update";
                String url_param = "user_id=" + info.getInt("id") + "&session=" + info.getString("session") + "&prefer=" + preference;
                byte[] postData = url_param.getBytes("UTF-8");
                int postDataLen = postData.length;
                URL update_prefer_url = new URL(api_url + action);
                HttpURLConnection con = (HttpURLConnection) update_prefer_url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", Integer.toString(postDataLen));

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(postData);

                resp = response(con.getInputStream());
                if (resp.contains("User Information Edit Successful")) {
                    System.out.println(url_param);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                count++;
                if(count >= 10){
                    return false;
                }
            }
        }
    }

    public boolean insert_survey_data(JSONObject info, int score, String date){
        String api_url = "http://health.ordinaryzone.com";
        int count = 0;
        while(true) {
            try {
                count++;
                String action = "/data/" + info.getInt("id") + "/insert";
                String data = new JSONObject().put("score",score).put("date",date).put("user_id",info.getInt("id")).toString();
                String url_param = "user_id=" + info.getInt("id") + "&session=" + info.getString("session") +
                        "&data=" + data +
                        "&data_type=survey";

                byte[] postData = url_param.getBytes("UTF-8");
                int postDataLen = postData.length;
                URL insert_data_url = new URL(api_url + action);

                HttpURLConnection con = (HttpURLConnection) insert_data_url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", Integer.toString(postDataLen));

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(postData);

                resp = response(con.getInputStream());
                if (resp.contains("Data Insert Successful")) {
                    System.out.println("Data Insert Successful: " + score + ":" + date);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                if(count >= 10){
                    return false;
                }
            }
        }
    }

    protected boolean connect_api(String username, String password, String action, String connectSucceed)
    {
        while(true) {
            try {
                String api_url = "http://health.ordinaryzone.com" + action;
                String url_param = "username=" + username + "&password=" + password;
                if(action == "/user/create_account"){
                    url_param = update_param(url_param);
                }
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

                resp = response(con.getInputStream());
                if (resp.contains(connectSucceed)) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                System.out.println("FAILED Connection");
            }
        }
    }

    public String get_resp(){
        return resp;
    }

    public String update_param(String url_param){
        if(gender != 0){
            url_param += "&gender=" + gender;
        }
        if(age.equals("") == false){
            url_param += "&age=" + age;
        }
        if(phone.equals("") == false){
            url_param += "&telephone=" + phone;
        }
        if(major.equals("") == false){
            url_param += "&major=" + major;
        }
        if(country.equals("") == false){
            url_param += "&country=" + country;
        }
        if(state.equals("") == false){
            url_param += "&state=" + state;
        }
        if(city.equals("") == false){
            url_param += "&city=" + city;
        }
        return url_param;
    }

    public void set_param(String phone, String major, String age, int gender, String city, String state, String country){
        this.phone = phone;
        this.major = major;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    protected String response(InputStream in){
        BufferedReader reader = null;
        String resp = "";
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            int count = 0;
            while((line = reader.readLine()) != null){
                resp += line;
                count++;
                if(count >= 20){
                    System.out.println(resp);
                    break;
                }
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

    private URI addParam(URI uri, String urlParameters, int value) {
        StringBuilder query = new StringBuilder();
        try {
            query.append(urlParameters).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            /* As URLEncoder are always correct, this exception
             * should never be thrown. */
            throw new RuntimeException(ex);
        }

        try {
            return new URI(uri.getScheme(), uri.getAuthority(),
                    uri.getPath(), query.toString(), null);
        } catch (URISyntaxException ex) {
            /* As baseUri and query are correct, this exception
             * should never be thrown. */
            throw new RuntimeException(ex);
        }
    }
}
