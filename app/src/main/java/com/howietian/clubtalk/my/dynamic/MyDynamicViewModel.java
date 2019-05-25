package com.howietian.clubtalk.my.dynamic;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.CommentList;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.DynamicList;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.circle.DynamicListDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyDynamicViewModel extends BaseViewModel {
    private MutableLiveData<Boolean> mNotifyFooter = new MutableLiveData<>();
    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();
    private MutableLiveData<String[]> mNotifyItemData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifySwipeRefresh = new MutableLiveData<>();

    private DynamicListDataHelper mDataHelper = new DynamicListDataHelper();
    private User mUser = BmobUser.getCurrentUser(User.class);

    public void start() {
        if (mUser == null) {
            return;
        }
        requestDynamicList(0);
    }

    public void refresh() {
        mDataHelper.clear();
        requestDynamicList(0);
    }

    private void requestDynamicList(final int offset) {
        BmobQuery<Dynamic> query = new BmobQuery<>();
        query.setSkip(offset)
                .setLimit(mDataHelper.getPageCount())
                .order("-createdAt")
                .include("user")
                .addWhereEqualTo("user", mUser);

        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if (e == null) {
                    DynamicList dynamicList = new DynamicList(list);
                    if (list != null && list.size() > 0) {
                        requestDynamicComment(dynamicList, offset);
                    } else {
                        mNotifyData.postValue(null);
                        mNotifyFooter.postValue(false);
                        mNotifySwipeRefresh.postValue(false);
                    }
                } else {
                    toast("网络开小差了");
                }
            }
        });
    }

    private void requestDynamicComment(final DynamicList dynamicList, final int offset) {

        List<String> dynamicIdList = new ArrayList<>();
        for (int i = 0; i < dynamicList.size(); i++) {
            dynamicIdList.add(dynamicList.get(i).getObjectId());
        }
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereContainedIn("dynamicId", dynamicIdList);
        query.include("user,replyUser");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    CommentList commentList = new CommentList(list);
                    if (mDataHelper.setDynamicList(dynamicList, commentList, offset)) {
                        mNotifyData.postValue(null);
                    }
                } else {
                    toast("网络开小差了");
                }

                mNotifyFooter.postValue(mDataHelper.isHasMore());
                if (offset == 0) {
                    mNotifySwipeRefresh.postValue(false);
                }
            }
        });
    }

    public void like(boolean isLike, int position, Dynamic dynamic, User user) {
        if (isLike) {
            cancelLike(position, dynamic, user);
        } else {
            toLike(position, dynamic, user);
        }
    }

    private void toLike(final int position, final Dynamic dynamic, final User user) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        dynamic.setLike(relation);

        ArrayList<String> likedIdList = dynamic.getLikedIdList();
        if (likedIdList == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = dynamic.getLikedIdList();
        }

        likedIdList.add(user.getObjectId());
        final int likeCount = likedIdList.size();
        dynamic.setLikeCount(likeCount);
        dynamic.setLikedIdList(likedIdList);

        dynamic.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    dynamic.setLiked(true);
                    mDataHelper.updateItem(position, dynamic);
                    mNotifyItemData.postValue(new String[]{String.valueOf(position), "like"});
                } else {

                    toast("动态点赞失败");
                }
            }
        });
    }

    private void cancelLike(final int position, final Dynamic dynamic, final User user) {
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        dynamic.setLike(relation);

        ArrayList<String> likedIdList = dynamic.getLikedIdList();
        if (likedIdList == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = dynamic.getLikedIdList();
        }

        if (likedIdList.contains(user.getObjectId())) {
            likedIdList.remove(user.getObjectId());
        }
        dynamic.setLikedIdList(likedIdList);
        dynamic.setLikeCount(likedIdList.size());

        dynamic.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    dynamic.setLiked(false);
                    mDataHelper.updateItem(position, dynamic);
                    mNotifyItemData.postValue(new String[]{String.valueOf(position), "like"});
                } else {
                    toast("文章取消点赞失败");
                }
            }
        });
    }

    public void comment(final int position, final Comment comment) {


        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mDataHelper.getItem(position).getCommentDataHelper().insertComment(comment);
                    mNotifyItemData.postValue(new String[]{String.valueOf(position), "comment"});

//                    updateDynamicCommentCount(dynamic);
                } else {
                    toast("评论上传失败");
                }
            }
        });
    }

    public void next() {
        if (mDataHelper.isHasMore()) {
            requestDynamicList(mDataHelper.getOffset());
        } else {
            mNotifyFooter.postValue(false);
        }
    }

    public void bindData(IDataBinder dataBinder) {
        dataBinder.bind(mDataHelper);
    }

    public MutableLiveData<Boolean> getNotifyFooter() {
        return mNotifyFooter;
    }

    public MutableLiveData<Void> getNotifyData() {
        return mNotifyData;
    }

    public MutableLiveData<Boolean> getNotifySwipeRefresh() {
        return mNotifySwipeRefresh;
    }

    public MutableLiveData<String[]> getNotifyItemData() {
        return mNotifyItemData;
    }
}
