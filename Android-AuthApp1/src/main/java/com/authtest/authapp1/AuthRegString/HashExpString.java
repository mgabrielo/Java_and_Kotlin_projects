package com.authtest.authapp1.AuthRegString;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HashExpString {

    public static String extract(String str2){

       return newSplit(str2);
    }


    public static String newSplit(String newStr) {


        ArrayList<String> array = new ArrayList<String>();

        HashMap<String,String>  em_map=new HashMap<String,String>();


        em_map.put("4919", "1F648");
        em_map.put("5030", "1F637");
        em_map.put("5212", "1F635");
        em_map.put("5696", "1F62A");
        em_map.put("5939", "1F624");
        em_map.put("6171", "1F30D");
        em_map.put("6414", "1F31E");
        em_map.put("6595", "1F3JE");
        em_map.put("6838", "1F40B");
        em_map.put("7090", "1F55E");



        int i = 0;
        while(i<newStr.length())
        {
            int j=i+4;
            if(j>newStr.length())
                j=newStr.length();
            array.add(newStr.substring(i,j));
            i=j;
        }

        System.out.println(array);

        HashMap<String,String>  pw_map=new HashMap<String,String>();


        pw_map.put(array.get(0),em_map.get(array.get(0)));
        pw_map.put(array.get(1),em_map.get(array.get(1)));
        pw_map.put(array.get(2),em_map.get(array.get(2)));
        pw_map.put(array.get(3),em_map.get(array.get(3)));
        pw_map.put(array.get(4),em_map.get(array.get(4)));
        pw_map.put(array.get(5),em_map.get(array.get(5)));
        pw_map.put(array.get(6),em_map.get(array.get(6)));
        pw_map.put(array.get(7),em_map.get(array.get(7)));



        String sums = "";

        for(int a = 0; a < array.size(); a ++){
            sums += pw_map.get(array.get(a));
        }

        String str_a = TextUtils.join("", Collections.singleton(sums));
        String lastTwelveChars = "";
        lastTwelveChars = str_a.substring(0, 39);
        System.out.println(lastTwelveChars + " :str_a" );


        return lastTwelveChars;

    }




}
