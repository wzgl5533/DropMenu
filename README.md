## 简介
这是一个改写过的多级下拉菜单（给自己项目使用的），可以根据自己的需求进行添加各种菜单样式，在很多App上都能看到这个效果，如美团，爱奇艺电影票等，单级菜单，
多级菜单和其他自定义的菜单。本项目参考下面的开源库，搭了基本的框架，可以自己根据要求添加菜单视图。

主要参考开源库：[DropDownMenu](https://github.com/dongjunkun/DropDownMenu)。在此表示感谢，喜欢的可以自己参考原库进行改写
## 特色
* 支持多级菜单，对应类**MultiMenusView**
* 支持单级菜单，对应类**SingleMenuView**
* 当然你也可以完全自定义下拉列表的展现形式，使用复杂的布局等
* 你可以完全自定义你的菜单样式，我这里只是封装了一些实用的方法，Tab的切换效果，菜单显示隐藏效果等
* 并非用popupWindow实现，无卡顿
## ScreenShot
![dropmenu.gif](http://note.youdao.com/yws/public/resource/7ef1d889de68ddafc07874e25cadfd57/xmlnote/8C9AEFB246474938BA5DA99FEFA087A0/10690)

## 使用
添加DropDownMenu 到你的布局文件，如下：
```
<com.qlh.dropdownmenu.DropDownMenu
        android:id="@+id/dropDownMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:dddividerColor="@color/divider" //分割线颜色
        app:ddmaskColor="@color/mask_color" //遮罩颜色，一般是半透明
        app:ddmenuBackgroundColor="@color/white" //tab 背景颜色
        app:ddmenuSelectedIcon="@drawable/drop_down_selected_icon" //tab选中状态图标
        app:ddmenuTextSize="@dimen/x40"   //tab字体大小
        app:ddmenuUnselectedIcon="@drawable/drop_down_unselected_icon" //tab未选中状态图标
        app:ddtextSelectedColor="@color/blue_light"  //tab选中颜色
        app:ddtextUnselectedColor="@color/drop_down_unselected" //tab未选中颜色
        app:ddunderlineColor="@color/divider"   //下划线颜色
        app:ddmenuMenuHeightPercent="0.6" //设置下拉弹框的最大高度比例，根据屏幕高度计算 />
```

## DropDownMenu类的主要结构：

根布局包含：

* tabMenuView —— 菜单顶部导航布局
* underLine —— 下划线 
* containerView（包含contentView[内容],maskView[遮罩],popupMenuViews[下拉弹框]）—— 底部容器
*  说明：这些布局都是可以进行自己定义，根据自己的项目需求进行改装

## 本项目主文件MainActivity.java
```
package com.qlh.dropmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;
import com.qlh.dropdownmenu.view.SingleMenuView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivity instance;
    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu mDropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private SingleMenuView singleMenuView;//单级菜单
    //内容视图
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        instance = this;
        initView();
        initMenus();
        initListener();

    }

    private void initView() {

        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
    }

    private void initMenus() {

        headers = new String[]{"木材类别", "产地"};
        //初始化多级菜单
        final String[] levelOneMenu = {"全部", "原木", "板材", "方料"};
        final String[][] levelTwoMenu = {
                {"辐射松", "铁杉", "樟子松", "云杉", "红橡", "白橡", "樟木", "桦木", "沙比利", "奥坎", "榉木", "落叶松"},
                {"辐射松", "铁杉", "樟子松", "云杉"},
                {"红橡", "白橡", "樟木", "桦木"},
                {"沙比利", "奥坎", "榉木", "落叶松"}
        };
        multiMenusView = new MultiMenusView(instance,levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化单级菜单
        String[] addressArrays = {"不限","澳大利亚", "美国", "英国", "法国", "日本", "新西兰", "印度"};
        singleMenuView = new SingleMenuView(instance,addressArrays);
        popupViews.add(singleMenuView);
        //初始化内容视图
        contentView = LayoutInflater.from(instance).inflate(R.layout.content_view,null);
        //装载
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initListener() {

        //下拉菜单
        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                mDropDownMenu.setTabText(showText);
                mDropDownMenu.closeMenu();
            }
        });
        singleMenuView.setOnSelectListener(new SingleMenuView.OnSelectListener() {
            @Override
            public void getValue(int position, String showText) {
                mDropDownMenu.setTabText(showText);
                mDropDownMenu.closeMenu();
            }
        });
    }
}
```
## 备注
如果你要了解更多，可以直接看源码或者关注原作者的项目，喝水不忘挖井人
> 可以直接添加依赖库到工程中（As），ES的只能复制了

## 关于我的点滴

简书[简书](http://www.jianshu.com/p/0a313554364b)

CSDN[CSDN](http://blog.csdn.net/wzgl708937822)
