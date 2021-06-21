package com.itcast.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itcast.zhbj.R;

/**
 * 5个标签页的基类
 * 
 * 共性：子类都有标题栏，所以可以直接在父类中加载布局页面
 * 
 * @author Administrator
 * 
 */
public class BasePager {

	public Activity mActivity;
	public TextView tv_title;
	public ImageButton btnMenu;
	public FrameLayout flContainer;// 空的帧布局，由子类动态填充布局

	// 当前页面的根布局
	public View mRootView;

	public BasePager(Activity activity) {
		mActivity = activity;

		// 在页面对象创建时就初始化了布局
		mRootView = initViews();

	}

	// 初始化布局
	public View initViews() {
		// TODO Auto-generated method stub

		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContainer = (FrameLayout) view.findViewById(R.id.fl_container);

		return view;
	}

	// 初始化数据
	public void initData() {
		// TODO Auto-generated method stub

	}

}
