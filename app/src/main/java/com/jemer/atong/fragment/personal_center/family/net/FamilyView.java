package com.jemer.atong.fragment.personal_center.family.net;

import com.jemer.atong.net.base.BaseView;
import com.jemer.atong.net.base.BaseViewNormal;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:05
 * 描述：动态 mvp
 */
public interface FamilyView<T> extends BaseViewNormal {

    void getUserInfoSuccess(T data);
    void addUserInfoState(boolean state,String str);
    void delUserInfoState(boolean state,int str);


}
