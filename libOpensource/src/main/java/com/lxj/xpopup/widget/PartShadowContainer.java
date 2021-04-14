package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnClickOutsideListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.sharp.SharpView;
import com.lxj.xpopup.widget.sharp.SharpViewRenderProxy;
import com.openlib.R;

/**
 * Description:
 * Create by dance, at 2019/1/10
 */
public class PartShadowContainer extends FrameLayout {
    SharpViewRenderProxy mSharpViewRenderProxy;
    public boolean isDismissOnTouchOutside = true;

    public PartShadowContainer(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public PartShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs, 0);
    }

    public PartShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mSharpViewRenderProxy = new SharpViewRenderProxy(this, context, attrs, defStyleAttr);
        mSharpViewRenderProxy.setSharpSize(16f);
    }

    public void setSharpRelativePosition(float relativePosition){
        mSharpViewRenderProxy.setRelativePosition(relativePosition);
    }

    public void setSharpBackgroundColor(int colorRes){
        mSharpViewRenderProxy.setBackgroundColor(ResourcesCompat.getColor(getResources(), colorRes, null));
    }

    public void setSharpArrowDirection(PopupPosition arrowDirection){
        switch (arrowDirection){
            case Bottom: setPadding(0, 0, 0, 16);
            case Right: setPadding(0, 0, 16, 0);
            case Left: setPadding(16, 0, 0, 0);
            case Top: setPadding(0, 16, 0, 0);
        }
        mSharpViewRenderProxy.setArrowDirection(arrowDirection);
    }

    public void setRadius(float radius){
        mSharpViewRenderProxy.setRadius(radius);
    }

    private float x, y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算implView的Rect
        View implView = getChildAt(0);
        int[] location = new int[2];
        implView.getLocationInWindow(location);
        Rect implViewRect = new Rect(location[0], location[1], location[0] + implView.getMeasuredWidth(),
                location[1] + implView.getMeasuredHeight());
        if (!XPopupUtils.isInRect(event.getRawX(), event.getRawY(), implViewRect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        if (isDismissOnTouchOutside) {
                            if (listener != null) listener.onClickOutside();
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        return true;
    }

    private OnClickOutsideListener listener;

    public void setOnClickOutsideListener(OnClickOutsideListener listener) {
        this.listener = listener;
    }
}
