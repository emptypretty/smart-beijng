package com.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.itcast.zhbj.base.BasePager;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
		//
	}

	@Override
	public void initData() {

		// 给空的帧布局动态添加布局对象
		TextView view = new TextView(mActivity);

		view.setTextSize(22);
		view.setTextColor(Color.RED);
		view.setGravity(Gravity.CENTER);
		view.setText("智慧服务");
		flContainer.addView(view);

		tv_title.setText("生活");

	}

}
