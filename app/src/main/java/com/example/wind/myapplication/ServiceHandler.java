/** Steve
 * Use this class to connect to the website and pull the data
 */
package com.example.wind.myapplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;
import java.io.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ServiceHandler {
    public ServiceHandler() {

    }

    public String makeServiceCall(String sAddress) throws IOException
    {
        StringBuilder sb = new StringBuilder();

        BufferedInputStream bis = null;
        URL url = new URL(sAddress);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode;

        con.setConnectTimeout( 10000 );
        con.setReadTimeout( 10000 );

        responseCode = con.getResponseCode();

        if ( responseCode == 200)
        {
            bis = new java.io.BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
            String line = null;

            while ((line = reader.readLine()) != null)
                sb.append(line);

            bis.close();
        }

        return sb.toString();
    }
}
