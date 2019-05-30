package com.howietian.clubtalk.base;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContent();
        setContentView(getLayoutId());
        mViewModel = bindViewModel();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(android.R.style.Theme_Material_Dialog);
        mProgressDialog.setMessage("正在登录中...");
        mProgressDialog.setCanceledOnTouchOutside(false);

        observe();
    }

    private void observe() {
        if (mViewModel != null && mViewModel instanceof BaseViewModel) {
            BaseViewModel baseViewModel = (BaseViewModel) mViewModel;
            baseViewModel.getNotifyToast().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(BaseActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });

            baseViewModel.getNotifyProgress().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        mProgressDialog.show();
                    } else {
                        mProgressDialog.dismiss();
                    }
                }
            });

            baseViewModel.getNotifyProgressText().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (mProgressDialog != null) {
                        mProgressDialog.setMessage(s);
                    }
                }
            });

            baseViewModel.getNotifyFinish().observe(this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    finish();
                }
            });
        }

    }

    protected VM getViewModel() {
        return mViewModel;
    }

    protected VM bindViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type != null && type instanceof ParameterizedType) {
            return ViewModelProviders.of(this)
                    .get((Class<VM>) ((ParameterizedType) type).getActualTypeArguments()[0]);
        }
        return null;
    }

    protected abstract int getLayoutId();

    protected void beforeSetContent() {
    }

    public void loadImage(Uri uri, ImageView imageView) {
        Glide.with(this).load(uri).into(imageView);
    }

    public void loadImage(String s, ImageView imageView) {
        Glide.with(this).load(s).into(imageView);
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
