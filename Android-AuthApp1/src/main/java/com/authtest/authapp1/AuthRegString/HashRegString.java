package com.authtest.authapp1.AuthRegString;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashMap;


public class HashRegString {
    public static String main(String str){

//Step 1: Defining object of HashMap Class
        HashMap<String,Integer> map=new HashMap<String,Integer>();

//Step 2: Adding Key Value pair

        map.put("1", 4919);
        map.put("2", 5030);
        map.put("3", 5212);
        map.put("4", 5696);
        map.put("5", 5939);
        map.put("6", 6171);
        map.put("7", 6414);
        map.put("8", 6595);
        map.put("9", 6838);
        map.put("0", 7090);


        String[]crt = str.split("");
        String sum = "";

        for(int i = 0; i < crt.length; i ++){
            sum += map.get(crt[i]);
        }



        String str_x = TextUtils.join("", Collections.singleton(sum));
        String str_y = str_x.substring(4);
        System.out.println(str_y + " :str_y");

       return HashExpString.extract(str_y);

    }

}

