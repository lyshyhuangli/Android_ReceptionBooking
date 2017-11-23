package com.huizhou.receptionbooking.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class SpUtils {

    private static List<String> cache = new ArrayList<String>();

    public static int getIsFirstOpen()
    {
       return cache.size();
    }

    public static void setIsFirstOpen()
    {
        cache.add("true");
    }




}
