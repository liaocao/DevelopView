package com.cookie.developview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int lineWidth = 0;//记录每一行的宽度
        int lineHeight = 0;//记录每一行的高度
        int height = 0;//记录整个FlowLayout所占高度
        int width = 0;//记录整个FlowLayout所占宽度
        int count = getChildCount();
        for (int i=0;i<count;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin +lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(lineWidth + childWidth < measureWidth - getPaddingLeft() - getPaddingRight()){//这样少算了一行高度，因此最后一行时加上
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);

            }else{//换行重新计算下一行宽度lineWidth
                height += lineHeight;//如果换行，必须立刻加上之前的lineHeight

                lineWidth = childWidth;
                lineHeight = childHeight;
            }

            if(i == count - 1){
                height += lineHeight;
            }
            width = Math.max(width, lineWidth);
        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth
                : width + getPaddingLeft() + getPaddingRight(), (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight
                : height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int lineWidth = 0;//累加当前行的行宽
        int lineHeight = 0;//当前行的行高
        int top = 0, left = 0;//当前坐标的top坐标和left坐标

        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;

            //lineWidth是为了判断是否要换行，lineHeight是为了确定换行的位置
            if(childWidth + lineWidth < getMeasuredWidth() - getPaddingLeft() - getPaddingRight()){
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }else{
                //如果换行
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            }

            //计算childView的left,top,right,bottom
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc =lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();
            child.layout(lc, tc, rc, bc);
            //将left置为下一子控件的起始点
            left+=childWidth;
        }
    }
}
