package com.howietian.clubtalk.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.howietian.clubtalk.R;


/**
 * Created by tianhaoyu on 18/01/2019.
 * <p>
 * 评论对话框
 */

public class CommentDialog extends Dialog implements View.OnClickListener, TextWatcher, ModifyEditText.KeyboardCloseListener {

    private TextView mCancelTextView;

    private TextView mSubmitTextView;

    private ModifyEditText mInputEditText;

    private String mText;

    private OnSubmitCommentListener mOnSubmitCommentListener;

    public CommentDialog(@NonNull Context context) {
        this(context, R.style.MotifyDialogStyle);
    }

    public CommentDialog(@NonNull Context context, boolean randomAble) {
        this(context, R.style.MotifyDialogStyle, randomAble);
    }

    public CommentDialog(@NonNull Context context, int themeResId) {
        this(context, themeResId, false);
    }

    public CommentDialog(@NonNull Context context, int themeResId, boolean randomAble) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_comment);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        findView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mInputEditText.addTextChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInputEditText.removeTextChangedListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void findView() {

        mCancelTextView = findViewById(R.id.text_cancel);
        mSubmitTextView = findViewById(R.id.text_submit);
        mInputEditText = findViewById(R.id.edit_content);
        mInputEditText.setRandomAble(false);

        mInputEditText.setKeyboardCloseListener(this);
        mCancelTextView.setOnClickListener(this);
        mSubmitTextView.setOnClickListener(this);

        initInputEditText();

    }

    private void initInputEditText() {
        if (mInputEditText == null) {
            return;
        }
        if (!TextUtils.isEmpty(mText)) {
            mInputEditText.setText(mText);
            mInputEditText.setSelection(mText.length());
            mSubmitTextView.setEnabled(true);
        } else {
            mSubmitTextView.setEnabled(false);
        }
    }

    @Override
    public void show() {
        super.show();
        initInputEditText();
    }


    @Override
    public void onClick(View v) {
        if (mCancelTextView == v) {
            dismiss();
        } else if (mSubmitTextView == v) {
            String text = mInputEditText.getText().toString();
            if (submit(text)) {
                dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mSubmitTextView.setEnabled(s.length() > 0);
    }


    private boolean submit(String text) {

        if (mOnSubmitCommentListener != null) {
            mOnSubmitCommentListener.onSubmitComment(text);
            return true;
        }

        return false;
    }

    public void setHint(String text) {
        if (mInputEditText != null && !TextUtils.isEmpty(text)) {
            mInputEditText.setHint(text);
        }
    }

    @Override
    public void onClose() {
        dismiss();
    }

    public void setText(String text) {
        this.mText = text;
        if (mInputEditText != null) {
            mInputEditText.setText(text);
        }
    }


    public interface OnSubmitCommentListener {
        void onSubmitComment(String text);
    }

    public void setOnSubmitCommentListener(OnSubmitCommentListener listener) {
        this.mOnSubmitCommentListener = listener;
    }
}

