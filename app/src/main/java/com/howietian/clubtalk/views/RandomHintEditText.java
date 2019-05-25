package com.howietian.clubtalk.views;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.widget.EditText;

public class RandomHintEditText extends EditText {

    private int contextId;

    private boolean mRandomAble = true;

    public RandomHintEditText(Context context) {
        super(context);
        init(context);
    }

    public RandomHintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RandomHintEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        contextId = context.hashCode();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRandomAble) {
            String hint = RandomHintManager.get().getHint(contextId);
            setHint(hint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRandomAble) {
            RandomHintManager.get().removeHint(contextId);
        }
    }

    public boolean isRandomAble() {
        return mRandomAble;
    }

    public void setRandomAble(boolean mRandomAble) {
        this.mRandomAble = mRandomAble;
    }
}
