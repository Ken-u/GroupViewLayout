package com.wiipu.groupviewlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiipu.groupviewlayout.R;

/**
 * Created by Ken~Jc on 2016/3/21.
 */
public class GroupViewLayout extends LinearLayout {

    private TextView mTitleText;

    private int mContentHeight = 0;
    private int mTitleHeight = 0;

//    标题栏控件，设定监听事件以达到显示或隐藏抽屉的效果
    private RelativeLayout mTitleView;
//    隐藏或显示的抽屉中的控件
    private RelativeLayout mHideView;
//    标题栏右边的指示器
    private ImageView mIndicator;

//    是否展开的标志
    private boolean isExpand;

//    动画
    private Animation animationDown;
    private Animation animationUp;
//    上下文
    private Context mContext;

//    指示器的两个状态的图片Id
    private int mPic_Closed;
    private int mPic_Opened;

//    设定标题栏的文字、字号、颜色以及图片
    public void setTitle(String titleText,float titleSize,int titleColor,int indicatorClosed,int indicatorOpened){
        mTitleText.setText(titleText);
        mTitleText.setTextColor(titleColor);
        mTitleText.setTextSize(titleSize);
        mPic_Closed=indicatorClosed;
        mPic_Opened=indicatorOpened;
    }

//        将想要放到抽屉里的RelativeLayout视图传入进行设置
    public void settingGroupView(RelativeLayout hideView){
        this.mHideView=hideView;
        this.mTitleView.setOnClickListener(new ExpandListener());
        mHideView.setVisibility(View.GONE);
    }

//        将自定义的TitleView和想要放到抽屉里的RelativeLayout视图传入进行设置
//        即可以对自定义的View当作抽屉开关
    public void settingGroupView(RelativeLayout titleView,RelativeLayout hideView){
//        获取当前控件的布局参数，并把当前高度设置为0，即隐藏默认的标题栏
        LinearLayout.LayoutParams linearParams=(LinearLayout.LayoutParams)mTitleView.getLayoutParams();
        linearParams.height=0;
//        默认的标题栏被隐藏了之后，再传入需要自定义的标题栏
        this.mTitleView=titleView;
        this.mHideView=hideView;
        this.mTitleView.setOnClickListener(new ExpandListener());
        mHideView.setVisibility(View.GONE);
    }

//    重写onMeasure方法
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        if (this.mContentHeight == 0) {
            this.mHideView.measure(widthMeasureSpec, 0);
            this.mContentHeight = this.mHideView.getMeasuredHeight();
        }
        if (this.mTitleHeight == 0) {
            this.mTitleView.measure(widthMeasureSpec, 0);
            this.mTitleHeight = this.mTitleView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    抽屉动画监听器
    private class ExpandListener implements View.OnClickListener {
        @Override
        public final void onClick(View paramView) {
            //clearAnimation是view的方法
            clearAnimation();
            if (!isExpand) {
                if (animationDown == null) {
                    animationDown = new DropDownAnim(mHideView,
                            mContentHeight, true);
                    animationDown.setDuration(200); // SUPPRESS CHECKSTYLE
                }
                startAnimation(animationDown);
                mHideView.startAnimation(AnimationUtils.loadAnimation(
                        mContext, R.anim.animalpha));
                mIndicator.setImageResource(mPic_Opened);
                isExpand = true;
            } else {
                isExpand = false;
                if (animationUp == null) {
                    animationUp = new DropDownAnim(mHideView,
                            mContentHeight, false);
                    animationUp.setDuration(200); // SUPPRESS CHECKSTYLE
                }
                startAnimation(animationUp);
                mIndicator.setImageResource(mPic_Closed);
            }
        }
    }

//    创建控件时从资源文件layout的xml中传入数据
    public GroupViewLayout(Context context,AttributeSet attrs){
        super(context, attrs);
        this.mContext=context;

        LayoutInflater.from(context).inflate(R.layout.group_view, this);
        mTitleText=(TextView)findViewById(R.id.tv_title_text);
        mTitleView=(RelativeLayout)findViewById(R.id.ll_group_view);
        mIndicator=(ImageView)findViewById(R.id.iv_right_pic);

//        获得自定义属性并赋值
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.GroupViewLayout);
        int picClosed=typedArray.getResourceId(R.styleable.GroupViewLayout_indicator_Closed, R.drawable.update_detail_right);
        int picOpened=typedArray.getResourceId(R.styleable.GroupViewLayout_indicator_Opened,R.drawable.update_detail_down);
        String text=typedArray.getString(R.styleable.GroupViewLayout_titleText);
        int color=typedArray.getColor(R.styleable.GroupViewLayout_titleText_Color, 0x38ad5a);
        float size=typedArray.getDimension(R.styleable.GroupViewLayout_titleText_Size, 18);

//        释放资源
        typedArray.recycle();

//        将自定义属性值赋给自定义控件
        setTitle(text,size,color,picClosed,picOpened);

    }

//    动画
    class DropDownAnim extends Animation {
        /** 目标的高度 */
        private int targetHeight;
        /** 目标view */
        private View view;
        /** 是否向下展开 */
        private boolean down;

        /**
         * 构造方法
         *
         * @param targetview
         *            需要被展现的view
         * @param vieweight
         *            目的高
         * @param isdown
         *            true:向下展开，false:收起
         */
        public DropDownAnim(View targetview, int vieweight, boolean isdown) {
            this.view = targetview;
            this.targetHeight = vieweight;
            this.down = isdown;
        }
        //down的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            int newHeight;
            if (down) {
                newHeight = (int) (targetHeight * interpolatedTime);
            } else {
                newHeight = (int) (targetHeight * (1 - interpolatedTime));
            }
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
