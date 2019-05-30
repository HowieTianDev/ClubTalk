package com.howietian.clubtalk.publish;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class PublishDynamicActivity extends BaseActivity<PublishDynamicViewModel> implements View.OnClickListener {

    private static final int REQUEST_TAKE_PHOTO_PERMISSION = 0;
    private static final int REQUEST_CODE_CHOOSE_IMAGE = 1;
    private static final int REQUEST_CODE_CHOOSE_TYPE = 2;
    private Toolbar mToolbar;
    private EditText mContentEdit;
    private LinearLayout mChooseLayout;
    private TextView mTypeText;
    private NineGridView mNineGridView;
    private ImageView mPhotoImage;

    private List<Uri> mImageUriList = null;
    private String mClubName;


    public static void launch(Context context) {
        Intent intent = new Intent(context, PublishDynamicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_dynamic;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        observe();
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_publish_dynamic);
        setSupportActionBar(mToolbar);

        mContentEdit = findViewById(R.id.edit_content);
        mChooseLayout = findViewById(R.id.layout_choose);
        mTypeText = findViewById(R.id.text_type);
        mNineGridView = findViewById(R.id.nine_grid_view);
        mPhotoImage = findViewById(R.id.image_photo);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mChooseLayout.setOnClickListener(this);
        mPhotoImage.setOnClickListener(this);
    }

    private void observe() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_publish_single) {
            publish();
        }
        return true;
    }

    private void publish() {

        String eventName = "";
        Dynamic dynamic = new Dynamic();

        String content = mContentEdit.getText().toString();

        String type = mTypeText.getText().toString();
        if (type.contains("：")) {
            String[] titles = type.split("：");
            if (titles != null && titles.length > 1) {
                eventName = titles[1];
                dynamic.setEventName(eventName);
                type = titles[0];
                content = "# " + eventName + "\n\n" + content;
            }
        }

        if (!TextUtils.isEmpty(mClubName)) {
            dynamic.setClubName(mClubName);
        }

        String[] imagePaths = null;
        if (mImageUriList != null && mImageUriList.size() > 0) {
            imagePaths = new String[mImageUriList.size()];
            for (int i = 0; i < mImageUriList.size(); i++) {
                imagePaths[i] = CommonUtil.getFilePathFromContentUri(this, mImageUriList.get(i));
            }
        }

        dynamic.setContent(content);
        dynamic.setType(type);
        dynamic.setUser(BmobUser.getCurrentUser(User.class));

        getViewModel().publish(dynamic, imagePaths);
    }

    @Override
    public void onClick(View view) {
        if (view == mChooseLayout) {
            ChooseTypeActivity.launchForResult(this, REQUEST_CODE_CHOOSE_TYPE);
        } else if (view == mPhotoImage) {
            requestGallery();
        }
    }

    private void requestGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_TAKE_PHOTO_PERMISSION);
        } else {
            toGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                toGallery();
            } else {
                showToast("CAMERA PERMISSION DENIED");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void toGallery() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_IMAGE:
                    List<Uri> resultList = Matisse.obtainResult(data);
                    mImageUriList = resultList;
                    List<ImageInfo> imageInfoList = new ArrayList<>();
                    if (resultList != null && resultList.size() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setThumbnailUrl(resultList.get(i).toString());
                            imageInfo.setBigImageUrl(resultList.get(i).toString());

                            imageInfoList.add(imageInfo);
                        }

                        mNineGridView.setAdapter(new NineGridViewClickAdapter(this, imageInfoList));
                    }
                    break;
                case REQUEST_CODE_CHOOSE_TYPE:
                    String type = data.getStringExtra(ChooseTypeActivity.EXTRA_TYPE);
                    Bundle bundle = data.getBundleExtra(ChooseFeedEventActivity.EXTRA_BUNDLE);
                    if (bundle != null) {
                        type = bundle.getString(ChooseTypeActivity.EXTRA_TYPE);
                        String eventName = bundle.getString(ChooseFeedEventActivity.EXTRA_EVENT);
                        mClubName = bundle.getString(ChooseFeedEventActivity.EXTRA_CLUB);

                        if (!TextUtils.isEmpty(eventName)) {
                            type = type + "：" + eventName;
                        }
                    }

                    mTypeText.setText(type);
                    break;
            }
        }
    }
}
