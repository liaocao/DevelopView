package com.cookie.developview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyLinLayout extends ViewGroup {

    public MyLinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //这个方法的目的是求出子view累计的总宽度和总高度，再交给setMeasuredDimension方法决定是否采用
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        int width = 0;
        int count = getChildCount();
        for (int i=0;i<count;i++) {
            //测量子控件
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //获得子控件的高度和宽度
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;

            //得到最大宽度，并且累加高度
            height += childHeight;
            width = Math.max(childWidth, width);
        }//原来width和height是通过计算子控件的宽高度得来的

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth: width, (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight: height);
    }//EXACTLY对应的是MATCH_PARENT和具体的数值，就不需要计算子view累计的值了，所以对应的就是measureWidth

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //这个方法的目的是将其包含的子View全部布局好
        int top = 0;
        int count = getChildCount();
        for (int i=0;i<count;i++) {

            View child = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;

            child.layout(0, top, childWidth, top + childHeight);
            top += childHeight;
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }
}