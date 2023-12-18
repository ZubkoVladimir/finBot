package com.example.demotelegrambot.Command;

import lombok.SneakyThrows;

import java.io.File;
import java.util.*;


public class Currencies {






    @SneakyThrows
    public static HashSet<String> abbreviation(){
        HashSet<String> cur = new HashSet<>();
        Scanner scan = new Scanner(new File("src/main/resources/currencies.txt"));
        while (scan.hasNextLine()) {

            cur.add(scan.nextLine());
        }
        scan.close();


        return cur;
    }



}
