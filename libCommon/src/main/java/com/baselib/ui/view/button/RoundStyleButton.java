package com.baselib.ui.view.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.baselib.R;


public final class RoundStyleButton extends AppCompatTextView {
    int cornerRadius;
    float pressedRatio;
    ColorStateList solidColor;
    int strokeColor;
    int strokeWidth;
    int strokeDashWidth;
    int strokeDashGap;
    boolean enableChangeAlpha;

    public RoundStyleButton(Context context) {
        this(context, null);
    }

    public RoundStyleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundStyleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundStyleButton);

        pressedRatio = a.getFloat(R.styleable.RoundStyleButton_btnPressedRatio, 1.5f);
        cornerRadius = a.getLayoutDimension(R.styleable.RoundStyleButton_btnRadius, 50);
        enableChangeAlpha = a.getBoolean(R.styleable.RoundStyleButton_btnEnableChangeAlpha, true);

        solidColor = a.getColorStateList(R.styleable.RoundStyleButton_btnSolidColor);
        strokeColor = a.getColor(R.styleable.RoundStyleButton_btnStrokeColor, 0x0);
        strokeWidth = a.getDimensionPixelSize(R.styleable.RoundStyleButton_btnStrokeWidth, 0);
        strokeDashWidth = a.getDimensionPixelSize(R.styleable.RoundStyleButton_btnStrokeDashWidth, 0);
        strokeDashGap = a.getDimensionPixelSize(R.styleable.RoundStyleButton_btnStrokeDashGap, 0);

//        btnDrawableLeftCenter = a.getBoolean(R.styleable.RoundStyleButton_btnDrawableLeftCenter, false);
//        btnDrawableRightCenter = a.getBoolean(R.styleable.RoundStyleButton_btnDrawableRightCenter, false);

        a.recycle();

        setSingleLine(true);
        setGravity(Gravity.CENTER);

        setBackground(solidColor);
        setEnabled(isEnabled());
    }

    private void setBackground(ColorStateList solidColor){
        RoundDrawable rd = new RoundDrawable(cornerRadius == -1);
        rd.setCornerRadius(cornerRadius == -1 ? 0 : cornerRadius);
        rd.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);

        if (solidColor == null) {
            solidColor = ColorStateList.valueOf(0);
        }
        if (solidColor.isStateful()) {
            rd.setSolidColors(solidColor);
        } else if (pressedRatio > 0.0001f) {
            rd.setSolidColors(csl(solidColor.getDefaultColor(), pressedRatio));
        } else {
            rd.setColor(solidColor.getDefaultColor());
        }
        setBackground(rd);
    }

    // 灰度
    int greyer(int color) {
        int blue = (color & 0x000000FF) >> 0;
        int green = (color & 0x0000FF00) >> 8;
        int red = (color & 0x00FF0000) >> 16;
        int grey = Math.round(red * 0.299f + green * 0.587f + blue * 0.114f);
        return Color.argb(0xff, grey, grey, grey);
    }

    // 明度
    int darker(int color, float ratio) {
        color = (color >> 24) == 0 ? 0x22808080 : color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= ratio;
        return Color.HSVToColor(color >> 24, hsv);
    }

    ColorStateList csl(int normal, float ratio) {
        //        int disabled = greyer(normal);
        int pressed = darker(normal, ratio);
        int[][] states = new int[][]{{android.R.attr.state_pressed}, {}};
        int[] colors = new int[]{pressed, normal};
        return new ColorStateList(states, colors);
    }

    private static class RoundDrawable extends GradientDrawable {
        private boolean mIsStadium = false;

        private ColorStateList mSolidColors;
        private int mFillColor;

        public RoundDrawable(boolean isStadium) {
            mIsStadium = isStadium;
        }

        public void setSolidColors(ColorStateList colors) {
            mSolidColors = colors;
            setColor(colors.getDefaultColor());
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            if (mIsStadium) {
                RectF rect = new RectF(getBounds());
                setCornerRadius((rect.height() > rect.width() ? rect.width() : rect.height()) / 2);
            }
        }

        @Override
        public void setColor(int argb) {
            mFillColor = argb;
            super.setColor(argb);
        }

        @Override
        protected boolean onStateChange(int[] stateSet) {
            if (mSolidColors != null) {
                final int newColor = mSolidColors.getColorForState(stateSet, 0);
                if (mFillColor != newColor) {
                    setColor(newColor);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isStateful() {
            return super.isStateful() || (mSolidColors != null && mSolidColors.isStateful());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(enableChangeAlpha) setAlpha(enabled ? 1f : 0.5f);
        super.setEnabled(enabled);
    }

    public void setSolidColor(@ColorRes int color){
        solidColor = createColorStateList(getContext(), color);
        setBackground(solidColor);
        setAlpha(1f);
    }

    private ColorStateList createColorStateList(Context context, @ColorRes int color) {
        int[] colors = new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{-android.R.attr.state_selected};
        return new ColorStateList(states, colors);
    }
}