package com.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itcast.zhbj.base.BasePager;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
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
		view.setText("首页");
		flContainer.addView(view);

		// 修改标题
		tv_title.setText("智慧北京");
		btnMenu.setVisibility(View.GONE);
	}

}
