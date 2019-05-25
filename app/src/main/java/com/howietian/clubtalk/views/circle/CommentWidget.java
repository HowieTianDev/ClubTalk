package com.howietian.clubtalk.views.circle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.circle.CircleFragment;
import com.howietian.clubtalk.views.circle.listener.ClickableSpanEx;
import com.howietian.clubtalk.views.circle.listener.CommentClick;

/**
 * Created by 83624 on 2017/cup_7/21.
 */

public class CommentWidget extends AppCompatTextView {
    //    用户名颜色
    private int textColor = 0xff517fae;
    private static final int textSize = 14;
    private CircleFragment fragment = CircleFragment.newInstance();
    SpannableStringBuilderCompat mSpannableStringBuilderCompat;


    public CommentWidget(Context context) {
        this(context, null);
    }

    public CommentWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LinkMovementMethod.getInstance());
        setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    public void setCommentText(Comment comment) {
        if (comment == null) return;
        try {
            setTag(comment);
            createCommentStringBuilder(comment);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断到底是回复，原创评论
     *
     * @param comment
     */

    private void createCommentStringBuilder(@NonNull final Comment comment) {

        if (mSpannableStringBuilderCompat == null) {
            mSpannableStringBuilderCompat = new SpannableStringBuilderCompat();
        } else {
            mSpannableStringBuilderCompat.clear();
            mSpannableStringBuilderCompat.clearSpans();
        }
        String content = ": " + comment.getContent().trim();
        boolean isReply = (comment.getReplyUser() != null);
        // 用户B为空，证明是一条原创评论
        if (comment.getUser() != null && !isReply) {
            CommentClick userClickSpan = new CommentClick.Builder(getContext(), comment.getUser()).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(comment.getUser().getNickName(), userClickSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableStringBuilderCompat.append(content);
        } else if (comment.getUser() != null && isReply) {
            //用户A，B不空，证明是回复评论
            CommentClick userClickSpan = new CommentClick.Builder(getContext(), comment.getUser()).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(comment.getUser().getNickName(), userClickSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableStringBuilderCompat.append(" 回复 ");
            CommentClick replyUserClickSpan = new CommentClick.Builder(getContext(), comment.getReplyUser()).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(comment.getReplyUser().getNickName(), replyUserClickSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableStringBuilderCompat.append(content);
        }
        setText(mSpannableStringBuilderCompat);
    }

    public Comment getData() throws ClassCastException {
        return (Comment) getTag();
    }

}
