package com.zhiyou.bd14;

import java.awt.*;
import java.text.DecimalFormat;

public class MathTest01 {

    Double x = 0D;
    public static Double funFX(Double x){
        Double result = Math.pow(Math.sin(x),6)
                *Math.tan(1-x)
                *Math.pow(Math.E,30*x);
        return result;
    }

    public static Double funGX(Double x){
        Double result = -Math.pow(Math.sin(x),6)
                *Math.tan(1-x)
                *Math.pow(Math.E,30*x);
        return result;
    }
    public static Double formatData(Double x){
        DecimalFormat df = new DecimalFormat("00.00E000");
        return Double.valueOf(df.format(x));
    }

    public static void main(String args[]){
        for(Double x=0D;x<=1;x+=0.01){
            System.out.println(x+"  ||  "+formatData(funFX(x)));
        }

    }






















}
