package com.itcast.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;// 当做Context去使用 就是MainActivity
	public View mRootView;

	// fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mActivity = getActivity(); // 获取fragment所依赖的activity的对象
	}

	// 初始化fragment布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//
		mRootView = initViews();
		return mRootView;
	}

	// fragment所在的activity创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	// 初始化布局 ，必须由子类来实现
	public abstract View initViews();

	// 初始化数据,子类可以不实现
	public void initData() {

	}

}
