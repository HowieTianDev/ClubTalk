package com.howietian.clubtalk.publish;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;
import com.howietian.clubtalk.utils.TimeUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class PublishEventActivity extends BaseActivity<PublishEventViewModel> implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 0;
    private static final int REQUEST_TAKE_PHOTO_PERMISSION = 1;
    private static final int REQUEST_CROP = 2;

    private Toolbar mToolbar;
    private EditText mTitleEdit;
    private EditText mOrganizerEdit;
    private EditText mPlaceEdit;
    private EditText mStartTimeEdit;
    private EditText mPhoneEdit;
    private TextView mDeadLineText;
    private Button mSelectTimeBtn;
    private EditText mContentEdit;
    private ImageView mCoverImage;

    private BmobFile mCoverFile;
    private Uri mTempCropImageUri;


    public static void launch(Context context) {
        Intent intent = new Intent(context, PublishEventActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        observe();
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_publish_event);
        setSupportActionBar(mToolbar);

        mTitleEdit = findViewById(R.id.edit_title);
        mOrganizerEdit = findViewById(R.id.edit_organizer);
        mPlaceEdit = findViewById(R.id.edit_place);
        mStartTimeEdit = findViewById(R.id.edit_start_time);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mDeadLineText = findViewById(R.id.text_deadline);
        mSelectTimeBtn = findViewById(R.id.btn_select_time);
        mContentEdit = findViewById(R.id.edit_content);
        mCoverImage = findViewById(R.id.image_cover);

        mSelectTimeBtn.setOnClickListener(this);
        mCoverImage.setOnClickListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

        String title = mTitleEdit.getText().toString();
        String organizer = mOrganizerEdit.getText().toString();
        String place = mPlaceEdit.getText().toString();
        String startTime = mStartTimeEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        String deadLine = mDeadLineText.getText().toString();
        String content = mContentEdit.getText().toString();

        Event event = new Event();
        event.setUser(BmobUser.getCurrentUser(User.class));
        event.setTitle(title);
        event.setOrganizer(organizer);
        event.setPlace(place);
        event.setPlace(place);
        event.setStartTime(startTime);
        event.setPhone(phone);
        event.setDeadLine(deadLine);
        event.setContent(content);
        event.setCover(mCoverFile);

        getViewModel().publish(event);
    }

    @Override
    public void onClick(View view) {
        if (view == mSelectTimeBtn) {
            chooseDateDialog();
        } else if (view == mCoverImage) {
            requestGallery();
        }
    }

    private void chooseDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Date date = new Date(i - 1900, i1, i2);
                        mDeadLineText.setText(TimeUtil.formatDate(date, "yyyy-MM-dd"));
                    }
                }, year, month, day);
        dialog.show();
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
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    List<Uri> resultList = Matisse.obtainResult(data);
                    if (resultList != null && resultList.size() > 0) {
                        cropImage(resultList.get(0));
                    }
                    break;
                case REQUEST_CROP:
                    if (mTempCropImageUri != null) {
                        File file = new File(mTempCropImageUri.getPath());
                        mCoverFile = new BmobFile(file);

                        loadImage(mTempCropImageUri, mCoverImage);
                    }
                    break;
            }
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
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
//        是否缩放
        intent.putExtra("scale", true);
//        设置图片的大小，提高头像的上传速度
        intent.putExtra("outputX", 900);
        intent.putExtra("outputY", 450);
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
}
