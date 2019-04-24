package ru.whobuys.vovch.notaset;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebCall {

    private NotASetApplication provider;

    private static final String DATA_1 = "uname";
    private static final String DATA_2 = "upassword";
    private static final String DATA_3 = "third";
    private static final String ACTION = "action";
    private static final String DATA_JSON = "data_json";

    private static final String NO_INTERNET = "400";
    private static final String ONLINE_ACTIONS_DENIED_INFORMER = "000";

    public WebCall() {
    }

    public void setApplication(NotASetApplication application){
        this.provider = application;
    }

    public String callServer(Object... loginPair) {

        String response = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(provider.getString(R.string.controller_page));

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);                                                 // Enable POST stream
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            HashMap<String, String> postDataParams = new HashMap<String, String>();

            postDataParams.put(DATA_1, (String) loginPair[0]);
            postDataParams.put(DATA_2, (String) loginPair[1]);
            postDataParams.put(DATA_3, (String) loginPair[2]);
            postDataParams.put(ACTION, (String) loginPair[3]);
            postDataParams.put(DATA_JSON, (String) loginPair[4]);

            InputStream is = null;
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                response += String.valueOf(responseCode);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response += String.valueOf(responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            if(e instanceof IOException) {
                if (loginPair[3].equals("itemmark")) {
                    StringBuilder tempString = new StringBuilder("");
                    tempString.append("400");
                    tempString.append(loginPair[1]);
                    response = tempString.toString();
                }
            } else{
                response = "700";
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        if(response.equals("")){
            response = ONLINE_ACTIONS_DENIED_INFORMER;
        }
        return response;
    }


    public static String getStringFromJsonString(String jsonString, String value) {
        String result = null;
        if (jsonString != null && value != null) {
            try {
                JSONObject dataHolder = new JSONObject(jsonString);
                result = dataHolder.getString(value);
            } catch (Exception e) {                                                                         //TODO

            }
            return result;
        } else {
            return null;
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
