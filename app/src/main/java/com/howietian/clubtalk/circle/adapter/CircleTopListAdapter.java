package com.howietian.clubtalk.circle.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.howietian.clubtalk.R;

import java.util.ArrayList;

public class CircleTopListAdapter extends RecyclerView.Adapter<CircleTopListAdapter.CircleTopListViewHolder> {

    private static ArrayList<CircleCard> circleCardList;

    private OnTopItemClickListener mOnTopItemClickListener;

    public CircleTopListAdapter(OnTopItemClickListener listener) {
        this.mOnTopItemClickListener = listener;
        initData();
    }

    @NonNull
    @Override
    public CircleTopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_top_single, parent, false);
        CircleTopListViewHolder holder = new CircleTopListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CircleTopListViewHolder holder, int position) {
        holder.fill(circleCardList, position, mOnTopItemClickListener);
    }

    @Override
    public int getItemCount() {
        return circleCardList.size();
    }

    static class CircleTopListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTopTitleText;
        private TextView mBottomTitleText;
        private CardView mTopCardView;
        private CardView mBottomCardView;

        public CircleTopListViewHolder(@NonNull View itemView) {
            super(itemView);

            mTopTitleText = itemView.findViewById(R.id.text_title_top);
            mBottomTitleText = itemView.findViewById(R.id.text_title_bottom);
            mTopCardView = itemView.findViewById(R.id.card_top);
            mBottomCardView = itemView.findViewById(R.id.card_bottom);
        }

        public void fill(final ArrayList<CircleCard> cardArrayList, final int position, final OnTopItemClickListener listener) {
            final CircleCard circleCard = cardArrayList.get(position);
            if (!TextUtils.isEmpty(circleCard.topTitle)) {
                mTopCardView.setVisibility(View.VISIBLE);
                mTopTitleText.setText(circleCard.topTitle);
                mTopCardView.setCardBackgroundColor(Color.parseColor(circleCard.topColor));
            } else {
                mTopCardView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(circleCard.bottomTitle)) {
                mBottomCardView.setVisibility(View.VISIBLE);
                mBottomTitleText.setText(circleCard.bottomTitle);
                mBottomCardView.setCardBackgroundColor(Color.parseColor(circleCard.bottomColor));
            } else {
                mBottomCardView.setVisibility(View.GONE);
            }

            if (circleCard.topSelected) {
                mTopCardView.setCardBackgroundColor(Color.parseColor("#333333"));
            }

            if (circleCard.bottomSelected) {
                mBottomCardView.setCardBackgroundColor(Color.parseColor("#333333"));
            }

            mTopCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        for (int i = 0; i < cardArrayList.size(); i++) {
                            cardArrayList.get(i).topSelected = false;
                            cardArrayList.get(i).bottomSelected = false;
                        }
                        cardArrayList.get(position).topSelected = true;
                        listener.onTopItemClick(circleCard.topTitle);
                    }
                }
            });

            mBottomCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        for (int i = 0; i < cardArrayList.size(); i++) {
                            cardArrayList.get(i).bottomSelected = false;
                            cardArrayList.get(i).topSelected = false;
                        }
                        cardArrayList.get(position).bottomSelected = true;
                        listener.onTopItemClick(circleCard.bottomTitle);
                    }
                }
            });
        }


    }

    static class CircleCard {
        public String topTitle;
        public String topColor;

        public String bottomTitle;
        public String bottomColor;

        public boolean topSelected;
        public boolean bottomSelected;

        public CircleCard(String topTitle, String topColor, String bottomTitle, String bottomColor) {
            this.topTitle = topTitle;
            this.topColor = topColor;
            this.bottomTitle = bottomTitle;
            this.bottomColor = bottomColor;
        }

        public CircleCard() {
        }
    }

    private void initData() {
        String[] circlesTitle = {"全部", "反馈", "挑战", "情感", "体育", "娱乐", "文艺", "生活", "比赛", "学习"};
        String[] circlesColor = {"#03dac6", "#018786", "#03dac6", "#018786", "#03dac6", "#018786", "#03dac6", "#018786", "#03dac6", "#018786"};

        circleCardList = new ArrayList<>();
        for (int i = 0; i < circlesTitle.length; i = i + 2) {

            CircleCard circleCard = new CircleCard();
            circleCard.topTitle = circlesTitle[i];
            circleCard.topColor = circlesColor[i];
            if (i == 0) {
                circleCard.topSelected = true;
            }
            if (i + 1 < circlesTitle.length) {
                circleCard.bottomTitle = circlesTitle[i + 1];
                circleCard.bottomColor = circlesColor[i + 1];
            }

            circleCardList.add(circleCard);
        }
    }

    public interface OnTopItemClickListener {
        void onTopItemClick(String title);
    }
}
