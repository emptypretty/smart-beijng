package com.itcast.zhbj;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.itcast.zhbj.utils.PrefUtils;

/**
 * 新手引导页
 * 
 * @author Administrator
 * 
 */
public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	private LinearLayout ll_container;
	private ArrayList<ImageView> mImageViews;
	private ImageView ivRedPoint;
	private int[] mImageIds = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	protected int mPointDis;

	private Button mBtnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		initViews();
		initData();
	}

	// 初始化数据
	private void initData() {
		// 初始化三张图片的ImageView
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView view = new ImageView(this);
			view.setBackgroundResource(mImageIds[i]);
			mImageViews.add(view);

			// 初始化小圆点
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.shape_point_normal);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (i > 0) {
				params.leftMargin = 10;
			}

			point.setLayoutParams(params);// 设置布局参数
			ll_container.addView(point);

		}

		mViewPager.setAdapter(new GuideAdapter());

		// 监听ViewPager滑动事件，更新小红点位置

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				//
				if (position == mImageIds.length - 1) {
					mBtnStart.setVisibility(View.VISIBLE);
				} else {
					mBtnStart.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//
				System.out.println("当前位置: " + position + ";偏移百分比:"
						+ positionOffset);
				// 通过修改小红点的左边距来更新小点的位置
				int leftMargin = (int) (mPointDis * positionOffset + position
						* mPointDis + 0.5f);// 要将当前的位置信息产生的距离加进来

				// 获取小红点的布局参数

				RelativeLayout.LayoutParams params = (LayoutParams) ivRedPoint
						.getLayoutParams();
				params.leftMargin = leftMargin;
				ivRedPoint.setLayoutParams(params);

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				//

			}
		});

		// 计算圆点移动距离
		/*
		 * int mPointDis = ll_container.getChildAt(1).getLeft() -
		 * ll_container.getChildAt(0).getLeft();
		 */
		// measure->layout->draw,必须oncreate执行结束之后，才会开始绘制布局，走此上述三个方法

		// 视图树
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// 一旦视图树的layout方法调用完成，就会回调此方法
					@Override
					public void onGlobalLayout() {
						// 布局位置已经确定，可以拿到位置信息了

						mPointDis = ll_container.getChildAt(1).getLeft()
								- ll_container.getChildAt(0).getLeft();

						// 移除观察者
						ivRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

					}
				});

		mBtnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 在sp中记录访问过引导页的状态
				PrefUtils.putBoolean(getApplicationContext(), "is_guide_show",
						true);

				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));

				finish();

			}
		});

	}

	/**
	 * 初始化布局
	 */
	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		mBtnStart = (Button) findViewById(R.id.btn_start);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageIds.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 初始化布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView view = mImageViews.get(position);
			container.addView(view);
			return view;
		}

		// 销毁布局
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
