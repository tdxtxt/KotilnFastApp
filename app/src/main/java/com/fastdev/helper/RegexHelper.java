package com.fastdev.helper;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    private static boolean matches(String regex, String str){
        if(str == null) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isInt(String str){
        return matches("^[-]?\\d+$", str);
    }

    public static boolean isDouble(String str){
        return matches("^-?\\d+(\\.\\d+)?$", str);
    }

    public static int str2Int(String str){
        return (int) str2Double(str);
    }

    public static long str2Long(String str){
        return (long) str2Double(str);
    }

    public static double str2Double(String str){
        if(str == null) return 0.0;
        if(!isDouble(str)) return 0.0;
        return Double.parseDouble(str);
    }

    /**
     * 在价格前面添加￥符号
     */
    public static String addMoneySymbol(String content) {
        if(TextUtils.isEmpty(content)) return "";
        Pattern pattern = Pattern.compile("[￥|¥]?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String oldReplace = matcher.group(0);
            String newReplace = "￥" + oldReplace.replace("￥", "").replace("¥", "");
            /*
             * 我们在这里使用Matcher对象的appendReplacement()方法来进行替换操作，而
             * 不是使用String对象的replaceAll()或replaceFirst()方法来进行替换操作，因为
             * 它们都能只能进行一次性简单的替换操作，而且只能替换成一样的内容，而这里则是要求每
             * 一个匹配式的替换值都不同，所以就只能在循环里使用appendReplacement方式来进行逐个替换了。
             */
            matcher.appendReplacement(sb, newReplace);
        }
        //最后还得要把尾串接到已替换的内容后面去
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String formatNumberValue(String value){
        //value是整数的去掉小数点，value为小数保留一位小数
        if(TextUtils.isEmpty(value)) return "0";
        return new BigDecimal(String.valueOf(value)).stripTrailingZeros().toPlainString();
    }

    public static String formatNumberValue(Float value){
        return formatNumberValue(value, 2, false);
    }

    public static String formatNumberValue(Float value, int decimalNum){
        return formatNumberValue(value, decimalNum, true);
    }

    public static String formatNumberValue(Float value, int decimalNum, boolean isKeepZeros){
        if(value == null) value = 0f;
        if(decimalNum < 0){
            return formatNumberValue(value);
        }else if(isKeepZeros){
            return String.format("%." + decimalNum + "f", value);
        }else if(value == 0){
            return "0";
        }else{
            BigDecimal bigNum = new BigDecimal(String.format("%." + decimalNum + "f", value));
//            if (bigNum.compareTo(BigDecimal.ZERO) == 0) {
//                bigNum = BigDecimal.ZERO;
//            }
            return bigNum.stripTrailingZeros().toPlainString();
        }
    }

}
