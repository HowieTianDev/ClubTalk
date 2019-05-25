package com.howietian.clubtalk.home.event.detail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.home.listener.OnBottomClickListener;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class EventDetailViewModel extends BaseViewModel {

    private MutableLiveData<Void> mNotifyTop = new MutableLiveData<>();

    private MutableLiveData<Void> mNotifyComment = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyCommentItem = new MutableLiveData<>();


    private EventTopDataHelper mEventTopDataHelper = new EventTopDataHelper();
    private EventCommentDataHelper mEventCommentDataHelper = new EventCommentDataHelper();
    private Event mEvent;

    public void start(Event event) {
        mEvent = event;
        if (mEventTopDataHelper.setEvent(event)) {

            mEventCommentDataHelper.setEventLikeCount(event.getLikeCount());
            mNotifyTop.postValue(null);

            requestComment();
        }
    }

    private void requestComment() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("event", mEvent)
                .order("createdAt")
                .include("user")
                .findObjects(new FindListener<Comment>() {
                    @Override
                    public void done(List<Comment> list, BmobException e) {
                        if (e == null) {
                            if (list != null) {
                                if (mEventCommentDataHelper.setCommentList(list)) {
                                    mNotifyComment.postValue(null);
                                }
                            }
                        } else {
                            toast("网络不给力");
                        }
                    }
                });
    }

    public void insertComment(Comment comment) {
        if (comment == null) {
            return;
        }
        mEventCommentDataHelper.insertComment(comment);
        mNotifyComment.postValue(null);
    }


    public void notifyCommentTitle(String type, int count) {
        if (type.equals("like")) {
            mEventCommentDataHelper.setEventLikeCount(count);
        }

        mNotifyCommentItem.postValue(0);
    }

    public void comment(String text, final Event event, User user, final OnBottomClickListener listener) {
        final Comment comment = new Comment();
        comment.setContent(text);
        comment.setUser(user);
        comment.setEvent(event);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onCommentSucceed(comment, event.getCommentCount());
                    }
                    updateEventCommentCount(event);
                } else {
                    toast("评论上传失败");
                }
            }
        });
    }

    private void updateEventCommentCount(Event event) {
        int commentCount = event.getCommentCount();
        commentCount++;
        event.setCommentCount(commentCount);
        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("thy", "success");
                }
            }
        });
    }

    public void joinEvent(Event event, User user) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        event.setJoin(relation);

        ArrayList<String> joinedIdList = null;
        if (event.getJoinedIdList() == null) {
            joinedIdList = new ArrayList<>();
        } else {
            joinedIdList = event.getJoinedIdList();
        }

        joinedIdList.add(user.getObjectId());
        event.setJoinedIdList(joinedIdList);

        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("报名成功请及时参加");
                    mNotifyTop.postValue(null);
                } else {
                    toast("报名失败，请稍后重试");
                }
            }
        });
    }

    public void like(boolean isLike, Event event, User user, OnBottomClickListener listener) {
        // 点赞
        if (isLike) {
            toLike(event, user, listener);
        } else { // 取消点赞
            cancelLike(event, user, listener);
        }
    }

    private void toLike(final Event event, final User user, final OnBottomClickListener listener) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        event.setLike(relation);

        ArrayList<String> likedIdList = event.getLikedIdList();
        if (likedIdList == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = event.getLikedIdList();
        }

        likedIdList.add(user.getObjectId());
        final int likeCount = likedIdList.size();
        event.setLikeCount(likeCount);
        event.setLikedIdList(likedIdList);

        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onLikeSucceed(likeCount);
                    }
                } else {
                    if (listener != null) {
                        listener.onLikeFail();
                    }
                    toast("文章点赞失败");
                }
            }
        });
    }

    private void cancelLike(final Event event, final User user, final OnBottomClickListener listener) {
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        event.setLike(relation);

        ArrayList<String> likedIdList = event.getLikedIdList();
        if (likedIdList == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = event.getLikedIdList();
        }

        if (likedIdList.contains(user.getObjectId())) {
            likedIdList.remove(user.getObjectId());
        }
        event.setLikedIdList(likedIdList);
        event.setLikeCount(likedIdList.size());
        final int likeCount = likedIdList.size();

        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.cancelLikeSucceed(likeCount);
                    }
                } else {
                    if (listener != null) {
                        listener.cancelLikeFail();
                    }
                    toast("文章取消点赞失败");
                }
            }
        });
    }


    public void collect(boolean isCollect, Event event, User user, OnBottomClickListener listener) {
        if (isCollect) {
            toCollect(event, user, listener);
        } else {
            cancelCollect(event, user, listener);
        }
    }

    private void toCollect(final Event event, final User user, final OnBottomClickListener listener) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        event.setCollect(relation);

        ArrayList<String> collectedIdList = event.getCollectedIdList();
        if (collectedIdList == null) {
            collectedIdList = new ArrayList<>();
        } else {
            collectedIdList = event.getCollectedIdList();
        }
        collectedIdList.add(user.getObjectId());
        event.setCollectedIdList(collectedIdList);

        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onCollectSucceed();
                    }
                } else {
                    if (listener != null) {
                        listener.onCollectFail();
                    }
                    toast("文章收藏失败");
                }
            }
        });
    }

    private void cancelCollect(final Event event, final User user, final OnBottomClickListener listener) {
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        event.setCollect(relation);

        ArrayList<String> collectedIdList = event.getCollectedIdList();
        if (collectedIdList == null) {
            collectedIdList = new ArrayList<>();
        } else {
            collectedIdList = event.getCollectedIdList();
        }

        if (collectedIdList.contains(user.getObjectId())) {
            collectedIdList.remove(user.getObjectId());
        }
        event.setCollectedIdList(collectedIdList);

        event.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.cancelCollectSucceed();
                    }
                } else {
                    if (listener != null) {
                        listener.cancelCollectFail();
                    }
                    toast("取消文章收藏失败");
                }
            }
        });
    }

    public void commentLike(final int position, Comment comment, User user) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        comment.setLike(relation);

        ArrayList<String> likedIdList;
        if (comment.getLikedIdList() == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = comment.getLikedIdList();
        }
        likedIdList.add(user.getObjectId());

        comment.setLikedIdList(likedIdList);
        comment.setLikeCount(likedIdList.size());
        comment.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEventCommentDataHelper.setItemLiked(position, true);
                } else {
                    mEventCommentDataHelper.setItemLiked(position, false);
                }
                mNotifyCommentItem.postValue(position);
            }
        });
    }

    public void commentCancelLike(final int position, Comment comment, User user) {
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        comment.setLike(relation);

        ArrayList<String> likedIdList;
        if (comment.getLikedIdList() == null) {
            likedIdList = new ArrayList<>();
        } else {
            likedIdList = comment.getLikedIdList();
        }

        if (likedIdList != null && likedIdList.contains(user.getObjectId())) {
            likedIdList.remove(user.getObjectId());
        }

        comment.setLikedIdList(likedIdList);
        comment.setLikeCount(likedIdList.size());
        comment.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEventCommentDataHelper.setItemLiked(position, false);
                } else {
                    mEventCommentDataHelper.setItemLiked(position, true);
                }
                mNotifyCommentItem.postValue(position);
            }
        });
    }


    public void bindTopData(IDataBinder<EventTopDataHelper> dataBinder) {
        dataBinder.bind(mEventTopDataHelper);
    }

    public void bindCommentData(IDataBinder<EventCommentDataHelper> dataBinder) {
        dataBinder.bind(mEventCommentDataHelper);
    }

    public MutableLiveData<Void> getNotifyTop() {
        return mNotifyTop;
    }

    public MutableLiveData<Void> getNotifyComment() {
        return mNotifyComment;
    }

    public MutableLiveData<Integer> getNotifyCommentItem() {
        return mNotifyCommentItem;
    }

}
