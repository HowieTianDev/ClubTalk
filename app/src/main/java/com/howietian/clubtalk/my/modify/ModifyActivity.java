package com.howietian.clubtalk.my.modify;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyActivity extends BaseActivity<ModifyViewModel> implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ModifyActivity.class);
        context.startActivity(intent);
    }

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CROP = 2;
    private static final int REQUEST_TAKE_PHOTO_PERMISSION = 3;

    private Toolbar mToolbar;
    private CircleImageView mAvatarImage;
    private EditText mNickNameEdit;
    private RadioButton mBoyRadio;
    private RadioButton mGirlRadio;
    private EditText mProfileEdit;
    private EditText mRealNameEdit;
    private EditText mSchoolEdit;
    private EditText mPhoneEdit;
    private LinearLayout mClubIntroLayout;
    private EditText mClubIntroEdit;

    private AlertDialog mChooseImageDialog;
    private Uri mTempImageUri;    // 临时拍照保存路径
    private File mTempImageFile;  // 临时拍照文件
    private Uri mTempCropImageUri;//临时的裁剪后的照片Uri
    private BmobFile mAvatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        observe();

        getViewModel().start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(mToolbar);

        mAvatarImage = findViewById(R.id.image_avatar);
        mNickNameEdit = findViewById(R.id.edit_nickname);
        mRealNameEdit = findViewById(R.id.edit_real_name);
        mProfileEdit = findViewById(R.id.edit_profile);
        mSchoolEdit = findViewById(R.id.edit_school);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mBoyRadio = findViewById(R.id.radio_boy);
        mGirlRadio = findViewById(R.id.radio_girl);

        mClubIntroEdit = findViewById(R.id.edit_club_intro);
        mClubIntroLayout = findViewById(R.id.layout_club_intro);
        if (BmobUser.getCurrentUser(User.class).getClub()) {
            mClubIntroLayout.setVisibility(View.VISIBLE);
        } else {
            mClubIntroLayout.setVisibility(View.GONE);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAvatarImage.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            User user = BmobUser.getCurrentUser(User.class);

            String nickname = mNickNameEdit.getText().toString();
            boolean gender = true;
            if (mBoyRadio.isChecked()) {
                gender = true;
            } else if (mGirlRadio.isChecked()) {
                gender = false;
            }
            String profile = mProfileEdit.getText().toString();
            String school = mSchoolEdit.getText().toString();
            String realName = mRealNameEdit.getText().toString();
            String phone = mPhoneEdit.getText().toString();
            String clubIntro = mClubIntroEdit.getText().toString();

            user.setNickName(nickname);
            user.setGender(gender);
            user.setProfile(profile);
            user.setSchool(school);
            user.setRealName(realName);
            user.setMobilePhoneNumber(phone);
            user.setClubIntro(clubIntro);
            if (mAvatarFile != null) {
                user.setAvatar(mAvatarFile);
            }

            getViewModel().saveInfo(user);

        }
        return true;
    }

    private void observe() {
        getViewModel().getNotifyAvatar().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    loadImage(s, mAvatarImage);
                }
            }
        });

        getViewModel().getNotifyGender().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mBoyRadio.setChecked(true);
                } else {
                    mGirlRadio.setChecked(true);
                }
            }
        });

        getViewModel().getNotifyNickname().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mNickNameEdit.setText(s);
                    mNickNameEdit.setSelection(s.length());
                }
            }
        });

        getViewModel().getNotifyProfile().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mProfileEdit.setText(s);
                    mProfileEdit.setSelection(s.length());
                }
            }
        });

        getViewModel().getNotifyPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mPhoneEdit.setText(s);
                    mPhoneEdit.setSelection(s.length());
                }
            }
        });

        getViewModel().getNotifySchool().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mSchoolEdit.setText(s);
                    mSchoolEdit.setSelection(s.length());
                }
            }
        });

        getViewModel().getNotifyRealName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mRealNameEdit.setText(s);
                    mRealNameEdit.setSelection(s.length());
                }
            }
        });

        getViewModel().getNotifyClubIntro().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    mClubIntroEdit.setText(s);
                    mClubIntroEdit.setSelection(s.length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mAvatarImage) {
            chooseImage();
        }
    }

    /**
     * 选择相机、相册的提示对话框
     */
    private void chooseImage() {

        mChooseImageDialog = new AlertDialog.Builder(this).create();
        mChooseImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        mChooseImageDialog.show();

        Window window = mChooseImageDialog.getWindow();
        window.setContentView(R.layout.dialog_choose_pic);
        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = mChooseImageDialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels;

        mChooseImageDialog.getWindow().setAttributes(lp);

        Button btnCamera = mChooseImageDialog.findViewById(R.id.btn_camera);
        Button btnGallery = mChooseImageDialog.findViewById(R.id.btn_picture);
        Button btnCancel = mChooseImageDialog.findViewById(R.id.btn_cancel);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGallery();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChooseImageDialog.dismiss();
            }
        });
    }

    /**
     * 跳转相机
     */
    private void requestCamera() {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_TAKE_PHOTO_PERMISSION);
        } else {
            //有权限，直接拍照
            toCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                toCamera();
            } else {
                showToast("CAMERA PERMISSION DENIED");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void toCamera() {
        mChooseImageDialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTempImageFile = new File(createImageName(".png"));
        mTempImageUri = Uri.fromFile(mTempImageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    /**
     * 跳转相册
     */
    private void toGallery() {
        mChooseImageDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            从图库中选取图片，会返回图片的Uri
            case REQUEST_GALLERY:
                if (data != null) {
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        cropImage(uri);
                    }
                }

                break;
//            使用相机拍照，不会返回图片的Uri
            case REQUEST_CAMERA:
//                打开相机页面后，如果按返回键也会回调，所以需要判断是否拍摄了照片
                if (resultCode == RESULT_OK && mTempImageUri != null) {
                    File file = new File(mTempImageUri.getPath());
                    if (file.exists()) {
//                       裁剪图片
                        cropImage(mTempImageUri);
                    }
                }
                break;
            case REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    // 将图片上传服务器
                    uploadAvatar();
                }
                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        图片处于可裁剪状态
        intent.putExtra("crop", "true");
//        aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//        是否缩放
        intent.putExtra("scale", true);
//        设置图片的大小，提高头像的上传速度
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
//        以Uri的方式传递照片
        File cropFile = new File(createImageName("crop.png"));
        mTempCropImageUri = Uri.fromFile(cropFile);
//        把裁剪好的图片保存到这个路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempCropImageUri);

        startActivityForResult(intent, REQUEST_CROP);
    }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     */
    private String createImageName(String endStamp) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + dateFormat.format(date) + endStamp;
        return path;
    }

    private void uploadAvatar() {
        File file = new File(mTempCropImageUri.getPath());
        mAvatarFile = new BmobFile(file);

        getViewModel().uploadAvatar(mAvatarFile, mTempCropImageUri.toString());
    }
}
