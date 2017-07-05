package com.qlh.dropdownmenu.view;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.qlh.dropdownmenu.R;
import com.qlh.dropdownmenu.adapter.TextAdapter;

import java.util.Arrays;
import java.util.List;



public class SingleMenuView extends RelativeLayout implements ViewBaseAction {

    private ListView mListView;
    private List<String> items;//显示字段
    private OnSelectListener mOnSelectListener;//选中监听器
    private TextAdapter adapter;
    private int whichPosition = 0;//标记选中项顺序
    private String showText;//最终显示在Tab上的字符串
    private Context mContext;

    public String getShowText() {
        return showText;
    }

    public SingleMenuView(Context context, String[] arrays) {
        super(context);
        items = Arrays.asList(arrays);
        init(context);
    }

    public SingleMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SingleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_single_menu, this, true);
        mListView = (ListView) findViewById(R.id.listView);

        setBackgroundColor(Color.parseColor("#FFFFFF"));

        adapter = new TextAdapter(context, items, R.drawable.choose_item_selected,
                R.drawable.choose_level_one_menu_item_selector, Color.parseColor("#0084ff"), Color.BLACK);
        adapter.setTextSize(getResources().getDimension(R.dimen.txt_size));
        //是否需要展示默认的选项？？？
        adapter.setSelectedPositionNoNotify(whichPosition);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                if (mOnSelectListener != null) {
                    showText = items.get(position);
                    mOnSelectListener.getValue(position, items.get(position));
                }
            }
        });
        mListView.setSelection(whichPosition);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(int position, String showText);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

}
