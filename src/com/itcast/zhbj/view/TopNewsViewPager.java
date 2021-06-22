package com.itcast.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// 上下滑动，需要拦截
	// 左滑时，最后一个页面需要拦截
	// 右滑时，第一个页面需要拦截
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		getParent().requestDisallowInterceptTouchEvent(true);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 按下
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();

			int dx = endX - startX;
			int dy = endY - startY;

			if (Math.abs(dx) > Math.abs(dy)) {
				// 左右滑动
				int currentItem = getCurrentItem();
				if (dx > 0) {
					if (currentItem == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 左滑
					int count = getAdapter().getCount();
					if (currentItem == count - 1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
