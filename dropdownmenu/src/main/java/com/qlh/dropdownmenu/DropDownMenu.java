package com.qlh.dropdownmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by qlh on 2017/7/5.
 * 根布局DropDownMenu主要包括：tabMenuView，underLine，containerView（包含popupMenuViews，maskView）
 */
public class DropDownMenu extends LinearLayout {

    private Context context;
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    //menu tab高度,暂时不用
    private float tabMenuHeightPercent = ViewGroup.LayoutParams.WRAP_CONTENT;
    //是否使用高度百分比
    private boolean isUsePopMenuHeightPercent = false;
    //menu弹框最大高度，按照屏幕比例计算
    private float popMenuHeightPercent = 0.5f;


    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        //设置根布局为垂直方向
        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;//菜单导航背景颜色
        int underlineColor = 0xffcccccc;//下划线颜色
        //自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);//下划线颜色
        dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);//分割线颜色
        //文本选中颜色
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
        //文本未选中颜色
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
        //菜单导航背景颜色
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        //遮罩层颜色
        maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
        //菜单导航文本字体大小
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
        //菜单导航item选中时箭头图标
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
        //菜单导航item未选中时箭头图标
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
        //是否使用高度百分比
        isUsePopMenuHeightPercent = a.getBoolean(R.styleable
                .DropDownMenu_ddIsUsePopMenuHeightPercent,isUsePopMenuHeightPercent);
        //菜单导航高度
        popMenuHeightPercent = a.getFloat(R.styleable.DropDownMenu_ddpopMenuHeightPercent, popMenuHeightPercent);
        a.recycle();

        //初始化tabMenuView并添加到主布局DropDownMenu
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为主布局DropDownMenu添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx
                (0.5f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());

        if (isUsePopMenuHeightPercent)
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT,(int)(DeviceUtils.getScreenSize(getContext()).y * popMenuHeightPercent)));

        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    private void addTab(@NonNull List<String> tabTexts, int i) {

        final View tabView = LayoutInflater.from(context).inflate(R.layout.tab_menu_ly,null);
        tabView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams
                .WRAP_CONTENT,1.0f));
        tabView.setPadding(dpTpPx(0), dpTpPx(12), dpTpPx(0), dpTpPx(12));

        TextView tabText = (TextView) tabView.findViewById(R.id.tab_menu_txt);
        ImageView tabImg = (ImageView) tabView.findViewById(R.id.tab_menu_img);

        tabText.setSingleLine();
        tabText.setEllipsize(TextUtils.TruncateAt.END);
        tabText.setTextSize(TypedValue.COMPLEX_UNIT_PX,menuTextSize);
        tabText.setTextColor(textUnselectedColor);
        tabText.setText(tabTexts.get(i));
        LayoutParams lp = (LayoutParams) tabText.getLayoutParams();
        lp.rightMargin = (int) getResources().getDimension(R.dimen.txt_to_img_distance);
        tabText.setLayoutParams(lp);

        tabImg.setImageResource(menuUnselectedIcon);
        tabImg.setLayoutParams(new LayoutParams((int)(getResources().getDimension(R.dimen
                .img_icon_width)),(int) (getResources().getDimension(R.dimen
                .img_icon_height))));

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tabView);
            }
        });
        tabMenuView.addView(tabView);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {

            LinearLayout ly = (LinearLayout) tabMenuView.getChildAt(current_tab_position);
            ((TextView)ly.getChildAt(0)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {

            LinearLayout ly = (LinearLayout) tabMenuView.getChildAt(current_tab_position);
            ((TextView)ly.getChildAt(0)).setTextColor(textUnselectedColor);
            ((ImageView)ly.getChildAt(1)).setImageResource(menuUnselectedIcon);

            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {

        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;

                    LinearLayout ly = (LinearLayout) tabMenuView.getChildAt(i);
                    ((TextView)ly.getChildAt(0)).setTextColor(textSelectedColor);
                    ((ImageView)ly.getChildAt(1)).setImageResource(menuSelectedIcon);

                }
            } else {

                LinearLayout ly = (LinearLayout) tabMenuView.getChildAt(i);
                ((TextView)ly.getChildAt(0)).setTextColor(textUnselectedColor);
                ((ImageView)ly.getChildAt(1)).setImageResource(menuUnselectedIcon);

                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
