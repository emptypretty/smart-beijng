package com.itcast.zhbj.fragment;

import android.view.View;

import com.itcast.zhbj.R;

/**
 * 侧边栏fragment
 * 
 * */
public class LeftMenuFragment extends BaseFragment {

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		return view;
	}
}
