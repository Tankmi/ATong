package com.jemer.atong.fragment.eyesight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.LayoutUtil;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.view.wheel.OnWheelChangedListener;
import huitx.libztframework.view.wheel.OnWheelScrollListener;
import huitx.libztframework.view.wheel.WheelView;
import huitx.libztframework.view.wheel.adapter.AbstractWheelTextAdapter;

/**
 * 条目选择框
 */
public class EyesightSelUserWheelView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private ArrayList<String> arryLists = new ArrayList<>();
    private ArrayList<String> idLists = new ArrayList<>();
    private List<UserEntity.Data.FamilyData> mFamilyDatas = new ArrayList<>();
    private CalendarTextAdapter mYearAdapter;

    /**
     * 选中的对象下标
     */
    private int selIndex = 0;

    /**
     * 选中的值
     */
    private String selectData;

    private int maxTextSize = 18;
    private int minTextSize = 14;
    /**
     * 是否手动设置初始值
     */
    private boolean issetdata = false;
    /**
     * 显示的条目数
     */
    public int visibileItems = 5;

    private OnSelUserListener onSelUserListener;


    public EyesightSelUserWheelView(Context context) {
        this(context, null);
    }

    public EyesightSelUserWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EyesightSelUserWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    protected void init() {
        View.inflate(context, huitx.libztframework.R.layout.relativelayout_wheelview_birthday, EyesightSelUserWheelView.this);
        findView();

        initList();
        if (!issetdata) initData();

        mYearAdapter = new CalendarTextAdapter(context, arryLists, selIndex, maxTextSize, minTextSize);
        wv_ad_year.setVisibleItems(visibileItems);
        wv_ad_year.setViewAdapter(mYearAdapter);
        wv_ad_year.setCurrentItem(selIndex);
        wv_ad_year.setCenterDrawable(huitx.libztframework.R.drawable.back_wheel_birthday_draw);
//		wv_ad_year.closeDrawShadow();
        wv_ad_year.setCyclic(false);

        wv_ad_year.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
                selectData = currentText;
                selIndex = arryLists.indexOf(currentText);
                setData();
            }
        });

        wv_ad_year.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);

                setData();
            }
        });


    }

    public void getData() {
        if (onSelUserListener != null){
//            onSelUserListener.onClick(selectData);
            onSelUserListener.onClick(mFamilyDatas.get(selIndex).id + "");
        }
    }

    private void setData() {
        if (onSelUserListener != null){
//            onSelUserListener.onClick(selectData);
            onSelUserListener.onClick(mFamilyDatas.get(selIndex).id + "");
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list,
                                      int currentItem, int maxsize, int minsize) {
            super(context, huitx.libztframework.R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(huitx.libztframework.R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            LinearLayout linear = (LinearLayout) view.findViewById(huitx.libztframework.R.id.linear_bir_item);
            mLayoutUtil.drawViewRBLinearLayout(linear, -1, 70, -1, -1, -1, -1);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }


    public void initList(){
        String FamilyData = PreferencesUtils.getString(context, PreferenceEntity.KEY_CACHE_FAMILY);
        if(!StringUtils.isBlank(FamilyData)){
            Gson gson = new Gson();
            UserEntity mUserEntity;
            try {
                mUserEntity = gson.fromJson(FamilyData, UserEntity.class);
            } catch (Exception e) {
                return;
            }
            if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                mFamilyDatas = mUserEntity.data.list;
                for (UserEntity.Data.FamilyData datas:mUserEntity.data.list) {
                    arryLists.add(datas.name);
                    idLists.add(datas.id + "");
                }
            }
        }
    }


    public void initData() {
        setDate(0);
    }

    /**
     * 设置选中的值
     *
     */
    public void setDate(int index) {
        issetdata = true;
        selectData = arryLists.get(index);
        this.selIndex = index;

        wv_ad_year.setCurrentItem(selIndex);
    }

    /**
     * 设置默认值
     */
    public void setNotifyData(String data) {
        int dataIndex = idLists.indexOf(data);
        if (dataIndex != -1) {
            setDate(dataIndex);
        } else {
            setDate(0);
        }

    }


    @Override
    public void onClick(View v) {

    }

    public void findView() {
        // 初始化布局参数
        mLayoutUtil = LayoutUtil.getInstance();

        linear_account_date_main = findViewById(huitx.libztframework.R.id.linear_account_date_main);
        linear_account_date = findViewById(huitx.libztframework.R.id.linear_account_date);
        linear_account_date_main.setOnClickListener(this);

        wv_ad_year = findViewById(huitx.libztframework.R.id.wv_ad_year);
        wv_ad_month = findViewById(huitx.libztframework.R.id.wv_ad_month);
        wv_ad_day = findViewById(huitx.libztframework.R.id.wv_ad_day);

        wv_ad_month.setVisibility(GONE);
        wv_ad_day.setVisibility(GONE);

        mLayoutUtil.drawViewRBLinearLayout(linear_account_date, 0.0f, 300, 0, 0, 0, 0);
//		mLayoutUtil.drawViewRBLinearLayout(wv_ad_year, -1, 300, -1, -1, -1, -1);
    }

    private View linear_account_date_main;
    private View linear_account_date;
    private WheelView wv_ad_year;
    private WheelView wv_ad_month;
    private WheelView wv_ad_day;

    protected LayoutUtil mLayoutUtil;

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    public void setSelUserListener(OnSelUserListener onSelUserListener) {
        this.onSelUserListener = onSelUserListener;
    }

    public interface OnSelUserListener {
        void onClick(String id);
    }

}