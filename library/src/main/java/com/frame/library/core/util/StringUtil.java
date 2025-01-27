package com.frame.library.core.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月25日13:52
 * @Email: 971613168@qq.com
 */
public class StringUtil {
    private static final String PHONE_START = "1";
    private static final int PHONE_LENGTH = 11;

    private static final int LENGTH_ID_CARD = 18;
    /**
     * 温度符号
     */
    public static final String SYMBOL_TEMP = "°";
    public static final String LINE_HORIZONTAL = "--";

    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == PHONE_LENGTH && number.startsWith(PHONE_START);
    }


    public static boolean isIdCard(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == LENGTH_ID_CARD;
    }

    public static String getNotNullValue(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        return number;
    }


    public static boolean isCarNumberNo(String carNumber) {
        if (TextUtils.isEmpty(carNumber)) {
            return false;
        }
        if (carNumber.length() < 7 || carNumber.length() > 8) {
            return false;
        }
        String str = carNumber.charAt(carNumber.length() - 1) + "";
        boolean checkLast = (!str.equalsIgnoreCase("I")) && (!str.equalsIgnoreCase("O")) && isLetter(str) || isNumeric(str);
        return isChineseChar(carNumber.charAt(0)) && isLetter(carNumber.charAt(1) + "") && checkLast;
    }

    public static boolean isChineseChar(char c) {
        try {
            return String.valueOf(c).getBytes("UTF-8").length > 1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是字母
     *
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */

    public static boolean isLetter(String str) {

        if (str == null) return false;

        return str.matches("[a-zA-Z]+");

    }

    public static String getNotNullValueLine(String value) {
        if (TextUtils.isEmpty(value)) {
            return "-";
        }
        return value;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public static String formatDouble(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    public static String getVersionCode(String str) {
        if (StringUtils.isEmpty(str)) {
            return "000000";
        }
        String[] version = str.split("\\.");
        if (version.length == 3) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(appendStr(version[0]));
            stringBuffer.append(appendStr(version[1]));
            stringBuffer.append(appendStr(version[2]));
            return stringBuffer.toString();
        }
        return "000000";
    }

    private static StringBuffer appendStr(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() == 0) {
            stringBuffer.append("00");
        } else if (str.length() == 1) {
            stringBuffer.append("0").append(str);
        } else if (str.length() > 2) {
            stringBuffer.append(str, 0, 2);
        } else {
            stringBuffer.append(str);
        }
        return stringBuffer;
    }


}
