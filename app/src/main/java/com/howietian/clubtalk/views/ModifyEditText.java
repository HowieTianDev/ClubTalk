package com.howietian.clubtalk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;


public class ModifyEditText extends RandomHintEditText {

    private KeyboardCloseListener mListener;

    public ModifyEditText(Context context) {
        super(context);
    }

    public ModifyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModifyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mListener != null) {
                mListener.onClose();
            }
        }
        return false;
    }

    public void setKeyboardCloseListener(KeyboardCloseListener listener) {
        mListener = listener;
    }

    public interface KeyboardCloseListener {

        void onClose();
    }
}