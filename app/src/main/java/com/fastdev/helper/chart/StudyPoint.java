package com.fastdev.helper.chart;

import android.text.TextUtils;

import com.fastdev.helper.RegexHelper;

/**
 * @author: ton
 * @date: 2022/4/24
 */
public class StudyPoint {
    public int keep_decimal = -1;//显示时保留小数位数,-1表示根据实际数据展示

    public String day; //日期yyyy-MM-dd
    public float myNum; //我的 数据
    public float highNum; //200分  数据
    public float baseNum; //180分[基准] 数据

    public String mineValue(){
        return RegexHelper.formatNumberValue(myNum, keep_decimal);
    }

    public String highValue(){
        return RegexHelper.formatNumberValue(highNum, keep_decimal);
    }

    public String baseValue(){
        return RegexHelper.formatNumberValue(baseNum, keep_decimal);
    }

    public StudyPoint(String xLeable, float mineValue, float baseValue1, float baseValue2){
        this.day = xLeable;
        this.myNum = mineValue;
        this.baseNum = baseValue1;
        this.highNum = baseValue2;
    }

    public String toDay(){
        if(TextUtils.isEmpty(day)) return "";
        return day;
//        return TimeUtils.date2String(TimeUtils.string2Date(day, "yyyy-MM-dd"), "MM-dd");
    }

//    private int mode = MODE_MINIE;
//    public void setModeData(int mode){
//        this.mode = mode;
//    }
}
