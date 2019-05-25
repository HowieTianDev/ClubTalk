package com.howietian.clubtalk.my.club;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.User;

import cn.bmob.v3.BmobUser;

public class ApplyClubActivity extends BaseActivity<ApplyClubViewModel> {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ApplyClubActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private ImageView mAvatarImage;
    private EditText mClubNameEdit;
    private EditText mRealNameEdit;
    private EditText mSchoolEdit;
    private EditText mPhoneEdit;
    private EditText mClubIntroEdit;

    private User mUser = BmobUser.getCurrentUser(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        observe();

        getViewModel().start(mUser);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_club;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_apply_club);
        setSupportActionBar(mToolbar);

        mAvatarImage = findViewById(R.id.image_avatar);
        mClubNameEdit = findViewById(R.id.edit_club_name);
        mRealNameEdit = findViewById(R.id.edit_real_name);
        mSchoolEdit = findViewById(R.id.edit_school);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mClubIntroEdit = findViewById(R.id.edit_club_intro);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void observe() {
        getViewModel().getNotifyUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getAvatar() != null) {
                    Glide.with(ApplyClubActivity.this).load(user.getAvatar().getUrl()).into(mAvatarImage);
                }
                mRealNameEdit.setText(user.getRealName());
                mPhoneEdit.setText(user.getMobilePhoneNumber());
                mSchoolEdit.setText(user.getSchool());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            String clubName = mClubNameEdit.getText().toString();
            String realName = mRealNameEdit.getText().toString();
            String phone = mPhoneEdit.getText().toString();
            String school = mSchoolEdit.getText().toString();
            String clubIntro = mClubIntroEdit.getText().toString();

            mUser.setClub(true);
            mUser.setNickName(clubName);
            mUser.setRealName(realName);
            mUser.setMobilePhoneNumber(phone);
            mUser.setSchool(school);
            mUser.setClubIntro(clubIntro);
            getViewModel().saveClubInfo(mUser);
        }
        return true;
    }
}
