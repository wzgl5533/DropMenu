package com.qlh.dropdownmenu.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qlh.dropdownmenu.R;
import com.qlh.dropdownmenu.adapter.TextAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class MultiMenusView extends LinearLayout implements ViewBaseAction {

    private ListView levelOneMenuListView;
    private ListView levelTwoMenuListView;
    /**一级菜单*/
    private String[] levelOneMenu;
    private ArrayList<String> levelOneMenusList = new ArrayList<String>();
    /**二级菜单*/
    private String[][] levelTwoMenu;
    private LinkedList<String> levelTwoMenusList = new LinkedList<String>();
    /**映射一级和二级菜单*/
    private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
    /**一级菜单ListView适配器*/
    private TextAdapter levelTwoMenuListViewAdapter;
    /**二级菜单ListView适配器*/
    private TextAdapter levelOneMenuListViewAdapter;
    /**选中完成的监听器*/
    private OnSelectListener mOnSelectListener;
    /**一级菜单选中位置*/
    private int levelOneMenuPosition = 0;
    /**二级菜单选中位置*/
    private int levelTwoMenuPosition = 0;
    /**二级菜单最终选中的选项*/
    private String showString = "不限";

    public MultiMenusView(Context context,String[] levelOneMenu,String[][] levelTwoMenu) {
        super(context);
        this.levelOneMenu = levelOneMenu;
        this.levelTwoMenu = levelTwoMenu;
        init(context);
    }

    public MultiMenusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void updateShowText(String showArea, String showBlock) {
        if (showArea == null || showBlock == null) {
            return;
        }
        for (int i = 0; i < levelOneMenusList.size(); i++) {
            if (levelOneMenusList.get(i).equals(showArea)) {
                levelOneMenuListViewAdapter.setSelectedPosition(i);
                levelTwoMenusList.clear();
                if (i < children.size()) {
                    levelTwoMenusList.addAll(children.get(i));
                }
                levelOneMenuPosition = i;
                break;
            }
        }
        for (int j = 0; j < levelTwoMenusList.size(); j++) {
            if (levelTwoMenusList.get(j).replace("不限", "").equals(showBlock.trim())) {
                levelTwoMenuListViewAdapter.setSelectedPosition(j);
                levelTwoMenuPosition = j;
                break;
            }
        }
        setDefaultSelect();
    }


    /**
     * 设置菜单内容
     */
    public void setMenuContent() {

        for (int i = 0; i < levelOneMenu.length; i++) {
            levelOneMenusList.add(levelOneMenu[i]);
            LinkedList<String> tItem = new LinkedList<String>();
            for (int j = 0; j < levelTwoMenu[i].length; j++) {

                tItem.add(levelTwoMenu[i][j]);

            }
            children.put(i, tItem);
        }

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_multi_menu, this, true);
        levelOneMenuListView = (ListView) findViewById(R.id.level_one_menu_ls);
        levelTwoMenuListView = (ListView) findViewById(R.id.level_two_menu_ls);

        setBackgroundColor(Color.parseColor("#FFFFFF"));
        setMenuContent();

        levelOneMenuListViewAdapter = new TextAdapter(context, levelOneMenusList,
                R.drawable.choose_item_selected,
                R.drawable.choose_level_one_menu_item_selector, Color.parseColor("#0084ff"),Color.BLACK);
        levelOneMenuListViewAdapter.setTextSize(getResources().getDimension(R.dimen.x43));
        levelOneMenuListViewAdapter.setSelectedPositionNoNotify(levelOneMenuPosition);
        levelOneMenuListView.setAdapter(levelOneMenuListViewAdapter);
        levelOneMenuListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < children.size()) {
                            levelTwoMenusList.clear();
                            levelTwoMenusList.addAll(children.get(position));
                            levelTwoMenuListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        //初始化菜单
        if (levelOneMenuPosition < children.size())
            levelTwoMenusList.addAll(children.get(levelOneMenuPosition));

        levelTwoMenuListViewAdapter = new TextAdapter(context, levelTwoMenusList,
                R.drawable.choose_item_selected,
                R.drawable.choose_level_two_menu_item_selector,Color.parseColor("#0084ff"),Color.BLACK);
        levelTwoMenuListViewAdapter.setTextSize(getResources().getDimension(R.dimen.x43));
        //levelTwoMenuListViewAdapter.setSelectedPositionNoNotify(levelTwoMenuPosition);
        levelTwoMenuListView.setAdapter(levelTwoMenuListViewAdapter);
        levelTwoMenuListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {

                        showString = levelTwoMenusList.get(position);
                        if (mOnSelectListener != null) {

                            mOnSelectListener.getValue(showString);
                        }

                    }
                });
        //初始化选中二级菜单的选项
        if (levelTwoMenuPosition < levelTwoMenusList.size())
            showString = levelTwoMenusList.get(levelTwoMenuPosition);

        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }

    public void setDefaultSelect() {
        levelOneMenuListView.setSelection(levelOneMenuPosition);
        levelTwoMenuListView.setSelection(levelTwoMenuPosition);
    }

    public String getShowText() {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String showText);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }
}
