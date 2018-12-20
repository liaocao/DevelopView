package com.cookie.developview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    int lineWidth = 0;//记录每一行的宽度
    int lineHeight = 0;//记录每一行的高度
    int height = 0;//记录整个FlowLayout所占高度
    int width = 0;//记录整个FlowLayout所占宽度

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

        int count = getChildCount();
        for (int i=0;i<count;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin +lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > measureWidth){//当当前统计的lineWidth加上子view的宽度大于建议宽度时，就需要进行换行
                //需要换行时重点是flowlayout的width和height
                width = Math.max(lineWidth,childWidth);
                height += lineHeight;
                //因为由于盛不下当前控件，而将此控件调到下一行，所以将此控件的高度和宽度初始化给lineHeight、lineWidth
                lineHeight = childHeight;
                lineWidth = childWidth;
            }else{
                // 否则累加值lineWidth,lineHeight取最大高度
                lineHeight = Math.max(lineHeight,childHeight);
                lineWidth += childWidth;
            }

            //算到最后一行时，漏算了width和height，所以特地加上
            if (i == count -1){
                height += lineHeight;
                width = Math.max(width,lineWidth);
            }

        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth
                : width, (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight
                : height);
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

            if (childWidth + lineWidth > getMeasuredWidth()){
                //如果换行
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            }else{
                lineHeight = Math.max(lineHeight,childHeight);
                lineWidth += childWidth;
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
