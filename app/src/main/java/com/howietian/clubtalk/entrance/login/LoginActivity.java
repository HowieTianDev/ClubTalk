package com.howietian.clubtalk.entrance.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.howietian.clubtalk.MainActivity;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.entrance.forgetpwd.ForgetPwdActivity;
import com.howietian.clubtalk.entrance.register.RegisterActivity;

public class LoginActivity extends BaseActivity<LoginViewModel> implements View.OnClickListener ,OnLoginListener{

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";

    private Toolbar mToolbar;
    private EditText mPhoneEdit;
    private ImageView mDeleteImage;
    private EditText mPwdEdit;
    private ImageView mVisibleImage;
    private Button mLoginBtn;
    private TextView mForgetPwdText;
    private TextView mRegisterText;

    private boolean mIsShow = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        observe();
        readArguments();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(mToolbar);

        mPhoneEdit = findViewById(R.id.edit_phone);
        mDeleteImage = findViewById(R.id.image_delete);
        mPwdEdit = findViewById(R.id.edit_pwd);
        mVisibleImage = findViewById(R.id.image_visible);
        mLoginBtn = findViewById(R.id.btn_login);
        mForgetPwdText = findViewById(R.id.text_forget_pwd);
        mRegisterText = findViewById(R.id.text_register);

        mDeleteImage.setOnClickListener(this);
        mVisibleImage.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mForgetPwdText.setOnClickListener(this);
        mRegisterText.setOnClickListener(this);


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

    private void readArguments() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            String phone = intent.getStringExtra(PHONE);
            String password = intent.getStringExtra(PASSWORD);
            if (!TextUtils.isEmpty(phone)) {
                mPhoneEdit.setText(phone);
            }
            if (!TextUtils.isEmpty(password)) {
                mPwdEdit.setText(password);
            }
        }
    }

    private void observe() {


    }


    @Override
    public void onClick(View view) {
        if (view == mDeleteImage) {
            mPhoneEdit.setText("");
        } else if (view == mVisibleImage) {
            showPwd();
        } else if (view == mLoginBtn) {
            getViewModel().login(mPhoneEdit.getText().toString(), mPwdEdit.getText().toString(),this);
        } else if (view == mForgetPwdText) {
            ForgetPwdActivity.launch(this);
        } else if (view == mRegisterText) {
            RegisterActivity.launch(this);
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

    @Override
    public void onLoginSucceed() {
        MainActivity.launch(this);
    }
}
