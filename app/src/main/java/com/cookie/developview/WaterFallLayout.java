package com.cookie.developview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class WaterFallLayout extends ViewGroup {

    private int columns = 3;
    private int hSpace = 20;
    private int vSpace = 20;
    private int childWidth = 0;
    private int top[];

    public WaterFallLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        top = new int[columns];
    }

    public WaterFallLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterFallLayout(Context context) {
        this(context, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        childWidth = (sizeWidth - (columns - 1) * hSpace) / columns;

        int wrapWidth;
        int childCount = getChildCount();
        if (childCount < columns) {//如果子控件没有超过三列，那么总的控件宽度就是当前个数子控件的宽度总和组成
            wrapWidth = childCount * childWidth + (childCount - 1) * hSpace;
        } else {
            wrapWidth = sizeWidth;//如果子控件数超过了三个，那说明肯定能撑满一行了，宽度也就是父控件建议的sizeWidth宽度了
        }

        clearTop();//每次测量前清除三列高度数组值
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            int childHeight = child.getMeasuredHeight() * childWidth / child.getMeasuredWidth();//等比例的取到缩放的高度
            int minColum = getMinHeightColum();//取到当前高度最低的那一列
            top[minColum] += vSpace + childHeight;//将子控件高度添加到这一列
        }
        int wrapHeight;
        wrapHeight = getMaxHeight();//取到高度最大的值
        setMeasuredDimension(widthMode == MeasureSpec.AT_MOST ? wrapWidth : sizeWidth, wrapHeight);
    }

    private void clearTop() {
        for (int i = 0; i < columns; i++) {
            top[i] = 0;
        }
    }

    private int getMinHeightColum() {
        int minColum = 0;
        for (int i = 0; i < columns; i++) {
            if (top[i] < top[minColum]) {//挨个跟最小值比较
                minColum = i;
            }
        }
        return minColum;
    }

    private int getMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < columns; i++) {
            if (top[i] > maxHeight) {
                maxHeight = top[i];
            }
        }
        return maxHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        clearTop();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            int childHeight = child.getMeasuredHeight() * childWidth / child.getMeasuredWidth();

            int minColum = getMinHeightColum();//获取最小高度的那行

            int tleft = minColum * (childWidth + hSpace);//取到对应列的left值
            int ttop = top[minColum];
            int tright = tleft + childWidth;
            int tbottom = ttop + childHeight;

            top[minColum] += vSpace + childHeight;
            child.layout(tleft, ttop, tright, tbottom);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        for (int i = 0; i < getChildCount(); i++) {
            final int index = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, index);
                }
            });
        }
    }
}
