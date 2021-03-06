package huitx.libztframework.view.dialog.listener;

import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import huitx.libztframework.view.dialog.bean.PopuBean;


/**
 * 动态设置下拉框的数据
 */
public interface TdataListener {
    /**
     * 初始化数据
     */
    void initPupoData(List<PopuBean> lists);

    /**
     * 点击事件
     */
    void onItemClick(AdapterView<?> adapterView, View view, int position);
}