package com.howietian.clubtalk.base;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {
    private ProgressDialog mProgressDialog;
    private VM mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeSetContent();
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = bindViewModel();

        mProgressDialog = new ProgressDialog(getActivity());
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
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
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
                    getActivity().finish();
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
            return ViewModelProviders.of(this).get((Class<VM>) ((ParameterizedType) type).getActualTypeArguments()[0]);
        }
        return null;
    }

    public abstract int getLayoutId();

    public void beforeSetContent() {
    }

    public void loadImage(Uri uri, ImageView imageView) {
        Glide.with(getActivity()).load(uri).into(imageView);
    }

    public void loadImage(String s, ImageView imageView) {
        Glide.with(getActivity()).load(s).into(imageView);
    }

    public void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
