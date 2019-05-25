package com.howietian.clubtalk.entrance.forgetpwd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.entrance.login.LoginActivity;

public class ForgetPwdActivity extends BaseActivity<ForgetPwdViewModel> implements View.OnClickListener, OnSubmitListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private EditText mPhoneEdit;
    private ImageView mDeleteImage;
    private EditText mPwdEdit;
    private ImageView mVisibleImage;
    private Button mSubmitBtn;
    private EditText mCodeEdit;
    private TextView mGetCodeText;

    private boolean mIsShow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        observe();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_forget_pwd);
        setSupportActionBar(mToolbar);

        mPhoneEdit = findViewById(R.id.edit_phone);
        mDeleteImage = findViewById(R.id.image_delete);
        mPwdEdit = findViewById(R.id.edit_pwd);
        mVisibleImage = findViewById(R.id.image_visible);
        mSubmitBtn = findViewById(R.id.btn_submit);
        mCodeEdit = findViewById(R.id.edit_sms_code);
        mGetCodeText = findViewById(R.id.text_get_code);

        mDeleteImage.setOnClickListener(this);
        mVisibleImage.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mGetCodeText.setOnClickListener(this);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                if (len == 0) {
                    mDeleteImage.setVisibility(View.INVISIBLE);
                } else {
                    mDeleteImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                if (len == 0) {
                    mVisibleImage.setVisibility(View.INVISIBLE);
                    mPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mVisibleImage.setSelected(false);
                } else {
                    mVisibleImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void observe() {
        getViewModel().getNotifyGetCodeBg().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mGetCodeText.setEnabled(aBoolean);
            }
        });

        getViewModel().getNotifyGetCodeText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mGetCodeText.setText(s);
            }
        });
    }

    @Override
    public void OnSubmitSucceed(String phone) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.PHONE, phone);

        LoginActivity.launch(this, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == mDeleteImage) {
            mPhoneEdit.setText("");
        } else if (view == mVisibleImage) {
            showPwd();
        } else if (view == mSubmitBtn) {
            String phone = mPhoneEdit.getText().toString();
            String smsCode = mCodeEdit.getText().toString();
            String pwd = mPwdEdit.getText().toString();
            getViewModel().updatePwd(phone, smsCode, pwd, this);
        } else if (view == mGetCodeText) {
            String phone = mPhoneEdit.getText().toString();
            getViewModel().getSmsCode(phone);
        }
    }

    private void showPwd() {
        if (mIsShow) {
            mPwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mVisibleImage.setSelected(true);
        } else {
            mPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mVisibleImage.setSelected(false);
        }
        mPwdEdit.setSelection(mPwdEdit.getText().length());

        mIsShow = !mIsShow;
    }
}
