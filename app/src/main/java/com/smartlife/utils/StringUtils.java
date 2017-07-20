package com.smartlife.utils;

import android.annotation.SuppressLint;
import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
    };

    private final static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public StringUtils() {

    }

    public static String formatLongToTimeStr(int l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        String temp = "";
        if (l > 60) {
            minute = l / 60;
            second = l % 60;
        } else {
            second = l;

        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour != 0)
            temp = getTwoLength(hour) + ":";
        if (minute != 0)
            temp += getTwoLength(minute) + ":";
        if (second >= 0)
            temp += getTwoLength(second);
        return temp;
    }

    public static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    public static String PassWordMD5(String string) {
        string = string + "Hong2019FansCYB";
        // 信息摘要器
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(string.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                // 每个byte做与运算
                int number = b & 0xff;// 不按标准加密，密码学：加盐
                // 转换成16进制
                String numberStr = Integer.toHexString(number);
                if (numberStr.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(numberStr);
            }
            // 就标准的md5加密的结果
            return buffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String MD5(String string) {

        string = string + "Hong2019FansCYB";
        // 信息摘要器
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(string.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                // 每个byte做与运算
                int number = b & 0xff;// 不按标准加密，密码学：加盐
                // 转换成16进制
                String numberStr = Integer.toHexString(number);
                if (numberStr.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(numberStr);
            }
            // 就标准的md5加密的结果
            return buffer.toString();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 对byte类型的数组进行MD5加密
     */
    public static String getMD5String(byte[] bytes) {
        try {
            // 信息摘要器
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return bufferToHex(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuilder sb = new StringBuilder(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            sb.append(c0);
            sb.append(c1);
        }
        return sb.toString();
    }

    // private final static SimpleDateFormat dateFormater = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new
    // SimpleDateFormat("yyyy-MM-dd");

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean numberMatch(String string, String s) {
        if (null == string)
            return false;
        String dealStr = string.replace("-", "");
        dealStr = dealStr.replace(" ", "");
        return dealStr.contains(s);
    }

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isinputEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空 Determines if a string is not empty.
     */
    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }


    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if (isNotEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                /* 获取一个字符 */
                String temp = str.substring(i, i + 1);
                /* 判断是否为中文字符 */
                if (temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }
        return valueLength;
    }

    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (isNotEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                    // 中文字符长度为2
                    valueLength += 2;
                } else {
                    // 其他字符长度为1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 描述：获取指定长度的字符所在位置.
     *
     * @param str  指定的字符串
     * @param maxL 要取到的长度（字符长度，中文字符计2个）
     * @return 字符的所在位置
     */
    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < str.length(); i++) {
            // 获取一个字符
            String temp = str.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为2
                valueLength += 2;
            } else {
                // 其他字符长度为1
                valueLength += 1;
            }
            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    /**
     * @param str
     * @return
     * @描述：座机正则匹配
     * @作者：傅健
     * @时间：2015-7-4 下午4:50:28
     */
    public static boolean isLandLineNo(String str) {
        Boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^(0[0-9]{2,3})?([2-9][0-9]{6,7})+([0-9]{1,4})?$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * 描述：手机号格式验证.
     *
     * @param str 指定的手机号码字符串
     * @return 是否为手机号码格式:是为true，否则false
     */
    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0,5-9])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 描述：是否只是数字.
     *
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * 描述：是否是中文.
     *
     * @param str 指定的字符串
     * @return 是否是中文:是为true，否则false
     */
    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (isNotEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                } else {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (isNotEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                    isChinese = true;
                } else {

                }
            }
        }
        return isChinese;
    }

    /**
     * 描述：从输入流中获得String.
     *
     * @param is 输入流
     * @return 获得的String
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // 最后一个\n删除
            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 描述：标准化日期时间类型的数据，不足两位的补0.
     *
     * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
     * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
     */
    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();
        try {
            if (isEmpty(dateTime)) {
                return null;
            }
            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                for (String str : dateAndTime) {
                    if (str.indexOf("-") != -1) {
                        String[] date = str.split("-");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ");
                        String[] date = str.split(":");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    the str
     * @param length 指定字节长度
     * @return 截取后的字符串
     */
    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    文本
     * @param length 字节长度
     * @param dot    省略符号
     * @return 截取后的字符串
     */
    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if (strBLen <= length) {
            return str;
        }
        int temp = 0;
        StringBuffer sb = new StringBuffer(length);
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append(c);
            if (c > 256) {
                temp += 2;
            } else {
                temp += 1;
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot);
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 描述：截取字符串从第一个指定字符.
     *
     * @param str1   原文本
     * @param str2   指定字符
     * @param offset 偏移的索引
     * @return 截取后的字符串
     */
    public static String cutStringFromChar(String str1, String str2, int offset) {
        if (isEmpty(str1)) {
            return "";
        }
        int start = str1.indexOf(str2);
        if (start != -1) {
            if (str1.length() > start + offset) {
                return str1.substring(start + offset);
            }
        }
        return "";
    }

    /**
     * 描述：获取字节长度.
     *
     * @param str     文本
     * @param charset 字符集（GBK）
     * @return the int
     */
    public static int strlen(String str, String charset) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int length = 0;
        try {
            length = str.getBytes(charset).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取大小的描述.
     *
     * @param size 字节个数
     * @return 大小的描述
     */
    public static String getSizeDesc(long size) {
        String suffix = "B";
        if (size >= 1024) {
            suffix = "K";
            size = size >> 10;
            if (size >= 1024) {
                suffix = "M";
                // size /= 1024;
                size = size >> 10;
                if (size >= 1024) {
                    suffix = "G";
                    size = size >> 10;
                    // size /= 1024;
                }
            }
        }
        return size + suffix;
    }

    /**
     * 描述：ip地址转换为10进制数.
     *
     * @param ip the ip
     * @return the long
     */
    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println(dateTimeFormat("2012-3-2 12:2:20"));
    }

    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten, Locale.getDefault());
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return "";
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten, Locale.getDefault());
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static String ReplaceUrl(String url) {
        String temp = url;
        if (temp.lastIndexOf("@") > 0)
            return temp.substring(0, temp.lastIndexOf("@"));
        else
            return url;
    }

    /**
     * @return
     * @描述：传进来一个数字，转成00:00的格式
     * @作者：傅健
     * @时间：2015-11-18 下午1:54:45
     */
    public static String TimeFormat(int second) {
        String str = "";
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second >= 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp >= 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    } else {
                        s = 0;

                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            } else {
                s = 0;

            }
        }
        if (h != 00 && d != 0 && s != 0)
            str = (h >= 10 ? s : "0" + String.valueOf(h)) + ":" + (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h != 00 && d == 0 && s != 0)
            str = (h >= 10 ? s : "0" + String.valueOf(h)) + ":" + (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h != 00 && d != 0 && s == 0)
            str = (h >= 10 ? s : "0" + String.valueOf(h)) + ":" + (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h != 00 && d == 0 && s == 0)
            str = (h >= 10 ? s : "0" + String.valueOf(h)) + ":" + (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h != 00 && d == 0 && s != 0)
            str = (h >= 10 ? s : "0" + String.valueOf(h)) + ":" + (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h == 00 && d != 0 && s != 0)
            str = (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h == 00 && d != 0 && s == 0)
            str = (d >= 10 ? s : "0" + String.valueOf(d)) + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h == 00 && d == 0 && s != 0)
            str = "00" + ":" + (s >= 10 ? s : "0" + String.valueOf(s));
        else if (h == 00 && d == 0 && s == 0)
            str = "00" + ":" + (s >= 10 ? s : "0" + String.valueOf(s));


        return str;
    }

    /**
     * 转换歌曲时长
     */
    public static String getDuration(int duration) {
        String sDuration = "";
        try {
            SimpleDateFormat dateFormater = new SimpleDateFormat("mm:ss");
            Date date = new Date();
            sDuration = dateFormater.format(duration);
        } catch (Exception e) {
            e.toString();
        }
        return sDuration;
    }

    public static String formatTime(String time) {
        String str;
        int number = 0;
        try {
            number = Integer.valueOf(time);
        } catch (NumberFormatException e) {
            time = "0";
            // e.printStackTrace();
        }
        if (number < 60) {
            if (number < 10) {
                str = "00:0" + time;
            } else {
                str = "00:" + time;
            }
        } else {
            if (number / 60 < 10) {
                if (number % 60 < 10) {
                    str = "0" + number / 60 + ":" + "0" + number % 60;
                } else {
                    str = "0" + number / 60 + ":" + number % 60;
                }
            } else {
                if (number % 60 < 10) {
                    str = number / 60 + ":" + "0" + number % 60;
                } else {
                    str = number / 60 + ":" + number % 60;
                }
            }
        }

        return str;
    }

    @SuppressLint("SimpleDateFormat")
    public static String str2date(String time) {
        Date d = new Date(Long.valueOf(time) * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d) + " |更新";
    }

    public static String DoubleFormat(double num) {
        NumberFormat formatter = new DecimalFormat("0.#");
        String str = formatter.format(num);
        return str;
    }


    public static String GetResToString(Context context, int ResID) {
        return context.getApplicationContext().getResources().getString(ResID);
    }

    /**
     * @param cnStr
     * @return
     * @描述：获取汉字的ASCII码
     * @作者：傅健
     * @时间：2015-11-4 下午8:31:08
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    /**
     * 拼接两个数组
     *
     * @param A 第一个数组
     * @param B 第二个数组
     * @return 拼接的数组
     */
    public static byte[] concat(byte[] A, byte[] B) {
        byte[] C = new byte[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    /**
     * 去除特殊字符
     *
     * @param title
     * @return
     */
    public static String ReplaceTitle(String title) {
        String temp = title.replaceAll("\\s", "").replace("，&nbsp;", " ").replace(",&nbsp;", " ").replace("&nbsp;", " ");
        if (temp != null) {
            if (temp.length() == 0) {
                return "未知";
            } else
                return temp;
        } else
            return "未知";
    }

    /**
     * 过滤不需要的字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {

        String regEx = "[/\\:*?<>|\"\n\t]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

    }

    public static String TimeFormat1(String time) {
        String str = "";
        int number = Integer.valueOf(time);
        if (number < 60) {
            if (number < 10) {
                str = "00:0" + time;
            } else {
                str = "00:" + time;
            }
        } else {
            if (number / 60 < 10) {
                if (number % 60 < 10) {
                    str = "0" + number / 60 + ":" + "0" + number % 60;
                } else {
                    str = "0" + number / 60 + ":" + number % 60;
                }
            } else {
                if (number % 60 < 10) {
                    str = number / 60 + ":" + "0" + number % 60;
                } else {
                    str = number / 60 + ":" + number % 60;
                }
            }
        }

        return str;
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否只是数字
     * @param length     长度
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr;
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}