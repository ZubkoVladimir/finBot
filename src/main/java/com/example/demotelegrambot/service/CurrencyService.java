package com.example.demotelegrambot.service;

import com.example.demotelegrambot.service.model.CurrencyModel;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class CurrencyService {

    private static String getFormatDate(CurrencyModel model){
        return new SimpleDateFormat("dd MMM yyyy").format(model.getDate());
    }

    public static String getCurrencyRate(String message, CurrencyModel model) throws IOException, ParseException{

        URL url = new URL("https://api.nbrb.by/exrates/rates/"+message+"?parammode=2");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        while (scanner.hasNext()){
            result+= scanner.nextLine();
        }
        JSONObject obj = new JSONObject(result);

        model.setCur_ID(obj.getInt("Cur_ID"));
        model.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(obj.getString("Date")));
        model.setCur_Abbreviation(obj.getString("Cur_Abbreviation"));
        model.setCur_Scale(obj.getInt("Cur_Scale"));
        model.setCur_Name(obj.getString("Cur_Name"));
        model.setCur_OfficialRate(obj.getDouble("Cur_OfficialRate"));



        return "Official rate of BYN to " + model.getCur_Abbreviation() + "\n" +
                "on the date: " + getFormatDate(model) + "\n" + "is: " + model.getCur_OfficialRate() +
                " BYN per " + model.getCur_Scale() + " " +model.getCur_Abbreviation();
    }




}
