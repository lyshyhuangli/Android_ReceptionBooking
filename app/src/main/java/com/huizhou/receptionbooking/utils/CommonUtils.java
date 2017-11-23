package com.huizhou.receptionbooking.utils;


/**
 * Created by Administrator on 2017/9/1.
 */

public class CommonUtils
{


    public static String getFileLineMethod(Exception e)
    {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return "";

        StringBuffer toStringBuffer = new StringBuffer("[").append(
                trace[0].getFileName()).append(" -- ").append(
                trace[0].getMethodName()).append(" -- ").append(
                trace[0].getLineNumber()).append("]");
        return toStringBuffer.toString();
    }
}
