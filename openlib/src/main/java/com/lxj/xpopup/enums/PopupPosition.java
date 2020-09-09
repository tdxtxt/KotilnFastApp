package com.lxj.xpopup.enums;

/**
 * Description: 弹窗相对于目标View显示的位置
 * Create by dance, at 2019/3/26
 */
public enum PopupPosition {
    Left, Right, Top, Bottom;

    public PopupPosition nonValue(){
        switch (this){
            case Top: return Bottom;
            case Left: return Right;
            case Right: return Left;
            case Bottom: return Top;
            default: return Top;
        }
    }
}
