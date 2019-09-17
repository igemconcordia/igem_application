package com.example.quantifen;

import java.util.ArrayList;

public class User {

    private static int id;
    private static double fenData;

    public static void setId(int newId){
        id = newId;
    }

    public static int getId(){
        return id;
    }

    public static void setFenData(double newFen){
        fenData = newFen;
    }
}
