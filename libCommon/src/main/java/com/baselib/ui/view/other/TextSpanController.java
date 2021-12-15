package com.baselib.ui.view.other;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.baselib.app.ApplicationDelegate;
import com.baselib.callback.Action;

import java.util.ArrayDeque;
import java.util.Deque;

public class TextSpanController {
    private final SpannableStringBuilder builder;
    private final Deque<Span> stack;

    public TextSpanController() {
        builder = new SpannableStringBuilder();
        stack = new ArrayDeque<>();
    }

    public TextSpanController append(String string) {
        if(string == null) string = "";
        builder.append(string);
        return this;
    }

    public TextSpanController append(CharSequence charSequence) {
        builder.append(charSequence);
        return this;
    }

    public TextSpanController append(char c) {
        builder.append(c);
        return this;
    }

    public TextSpanController append(int number) {
        builder.append(String.valueOf(number));
        return this;
    }

    /** Starts {@code span} at the current position in the builder. */
    public TextSpanController pushSpan(Object span) {
        stack.addLast(new Span(builder.length(), span));
        return this;
    }

    public TextSpanController pushClickSpan(final int color, final Action click) {
        return pushSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if(widget instanceof TextView){
                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                }
                if(click != null) click.invoke();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        });
    }

    public TextSpanController pushSizeSpan(int dp){
        return pushSpan(new AbsoluteSizeSpan(dp, true));
    }

    public TextSpanController pushColorSpan(@ColorInt int color){
        return pushSpan(new ForegroundColorSpan(color));
    }

    public TextSpanController pushTypefaceSpan(final String typefaceAssetPath){
        return pushSpan(new TypefaceSpan(""){
            Typeface typeface = Typeface.createFromAsset(ApplicationDelegate.context.getAssets(), typefaceAssetPath);
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                applyCustomTypeFace(ds);
            }

            @Override
            public void updateMeasureState(@NonNull TextPaint paint) {
                applyCustomTypeFace(paint);
            }

            private void applyCustomTypeFace(Paint paint){
                int oldStyle;
                Typeface old = paint.getTypeface();
                if (old == null) {
                    oldStyle = 0;
                } else {
                    oldStyle = old.getStyle();
                }

                int fake = oldStyle & ~typeface.getStyle();
                if ((fake & Typeface.BOLD) != 0) {
                    paint.setFakeBoldText(true);
                }

                if ((fake & Typeface.ITALIC) != 0) {
                    paint.setTextSkewX(-0.25f);
                }

                paint.setTypeface(typeface);
            }

        });
    }

    /** End the most recently pushed span at the current position in the builder. */
    public TextSpanController popSpan() {
        Span span = stack.removeLast();
        builder.setSpan(span.span, span.start, builder.length(), android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /** Create the final {@link CharSequence}, popping any remaining spans. */
    public CharSequence build() {
        while (!stack.isEmpty()) {
            popSpan();
        }
        return builder; // TODO make immutable copy?
    }

    private static final class Span {
        final int start;
        final Object span;

        public Span(int start, Object span) {
            this.start = start;
            this.span = span;
        }
    }
}
