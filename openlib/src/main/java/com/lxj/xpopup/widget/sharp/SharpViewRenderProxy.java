package com.lxj.xpopup.widget.sharp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.lxj.xpopup.enums.PopupPosition;
import com.openlib.R;

import androidx.core.content.res.ResourcesCompat;


public class SharpViewRenderProxy {

    private View mView;

    public float getRadius() {
        return mRadius;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public float getRelativePosition() {
        return mRelativePosition;
    }

    public float getSharpSize() {
        return mSharpSize;
    }

    public float getBorder() {
        return mBorder;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public int[] getBgColors() {
        return mBgColors;
    }

    public float[] getCornerRadii() {
        return mCornerRadii;
    }

    public PopupPosition getArrowDirection() {
        return mArrowDirection;
    }

    private float mRadius;

    private int mBackgroundColor;

    private float mRelativePosition;

    private float mSharpSize;

    private float mBorder;

    private int mBorderColor;

    public void setBgColor(int[] bgColor) {
        mBgColors = bgColor;
        refreshView();
    }

    private int[] mBgColors;

    public void setCornerRadii(float leftTop,float rightTop,float rightBottom,float leftBottom) {
        mCornerRadii[0] = leftTop;
        mCornerRadii[1] = leftTop;
        mCornerRadii[2] = rightTop;
        mCornerRadii[3] = rightTop;
        mCornerRadii[4] = rightBottom;
        mCornerRadii[5] = rightBottom;
        mCornerRadii[6] = leftBottom;
        mCornerRadii[7] = leftBottom;

    }

    private float[] mCornerRadii = new float[8];

    private PopupPosition mArrowDirection = PopupPosition.Left;

    public void setBorder(float border) {
        mBorder = border;
        refreshView();
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        refreshView();
    }

    public SharpViewRenderProxy(View view, Context context, AttributeSet attrs, int defStyleAttr) {
        mView = view;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SharpFrameLayout, defStyleAttr, 0);
        mRadius = a.getDimension(R.styleable.SharpFrameLayout_radius, 5);
        mCornerRadii[0] = mCornerRadii[1] = a.getDimension(R.styleable.SharpFrameLayout_left_top_radius, 0);
        mCornerRadii[2] = mCornerRadii[3] = a.getDimension(R.styleable.SharpFrameLayout_right_top_radius, 0);
        mCornerRadii[4] = mCornerRadii[5] = a.getDimension(R.styleable.SharpFrameLayout_right_bottom_radius, 0);
        mCornerRadii[6] = mCornerRadii[7] = a.getDimension(R.styleable.SharpFrameLayout_left_bottom_radius, 0);
        mBorder = a.getDimension(R.styleable.SharpFrameLayout_border, 0);
        mBackgroundColor = a.getColor(R.styleable.SharpFrameLayout_backgroundColor, ResourcesCompat.getColor(context.getResources(), R.color._xpopup_dark_color, null));
        mBorderColor = a.getColor(R.styleable.SharpFrameLayout_borderColor, 0);
        int direction = a.getInt(R.styleable.SharpFrameLayout_arrowDirection, 2);
        mRelativePosition = a.getFraction(R.styleable.SharpFrameLayout_relativePosition, 1, 1, 0.5f);
        mSharpSize = a.getDimension(R.styleable.SharpFrameLayout_sharpSize,10);
        switch (direction) {
            case 1:
                mArrowDirection = PopupPosition.Left;
                break;
            case 2:
                mArrowDirection = PopupPosition.Top;
                break;
            case 3:
                mArrowDirection =  PopupPosition.Right;
                break;
            case 4:
                mArrowDirection = PopupPosition.Bottom;
                break;
        }
        int start = a.getColor(R.styleable.SharpFrameLayout_startBgColor, -1);
        int middle  = a.getColor(R.styleable.SharpFrameLayout_middleBgColor, -1);
        int end  = a.getColor(R.styleable.SharpFrameLayout_endBgColor, -1);
        if (start != -1  && end != -1) {
            if (middle != -1) {
                mBgColors = new int[]{start, middle, end};
            } else {
                mBgColors = new int[]{start, end};
            }
        }
        a.recycle();
        refreshView();
    }

    SharpDrawable mSharpDrawable;

    private void refreshView() {
        SharpDrawable bd = new SharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        mSharpDrawable = bd;
        if (mBgColors != null) {
            bd.setColors(mBgColors);
        } else {
            bd.setBgColor(mBackgroundColor);
        }
        bd.setSharpSize(mSharpSize);
        bd.setArrowDirection(mArrowDirection);
        bd.setCornerRadius(mRadius);
        bd.setBorder(mBorder);
        bd.setBorderColor(mBorderColor);
        bd.setRelativePosition(mRelativePosition);
        if (mRadius == 0) {
            bd.setCornerRadii(mCornerRadii);
        }
//        if (mView instanceof SharpImageView) {
//            mView.invalidate();
//        } else {
            mView.setBackground(bd);
//        }
    }

    public void setRadius(float radius) {
        mRadius = radius;
        refreshView();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mBgColors = null;
        refreshView();
    }

    public void setRelativePosition(float relativePosition) {
        mRelativePosition = relativePosition;
        refreshView();
    }

    public void setSharpSize(float sharpSize) {
        mSharpSize = sharpSize;
        refreshView();
    }

    public void setArrowDirection(PopupPosition arrowDirection) {
        mArrowDirection = arrowDirection;
        refreshView();
    }

}
