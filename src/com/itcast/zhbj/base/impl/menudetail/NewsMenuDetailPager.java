package com.itcast.zhbj.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.itcast.zhbj.MainActivity;
import com.itcast.zhbj.R;
import com.itcast.zhbj.base.BaseMenuDetailPager;
import com.itcast.zhbj.base.impl.TabDetailPager;
import com.itcast.zhbj.domain.NewsMenu.NewsTabeData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

/*ViewPagerIndicator:
 * 
 * 1.引入ViewPagerIndicator库
 * 2.解决V4冲突，用大的版本覆盖小的版本，注意关联源码
 * 3.仿照sample中的程序进行拷贝SampleTableDefault
 * 4.拷贝布局文件和相关代码  mIndicator.setViewPager(mViewPager); 关联mViewPager
 * 5.重写PagerAdapter的getPageTitle返回指示器标题
 * */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	private ArrayList<NewsTabeData> children;

	private ArrayList<TabDetailPager> mPagers;

	public NewsMenuDetailPager(Activity activity,
			ArrayList<NewsTabeData> children) {
		super(activity);
		this.children = children;
	}

	@Override
	public View initViews() {
		/*
		 * TextView view = new TextView(mActivity); view.setTextSize(22);
		 * view.setTextColor(Color.RED); view.setGravity(Gravity.CENTER);// 居中显示
		 * view.setText("菜单详情页-新闻");
		 */

		View view = View.inflate(mActivity, R.layout.pagers_news_menu_detail,
				null);
		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void initData() {
		// 初始化12个页签对象
		// 以服务器为准
		mPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < children.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,
					children.get(i));
			mPagers.add(pager);
		}

		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);// 把mViewPager和mIndicator关联在一起

		// 设置页面触摸监听,当viewpager和indicator关联在一起，事件需要设置给indicator
		// mViewPager.setOnPageChangeListener(this);
		mIndicator.setOnPageChangeListener(this);
	}

	class NewsMenuDetailAdapter extends PagerAdapter {

		// 返回指示器Indicator的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return children.get(position).title;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);

			pager.initData();

			container.addView(pager.mRootView);

			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	// 通过注解的方式绑定事件，
	@OnClick(R.id.btn_next)
	public void nextPage(View view) {
		// 跳到下一个页面
		int currentPos = mViewPager.getCurrentItem();
		// mIndicator.setCurrentItem(++currentPos);
		mViewPager.setCurrentItem(++currentPos);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		System.out.println("当前页面：" + position);

		if (position == 0) {
			// 启用侧边栏
			setSlidingMenuEnable(true);
		} else {
			// 禁用侧边栏
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	// 开启、禁用侧边栏
	private void setSlidingMenuEnable(boolean enable) {
		// 获取SlidingMenu的对象
		// 获取MainActivity对象

		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

	}

}
