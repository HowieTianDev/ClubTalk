package com.howietian.clubtalk.views.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.utils.UIHelper;


/**
 * Created by 83624 on 2017/cup_7/19.
 */

public class ClickShowMoreLayout extends LinearLayout implements View.OnClickListener {

    public static final int CLOSE = 0;
    public static final int OPEN = 1;

    private TextView mTextView;
    private TextView mClickToShowTextView;

    private int textColor;
    private int textSize;
    private int showLine;
    private String clickText;

    private boolean hasMore;
    private boolean hasGetLineCount = false;

    private static final SparseIntArray TEXT_STATE = new SparseIntArray();


    public ClickShowMoreLayout(Context context) {
        this(context, null);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickShowMoreLayout);
        textColor = typedArray.getColor(R.styleable.ClickShowMoreLayout_text_color, 0xff1a1a1a);
        textSize = typedArray.getDimensionPixelSize(R.styleable.ClickShowMoreLayout_text_size, 16);
        showLine = typedArray.getInt(R.styleable.ClickShowMoreLayout_show_line, 8);
        clickText = typedArray.getString(R.styleable.ClickShowMoreLayout_click_text);

        if (TextUtils.isEmpty(clickText)) {
            clickText = "全文";
        }

        typedArray.recycle();

        initView(context);
    }


    private void initView(Context context) {
        mTextView = new TextView(context);
        mClickToShowTextView = new TextView(context);

        mTextView.setTextSize(textSize);
        mTextView.setTextColor(textColor);
        mTextView.setMaxLines(showLine);

        mClickToShowTextView.setBackground(getResources().getDrawable(R.drawable.show_more_selector));
        mClickToShowTextView.setTextSize(textSize);
        mClickToShowTextView.setTextColor(getResources().getColor(R.color.nick));
        mClickToShowTextView.setText(clickText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.topMargin = UIHelper.dipToPx(10f);
        mClickToShowTextView.setLayoutParams(params);
        mClickToShowTextView.setOnClickListener(this);

        setOrientation(VERTICAL);
        addView(mTextView);
        addView(mClickToShowTextView);
    }

    /**
     * 通过判断点击部分的文字和clickText是否相同，来确定具体的行为
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        boolean needOpen = TextUtils.equals(((TextView) view).getText().toString(), clickText);
        setState(needOpen ? OPEN : CLOSE);
    }

    /**
     * 设置全文or收起的状态，并将该状态保存至sparseintarray中
     *
     * @param state
     */

    public void setState(int state) {
        switch (state) {
            case CLOSE:
                mTextView.setMaxLines(showLine);
                mClickToShowTextView.setText(clickText);
                break;
            case OPEN:
                mTextView.setMaxLines(Integer.MAX_VALUE);
                mClickToShowTextView.setText("收起");
                break;
        }
    }

    /**
     * 要判断是否有更多的内容，只能通过文字的行数和我们规定的最大行数比较来确定，但是如果文字都没绘制
     * 上去，肯定是获得不到lines的，onDraw()方法里，是不能做判断的。所以，我们只能在onPreDraw()方
     * 法里做判断。所以，在onPreDraw()里判断
     *
     * @param str
     */
    public void setText(String str) {
        mTextView.setText(str);
        mTextView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                hasMore = mTextView.getLineCount() > showLine;
//                  这里只是做了控制下面全文/收起的显示还是不显示，如果行数大于规定行数，显示；反之，不显示
                mClickToShowTextView.setVisibility(hasMore ? VISIBLE : GONE);
//                  把监听移除
                mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
//          相当于初始化状态了，默认是关闭状态，无所谓text行数，因为text过少时，不可见了，也就不可能点击
        setState(CLOSE);
    }

    /**
     * 返回显示的文字
     *
     * @return
     */
    public CharSequence getText() {
        return mTextView.getText();
    }
}
