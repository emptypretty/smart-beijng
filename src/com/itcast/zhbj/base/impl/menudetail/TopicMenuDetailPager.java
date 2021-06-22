package com.itcast.zhbj.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itcast.zhbj.base.BaseMenuDetailPager;

public class TopicMenuDetailPager extends BaseMenuDetailPager {

	public TopicMenuDetailPager(Activity activity) {
		super(activity);

	}

	@Override
	public View initViews() {
		TextView view = new TextView(mActivity);
		view.setTextSize(22);
		view.setTextColor(Color.RED);
		view.setGravity(Gravity.CENTER);// 居中显示
		view.setText("菜单详情页-专题");

		return view;
	}

}
