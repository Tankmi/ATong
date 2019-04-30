/**
 * FileName:FragmentSwitchTool.java
 * Copyright YaoDiWei All Rights Reserved.
 */
package huitx.libztframework.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * 首页fragment 分发器
 * @author ZhuTao
 * @date 2017/5/9 
 * @params 
*/

public class FragmentSwitchTool implements OnClickListener {

	private FragmentManager fragmentManager;
	private Fragment currentFragment;
	private View[] currentSelectedView;
	/** 导航栏中的item父布局 */
	private View[] clickableViews; //传入用于被点击的view,比如是一个LinearLayout
	/** 导航栏中的item的子控件集合，用以改变控件UI时调用 */
	private List<View[]> selectedViews; //传入用于被更改资源selected状态的view[], 比如一组View[]{TextView, ImageView} 
	/** item对应的fragment */
	private Class<? extends Fragment>[] fragmentlists;
	private Bundle[] bundles;
	private int containerIds;
	/** 是否用动画过渡 */
	private boolean showAnimator;

	public FragmentSwitchTool(FragmentManager fragmentManager, int containerId) {
		super();
		this.fragmentManager = fragmentManager;
		this.containerIds = containerId;
	}

	public void setClickableViews(View... clickableViews) {
		this.clickableViews = clickableViews;
		for (View view : clickableViews) {
			view.setOnClickListener(this);
		}
	}
	
	public void setSelectedViews(List<View[]> selectedViews) {
		this.selectedViews = selectedViews;
	}
	
	public FragmentSwitchTool addSelectedViews(View... views){
		if (selectedViews == null) {
			selectedViews = new ArrayList<View[]>();
		}
		selectedViews.add(views);
		return this;
	}

	public void setFragments(Class<? extends Fragment>... fragments) {
		this.fragmentlists = fragments;
	}

	public void setBundles(Bundle... bundles) {
		this.bundles = bundles;
	}

	public void changeTag(View v) {
		if(mItemClickListener!=null)mItemClickListener.onFSTItemClick(v);

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = fragmentManager.findFragmentByTag(String.valueOf(v.getId()));
		for (int i = 0; i < clickableViews.length; i++) {
			if (v.getId() == clickableViews[i].getId()) {
				
				if (showAnimator) {
					int exitIndex = selectedViews.indexOf(currentSelectedView);
//					Log.e("yao", "enter : " + i + "   exit: " + exitIndex);
//					if (i > exitIndex){
//						fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);
//					} else if (i < exitIndex) {
//						fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
//					}
				}
				
				if (fragment == null) {
					if (currentFragment != null) {
						fragmentTransaction.hide(currentFragment);
						for (View view : currentSelectedView) {
							view.setSelected(false);
						}
					}
					try {
						fragment = fragmentlists[i].newInstance();

						if (bundles != null && bundles[i] != null) {
							fragment.setArguments(bundles[i]);
						}

					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					fragmentTransaction.add(containerIds, fragment, String.valueOf(clickableViews[i].getId()));
				} else if (fragment == currentFragment) {
				} else {
					fragmentTransaction.hide(currentFragment);
					for (View view : currentSelectedView) {
						view.setSelected(false);
					}
					fragmentTransaction.show(fragment);
				}
				
				fragmentTransaction.commit();
				currentFragment = fragment;
				for (View view : selectedViews.get(i)) {
					view.setSelected(true);
				}
				currentSelectedView = selectedViews.get(i);
				break;
			}
		}
	}
	
	@Override
    public void onClick(View v)
    {
       changeTag(v);
    }

    private onFSTItemClickListener mItemClickListener;

    public void setOnFSTItemClickListener(onFSTItemClickListener mItemClickListener){
		this.mItemClickListener = mItemClickListener;
	}

    public interface onFSTItemClickListener{
		void onFSTItemClick(View v);
	}
}
