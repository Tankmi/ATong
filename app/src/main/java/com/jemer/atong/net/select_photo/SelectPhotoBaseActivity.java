package com.jemer.atong.net.select_photo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.util.ProviderUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NetUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.view.swiperecyclerview.SpacesItemDecoration;
import huitx.libztframework.view.swiperecyclerview.SwipeRecyclerView;

public class SelectPhotoBaseActivity extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener
		, SwipeRecyclerView.OnSwipeRecyclerViewListener {

	public UserEntity mUserEntity;
	public static final int Intent_Photo_100 = 100;

	public SelectPhotoBaseActivity(int layoutId) {
		super(layoutId);
	}

	@Override
	protected void initHead() {
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void initContent() {
		findView();
	}


//	@Override
//	public void paddingDatas(String mData, int type) {
//		setLoading(false,"");
//		Gson mGson = new Gson();
//		try{
//			mUserEntity = mGson.fromJson(mData, UserEntity.class);
//		}catch(Exception e){
//			return;
//		}
//		if(mUserEntity.code == ContextConstant.RESPONSECODE_200){
////			if(type == GETUSERINFO){	//获取用户信息
////				Message message = Message.obtain();
////				message.what = type;
////				message.obj = mUserEntity.list;
////				mHandler.sendMessage(message);
////			}
//		} else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
//			reLoading();
//		} else {
//			ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
//		}
//	}
//
//	@Override
//	public void error(String msg, int type) {
//		setLoading(false,"");
//		if (!NetUtils.isAPNType(mContext)) {	//没网
//
//		}else{
////			ToastUtils.showToast("操作失败，请稍候重试！");
//
//		}
//	}

	protected MyHandler mHandler;

	protected class MyHandler extends Handler {
		protected static final int INITPHOTOSUCCESS = 2001;

		// SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
		private final WeakReference<Activity> mActivity;

		protected MyHandler(Activity activity) {
			mActivity = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Activity activity = mActivity.get();
			if (activity != null){
				//做操作
				switch (msg.what) {
					case INITPHOTOSUCCESS:	//初始化图片信息
						setLoading(false);
						initRecycler();
						break;
				}
			}
			super.handleMessage(msg);
		}
	}

	@Override
	protected void initLocation() {

	}
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void findView(){
		setRightButtonText("完成", R.color.main_color);
		mBtnRight.setOnClickListener(this);
		mBtnRight.setVisibility(View.GONE);
		mRecyclerView =  findViewByIds(R.id.srv_select_photo);

//		initPhoto();
//		initRecycler();
		setLoading(true);
		mHandler.post(initPhoto);
	}


//	protected Activity mActivity = null;
	private SwipeRecyclerView mRecyclerView;
	private RecyclerView recycManager;
	protected SpacesItemDecoration decor;
	private RecycleViewSelectPhotoAdapter mAdapter;
	private LinkedList<PhotoEntity> photoInfoList = new LinkedList<>();
	private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

//	protected ArrayList<String> resultList = new ArrayList<>();//返回首页的集合
//	protected File createCreamePath;

	/* 拍照的照片存储位置 */
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + PreferenceEntity.KEY_CACHE_PATH);
	String fileName = "";
	protected File mCurrentPhotoFile;// 照相机拍照得到的图片
	protected String selectedImagePath = "";
	/** 用来标识请求照相功能的activity */
	protected static int CAMERA_WITH_DATA = 1001;

	private void initRecycler() {

		mRecyclerView.setOnSwipeRecyclerViewListener(this);
		mRecyclerView.isCancelLoadNext(true);
		mRecyclerView.isCancelRefresh(true);

		GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);

		recycManager = mRecyclerView.getRecyclerView();
		recycManager.setLayoutManager(gridLayoutManager);

		int width = mLayoutUtil.getWidgetWidth(4.5f, true);
		decor = new SpacesItemDecoration(width, width);
		recycManager.addItemDecoration(decor);

		mAdapter = new RecycleViewSelectPhotoAdapter(mContext, photoInfoList,1, true);
		recycManager.setAdapter(mAdapter);

		mAdapter.getPhoto(new RecycleViewSelectPhotoAdapter.getPhoto() {
			@Override
			public void getTakeCreame(View v) {
//				Intent intentPhone = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				createCreamePath = createTmpFile(mContext, "/Edreamoon/Pictures");
//				intentPhone.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createCreamePath));
//				intentPhone.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//				startActivityForResult(intentPhone, CAMERA_WITH_DATA);

				String status = Environment.getExternalStorageState();
				if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
					ToastUtils.showToast("SD卡状态异常，暂时无法拍照！");
					return;
				}

				if (!PHOTO_DIR.exists()) {
					boolean iscreat = PHOTO_DIR.mkdirs();// 创建照片的存储目录
					LOG("创建存放头像文件夹 = " + iscreat);
				}
				fileName = System.currentTimeMillis() + ".jpg";
				mCurrentPhotoFile = new File(PHOTO_DIR, fileName);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
				Uri uri;
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
					uri = Uri.fromFile(mCurrentPhotoFile);

				}else{
					/**
					 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
					 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
					 */
					uri = FileProvider.getUriForFile(SelectPhotoBaseActivity.this,
							ProviderUtil.getFileProviderName(), mCurrentPhotoFile);
				}
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, CAMERA_WITH_DATA);
			}
		});

		mAdapter.getSelectPhoto(new RecycleViewSelectPhotoAdapter.getSelectPhoto() {
			@Override
			public void getListPhoto(List<String> photoPath) {
//				if (size > 0) {
//					textEnter.setVisibility(View.VISIBLE);
//				} else {
//					textEnter.setVisibility(View.GONE);
//				}
				mBtnRight.setVisibility(View.VISIBLE);
				selectedImagePath = photoPath.get(0);
				LOG("回调过来的图片地址：" + selectedImagePath);
				if (TextUtils.isEmpty(selectedImagePath)) {		//没有获取到图片
					LOG("没有获取到图片" );
					return;
				}
			}
		});
	}

	private void initPhoto(){
		loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>(){
			private final String[] IMAGE_PROJECT = {
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.DISPLAY_NAME,
					MediaStore.Images.Media.DATE_ADDED,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.SIZE,
			};

			@Override
			public Loader<Cursor> onCreateLoader(int id, Bundle args)
			{
				if (id == 0) {
					return new CursorLoader(mContext,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECT,
							null, null, IMAGE_PROJECT[2] + " DESC");

				} else if (id == 1) {
					return new CursorLoader(mContext,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							IMAGE_PROJECT,
							IMAGE_PROJECT[0] + "like '%" + args.getString("path") + "%'",
							null, IMAGE_PROJECT[2] + " DESC");
				}
				return null;
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor data)
			{
				LOG("onLoadFinished:" + (data ==null?0:data.getCount()));
				if (data != null) {
					photoInfoList.clear();
					int count = data.getCount();
					data.moveToFirst();
					if (count > 0) {
						do {
							String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECT[0]));
							String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECT[1]));
							long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECT[2]));
							int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECT[4]));
							PhotoEntity photoInfo = new PhotoEntity(path, name, time, false);
							Boolean isSize5k = size > 1024 * 5;
							if (isSize5k) {
								photoInfoList.add(photoInfo);
							}
						} while (data.moveToNext());
					}
				}
				mHandler.sendEmptyMessage(mHandler.INITPHOTOSUCCESS);
				LOG("onLoadFinished: photoInfoList" + photoInfoList.size());
			}

			@Override
			public void onLoaderReset(Loader<Cursor> loader)
			{

			}
		};
		getSupportLoaderManager().restartLoader(0, null, loaderCallbacks);
	}

	Runnable initPhoto = new Runnable() {
		@Override
		public void run()
		{
			initPhoto();
		}
	};

	@Override
	protected void initLogic() { }
	
	@Override
	protected void destroyClose() { }

	@Override
	protected void pauseClose() { }

	@Override
	public void onClick(View arg0) { }

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) { }

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) { }


	@Override
	public void onRefresh()
	{

	}

	@Override
	public void onLoadNext() {
	}
}
