package com.huizhou.receptionbooking.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 获得指定日期的后一天
     *
     * @param i
     * @return
     */
    public static String getSpecifiedDayAfter(int i)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + i);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 根据就餐情况返回数字
     *
     * @param str
     * @return
     */
    public static int getMenuTypeNumber(String str)
    {
        if ("早餐".equals(str))
        {
            return 3;
        }
        else if ("中餐".equals(str))
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }

    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public  static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }

}
