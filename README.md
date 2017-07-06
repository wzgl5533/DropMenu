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
        app:ddunderlineColor="@color/divider"   /下划线颜色
        app:ddmenuMenuHeightPercent="0.5"/>
        ```
