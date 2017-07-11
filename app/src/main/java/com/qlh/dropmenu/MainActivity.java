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
        final String[] levelOneMenu = {"全部", "原木", "板材", "方料","灌木"};
        final String[][] levelTwoMenu = {
                {"辐射松", "铁杉", "樟子松", "云杉", "红橡", "白橡", "樟木", "桦木", "沙比利", "奥坎", "榉木", "落叶松"},
                {"辐射松", "铁杉", "樟子松", "云杉"},
                {"红橡", "白橡", "樟木", "桦木"},
                {"沙比利", "奥坎", "榉木", "落叶松"},
                {"针叶", "观杨", "梧桐", "香樟树"}
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
