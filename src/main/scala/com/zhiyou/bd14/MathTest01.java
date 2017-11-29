package com.zhiyou.bd14;

import java.text.DecimalFormat;

public class MathTest01 {

    Double x = 0D;
    public static Double acc = 0.001D;

    public static Double funFX(Double x) {
        Double result = Math.pow(Math.sin(x), 6)
                * Math.tan(1 - x)
                * Math.pow(Math.E, 30 * x);
        return result;
    }
    public static Double funGX(Double x) {
        Double result = -Math.pow(Math.sin(x), 6)
                * Math.tan(1 - x)
                * Math.pow(Math.E, 30 * x);
        return result;
    }


    public static Double formatData(Double x) {
        DecimalFormat df = new DecimalFormat("00.00E000");
        return Double.valueOf(df.format(x));
    }



    public static Double x1(Double min, Double max) {
        return a + 0.382 * (b - a);
    }
    public static Double x2(Double min, Double max) {
        return a + 0.618 * (b - a);
    }



    public static Double a = 0D;
    public static Double b = 1D;
    public static Double x1 = x1(a, b);   //取0.382位置的值
    public static Double x2 = x2(a, b);   //取0.618位置的值, 横向取值

    //初始区间 [0,1], 初始化x1, x2的值
    public static void getMinValue(Double min, Double max) {


        if (funGX(x1) > funGX(x2)) {    //纵向求值, 比较大小
            // 靠近 a的函数值大, 消去靠近a的部分
            //令 a的值转换为x1,
            a = x1;
            // x1 = x2 , (funGX(x1) = funGX(x2))
            // 然后 x2的值从新计算, 还用x2的方法计算
            // 这一步完成了 a值的变换, 和x1值的变换, x2值的变换, b的值不变
            x1 = x2;
            x2 = x2(a,b);
            if(Math.abs(a-b) < acc){
                System.out.println("结果为x=:"+0.5*(Math.abs(a+b))
                        +" g(x)=:"+funGX(0.5*(Math.abs(a+b))));
            }else{
                getMinValue(a, b);
            }
        }else {
            //靠近b的函数值较大, 消去靠近b的部分
            // 令b 的值转化为 x2,
            // x2 的值左移, 转换为原来x1 的值
            b = x2;
            // 然后 x1的值重新计算, 还用x1 的计算方法
            // 这一步完成了 b值的变换, x1值的变换, x2值的变换, a的值不变
            x2 = x1;
            x1 = x1(a,b);
            if(Math.abs(a-b) < acc){
                System.out.println("结果为x=:"+0.5*(Math.abs(a+b))
                        +" g(x)=:"+funGX(0.5*(Math.abs(a+b))));
            }else{
                getMinValue(a, b);
            }
        }
    }


    public static void main(String args[]) {
//        for(Double x=0D;x<=1;x+=0.01){
//            System.out.println(x+"  ||  "+formatData(funGX(x)));
//        }
        //在区间 [a,b] 进行第一次 r1, r2 取值
        getMinValue(x1,x2);
    }

}
