package com.lyft.java.v1;

import com.lyft.java.v1.Quote;
import com.lyft.java.v1.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


@Controller
public class ConsumeController {
    //Quote quot1 = new Quote();

    @RequestMapping("/consume")
    public String consume(Model model) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("https://data.cityofchicago.org/resource/4wp6-czhu.json");

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            JSONArray list = new JSONArray(responseBody); //Convert String to JSON Object
            System.out.println("---------------response body-------------------------");
           System.out.println(responseBody);
            System.out.println("-------------------response body end---------------------");
      //      JSONArray tokenList = result.getJSONArray("Value");
            int len = list.length();
           // String[] test = new String[len];
            List<Map<String, String>> test = new ArrayList<Map<String, String>>();

            for(int i = 0; i<len;i++){
                System.out.println("-------------------json body in loop index: "+i+"---------------------");
                HashMap<String, String> map = new HashMap<>();
                JSONObject obj = list.getJSONObject(i);

                System.out.println(obj);
                System.out.println("ADDRESS: "+obj.getString("address"));
                JSONObject location = obj.getJSONObject("location");
                JSONArray coordinates = location.getJSONArray("coordinates");
                String index0 = coordinates.getString(0);
                model.addAttribute("name", index0);
                System.out.println("LAT: "+index0);

                map.put("address",obj.getString("address"));
                map.put("latitude",coordinates.getString(0));
                map.put("longitude",coordinates.getString(1));
                test.add(map);
            }

            model.addAttribute("list", test);
            System.out.println("-------------------json body end---------------------");
            httpclient.close();
            return "consume";
        } catch(Exception e){
            System.out.println("*********EXCEPTION*********");
            System.out.println(e);
        }finally {
           // httpclient.close();
        }
        return "consume";

    }
}
