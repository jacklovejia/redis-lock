package com.jack.market.absFundsService;

import java.lang.reflect.Method;

public class FundsExector {


    /**
     * 就用反射试一下
     */
    public static void main(String[] args) throws ClassNotFoundException {
        String name = "com.jack.market.fundSource.MS";
        Class<?> aClass = Class.forName(name);
        Method[] methods = aClass.getMethods();

    }

}
