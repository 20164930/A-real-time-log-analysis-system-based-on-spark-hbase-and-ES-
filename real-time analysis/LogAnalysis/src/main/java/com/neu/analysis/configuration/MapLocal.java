package com.neu.analysis.configuration;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MapLocal {
    private static HashMap<String,String> map=new HashMap<>();
    public static void setMap(){
        ClassPathResource classPathResource = new ClassPathResource("map.txt");
        BufferedReader br;
        try {
            InputStream inputStream = classPathResource.getInputStream();
            br=new BufferedReader(new InputStreamReader(inputStream));
            String s="";
            int i=1;
            while((s=br.readLine())!=null){
                map.put(String.valueOf(i),s);
                i++;
            }
            br.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMap(String id){
        return map.get(id);
    }
}
