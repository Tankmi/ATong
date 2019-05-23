package com.jemer.atong.fragment.personal_center.family;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.view.dialog.affirm.AffirmBean;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.personal_center.family.net.FamilyPresenter;
import com.jemer.atong.fragment.personal_center.family.net.FamilyView;
import com.jemer.atong.net.ClearDisposable;
import com.jemer.atong.view.dialog.affirm.AffirmDialogFragment;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.view.swiperecyclerview.SwipeRecyclerView;

//调用方式
//    SexDialogFragment playQueueFragment;
//    private FragmentManager fragmentManager;
//    private String MOVEMENT_TIME_TAG = "sharedialog";
//    /**
//     * 显示分享框
//     */
//    private void ShowSexDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class FamilyDialogFragment extends BaseDialogFragment implements FamilyView<UserEntity.Data>, SwipeRecyclerView.OnSwipeRecyclerViewListener, FamilyAdapter.OnFamilyRemoveListener {

    @BindView(R.id.bt_eyert_left)
    Button bt_eyert_left;
    @BindView(R.id.tv_eyert_title)
    TextView tv_eyert_title;

    @BindView(R.id.srv_familt_add)
    SwipeRecyclerView mSwipeRecyclerView;
    @BindView(R.id.btn_family_add)
    Button mbtnAdd;


    private String mSex;    //1男,2女
    private String myear, mmonth, mday;

    protected FamilyPresenter mPresenter;
    private boolean isGetNetData = false;

    public FamilyDialogFragment() {
        super(R.layout.fragment_family_list);
        TAG = getClass().getSimpleName();
    }


    @OnClick({R.id.bt_eyert_left, R.id.btn_family_add})
    void inputInfo(View view) {
        switch (view.getId()) {
            case R.id.bt_eyert_left:
                dismiss();
                break;
            case R.id.btn_family_add:   //提交
                LOG("添加用户");
                ShowAddDialog();
                break;
        }
    }

    @Override
    protected void initHead() {
        tv_eyert_title.setText("用户列表");
        if (mPresenter == null) {
            mPresenter = new FamilyPresenter();
        }
        mPresenter.attachView(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(677));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        //去除阴影
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        getDialog().getWindow().setAttributes(layoutParams);

    }


    @Override
    protected void initLogic() {
    }


    @Override
    public void onResume() {
        super.onResume();
        LOG("onResume");
    }

    protected FamilyAdapter mAdapter;

    @Override
    protected void initContent() {
        mSwipeRecyclerView.setOnSwipeRecyclerViewListener(this);
        mSwipeRecyclerView.isCancelRefresh(true);
        mSwipeRecyclerView.isCancelLoadNext(true);

        mAdapter = new FamilyAdapter(mContext);
        mAdapter.setOnItemClickListener(this);

        RecyclerView mRecyclerView = mSwipeRecyclerView.getRecyclerView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getUserInfo();
    }

    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewDefaultLayout(bt_eyert_left, 170, 86, -1, -1, -1, -1);
    }

    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {
        if (mPresenter != null) {
            mPresenter.detachView();
            ClearDisposable.getInstance().getCompositeDisposable().clear();
        }
    }

    FamilyAddDialogFragment addDialogFragment;
    private FragmentManager fragmentManager;
    private String DIALOG_SEX_TAG = "sexdialog";
    private String DIALOG_AFFIRM_TAG = "affirmDialog";

    protected void ShowAddDialog() {
        if (addDialogFragment == null) {
            addDialogFragment = new FamilyAddDialogFragment();
            addDialogFragment.setOnAddFamilyListener(state -> {
                LOG("添加用户：" + state);
                if (state) mPresenter.getUserInfo();
            });
        }
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();

        addDialogFragment.show(fragmentManager, DIALOG_SEX_TAG);

    }

    protected void ShowAffirmDialog(int id, int position) {
        AffirmBean bean = new AffirmBean();
        bean.setTitle("");
        bean.setContent("确定删除吗？");
        AffirmDialogFragment affirmDialogFragment = AffirmDialogFragment.getInstance(bean);
        affirmDialogFragment.setOnAffirmListener(state -> {
            LOG("确认弹框：" + state);
            if (state) mPresenter.delFamily(id, position);
        });
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();

        affirmDialogFragment.show(fragmentManager, DIALOG_AFFIRM_TAG);

    }


    @Override
    public void onRemoveListener(int id, int position) {

        ShowAffirmDialog(id, position);

    }

    @Override
    public void getUserInfoSuccess(UserEntity.Data data) {
        if (data.list != null && data.list.size() > 0) {
            mAdapter.setListData(data.list);
        } else {
            ToastUtils.showToast("暂无其他用户");
        }

    }

    @Override
    public void addUserInfoState(boolean state, String str) {

    }

    @Override
    public void delUserInfoState(boolean state, int position) {
        if (state) {
            mPresenter.getUserInfo();
            mAdapter.onItemDismiss(position);
        }
    }

    @Override
    public void loadingShow() {
        isGetNetData = true;
        setLoading(true);
    }

    @Override
    public void loadingDissmis() {
        isGetNetData = false;
        setLoading(false);
    }

    @Override
    public void loginOut() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadNext() {

    }

}
