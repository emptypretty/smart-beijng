package com.itcast.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// 重写父类onTouchEvent,此外什么都不做，从而达到禁用事件的目的
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// true表示拦截，false不拦截，传给子控件
		return false;// 标签页的ViewPager不拦截新闻菜单详情页页签的ViewPager
	}

}
