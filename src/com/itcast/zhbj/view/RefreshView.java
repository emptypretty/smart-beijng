package com.itcast.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.itcast.zhbj.R;

public class RefreshView extends ListView {

	private View mHeaderView;
	private int mHeaderViewHeight;

	public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();

	}

	public RefreshView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);

	}

	public RefreshView(Context context) {
		this(context, null);

	}

	// 初始化头布局
	private void initHeaderView() {
		mHeaderView = View
				.inflate(getContext(), R.layout.pull_to_refresh, null);
		addHeaderView(mHeaderView);// 给ListView添加头布局

		// 隐藏头布局
		// 获取当前头布局的高度，然后设置负paddingTop，布局就会向上走
		// int height = mHeaderView.getHeight();
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

	}

}
