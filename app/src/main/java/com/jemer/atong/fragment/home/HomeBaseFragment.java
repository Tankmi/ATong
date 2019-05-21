package com.jemer.atong.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.home.BannerEntity;
import com.jemer.atong.entity.home.HomeEntity;
import com.jemer.atong.fragment.home.net.HomePresenter;
import com.jemer.atong.fragment.home.net.HomeView;
import com.jemer.atong.view.banner.BannerViewHolder;
import com.jemer.atong.view.banner.BannerViewPager;
import com.jemer.atong.view.guide.GuideViewPager;
import com.jemer.atong.view.guide.interf.GuideViewHolder;
import com.jemer.atong.web.activity.WebViewActivity;


import java.lang.ref.WeakReference;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.view.swiperecyclerview.SwipeRecyclerView;

@SuppressLint("ValidFragment")
public class HomeBaseFragment extends BaseFragment implements
        SwipeRecyclerView.OnSwipeRecyclerViewListener, HomeView<HomeEntity.Data.HomeData, BannerEntity.Data.BannerData> {

    protected MyHandler mHandler;
    protected HomePresenter mPresenter;
    protected String mSearchText = "";

    private int current = 1,rowSize = 8;

    protected HomeDataAdapter mAdapter;

//    private String[] images = {
//            "https://images.unsplash.com/photo-1556909114-a1d34f47412c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
//            "https://images.unsplash.com/photo-1557502706-5a0e03129173?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
//            "https://images.unsplash.com/photo-1557505983-1649ce0ba3b7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
//    };

    public HomeBaseFragment(int layoutId) {
        super(layoutId);
    }


    @Override
    protected void initHead() {

    }

    @Override
    protected void initContent() {
        banner_home.setViewHolder(new BannerViewHolder(true));
//        banner_home.addIndicator(banner_dot_home);
//        banner_home.setPageMargin(dp2px(40));
//        banner_home.setPageTransformer(true, new StackTransformer());
//        banner_home.setData(Arrays.asList(images));

        mSwipeRecyclerView.setOnSwipeRecyclerViewListener(this);
        mSwipeRecyclerView.isCancelRefresh(false);
        mSwipeRecyclerView.isCancelLoadNext(true);

        mAdapter = new HomeDataAdapter(getActivity());

        RecyclerView mRecyclerView = mSwipeRecyclerView.getRecyclerView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);


        String homeLists = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_HOME);
        if(!StringUtils.isBlank(homeLists)){
            Gson gson = new Gson();
            HomeEntity mEntity = null;
            try {
                mEntity = gson.fromJson(homeLists, HomeEntity.class);
                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    mAdapter.setListData(mEntity.data.list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        String bannerLists = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_BANNER);
        if(!StringUtils.isBlank(bannerLists)){
            Gson gson = new Gson();
            BannerEntity mEntity = null;
            try {
                mEntity = gson.fromJson(bannerLists, BannerEntity.class);
                if (mEntity.code == ContextConstant.RESPONSECODE_200) {
                    banner_home.setData(mEntity.data.list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    protected void initLogic() {

    }



    @Override
    protected void onVisibile() {

    }


    @Override
    public void getBannerData(boolean state, List<BannerEntity.Data.BannerData> data) {
        if(state && data!=null && data.size()>0){
            rl_banner_home.setVisibility(View.VISIBLE);
            banner_home.setData(data);
        }else {
            rl_banner_home.setVisibility(View.GONE);
        }
    }

    @Override
    public void getListDataSuccess(int state, List<HomeEntity.Data.HomeData> datas) {

        mSwipeRecyclerView.onLoadFinish();
        if(datas == null || datas.size() == 0){
            if(state == 2)  mSwipeRecyclerView.isCancelLoadNext(true);
            else if(state ==1 && !StringUtils.isBlank(mSearchText)) mSearchText = "";
            return;
        }
        LOG("获取数据： state  " + state + "list.size(): " + datas.size());
        if (datas != null && datas.size() >= rowSize) mSwipeRecyclerView.isCancelLoadNext(false);
        else mSwipeRecyclerView.isCancelLoadNext(true);

        if (state == 1) {  mAdapter.setListData(datas);
        } else mAdapter.setLoadNextData(datas);
    }

    @Override
    public void getListDataFailed(int state, String parameter) {
//        if(!StringUtils.isBlank(parameter)){
//            ToastUtils.showToast("暂无跟 “" + parameter + "”相关的数据");
//        }else{
//            ToastUtils.showToast("暂无数据");
//        }
    }


    @Override
    public void loadingShow() {
        setLoading(true, "");
    }

    @Override
    public void loadingDissmis() {
        setLoading(false, "");
    }

    @Override
    public void loginOut() {
        reLoading();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onRefresh() {
        LOG("onRefresh");
        mPresenter.getListData(mSearchText,1);
    }

    @Override
    public void onLoadNext() {
        LOG("onLoadNext");
        mPresenter.getListData(mSearchText,2);
    }

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Context> mActivity;

        protected MyHandler(Context activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 0: // 获取版本号

                    break;
            }
        }

    }



    @BindView(R.id.ll_home_search)
    protected LinearLayout ll_home_search;
    @BindView(R.id.et_home_search)
    protected EditText et_home_search;
    @BindView(R.id.tv_home_search)
    protected TextView tv_home_search;

    @BindView(R.id.rl_banner_home)
    protected RelativeLayout rl_banner_home;
    @BindView(R.id.iv_banner_bg)
    protected ImageView iv_banner_bg;
    @BindView(R.id.banner_home)
    protected BannerViewPager banner_home;
//    protected BannerViewPager banner_home;
//    @BindView(R.id.banner_dot_home)
//    protected DotIndicator banner_dot_home;

    @BindView(R.id.srv_home)
    protected SwipeRecyclerView mSwipeRecyclerView;


    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLinearLayout(ll_home_search, -1, -1, 0, 0, PreferenceEntity.ScreenTop, -1);
        mLayoutUtil.drawViewRBLayout(iv_banner_bg, -1, 234, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(banner_home, 0, 308, 33, 33, -1, -1);
    }



//    public class BannerViewHolder implements GuideViewHolder<BannerEntity.Data.BannerData> {
    public class BannerViewHolder implements com.jemer.atong.view.banner.BannerViewHolder<BannerEntity.Data.BannerData> {
        private boolean isRoundedCorner = true;

        public BannerViewHolder() {

        }

        public BannerViewHolder(boolean roundedCorner) {
            isRoundedCorner = roundedCorner;
        }

        @Override
        public View getView(Context context, final int position, BannerEntity.Data.BannerData data) {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.item_banner_home, null);
            ImageView imageView = inflate.findViewById(R.id.image);
            if (isRoundedCorner) {
                Glide.with(imageView).load(data.image).transforms(new CenterCrop(), new RoundedCorners(dp2px(5))).disallowHardwareConfig().into(imageView);
            } else {
                Glide.with(imageView).load(data.image).transforms(new CenterCrop()).disallowHardwareConfig().into(imageView);
            }

            imageView.setOnClickListener(view ->{
                Intent intent_home = new Intent(mContext, WebViewActivity.class);
                intent_home.putExtra("url", data.url);
                intent_home.putExtra("title_name", data.title);
                intent_home.putExtra("is_refresh", false);
                mContext.startActivity(intent_home);
            });
            return inflate;
        }
    }

    public int dp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getResources().getDisplayMetrics());
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {

    }

}
