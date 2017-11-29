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


    public static Double r1(int a,int b){
        return a+0.382*(b-a);
    }
    public static Double r2(int a,int b){
        return a+0.618*(b-a);
    }

    public static void getMinValue(){
        int a = 0;
        int b = 1;
        Double r1 = r1(a,b);
        Double r2 = r2(a,b);

        if(funGX(r1) > funGX(r2)){
            //令 a的值转换为a1,
        }
    }


    public static void main(String args[]){
//        for(Double x=0D;x<=1;x+=0.01){
//            System.out.println(x+"  ||  "+formatData(funFX(x)));
//        }

        //在区间 [a,b] 进行第一次 r1, r2 取值


    }






















}
