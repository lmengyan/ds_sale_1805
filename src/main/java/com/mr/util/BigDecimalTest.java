package com.mr.util;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.math.BigDecimal;

/**
 *
 * 在对金额计算的时候需要使用 BigDecimal
 * Created by Lmy on 2018/11/8.
 */
public class BigDecimalTest {

    public static void main(String[] args) {

        BigDecimal b1=new BigDecimal("0.2");
        BigDecimal b2=new BigDecimal(0.2f);
        BigDecimal b3=new BigDecimal(0.2d);

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);



        BigDecimal b4= new BigDecimal("2");
        BigDecimal b5= new BigDecimal("5");

        //加
        BigDecimal add= b4.add(b5);
        System.out.println(add);
        //减
        BigDecimal subt=b4.subtract(b5);
        System.out.println(subt);

        //乘
        System.out.println(b4.multiply(b5));
        //除
        System.out.println(b5.divide(b4,2,BigDecimal.ROUND_HALF_DOWN));
    }
}
