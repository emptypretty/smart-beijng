package com.itcast.zhbj.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itcast.zhbj.MainActivity;
import com.itcast.zhbj.R;
import com.itcast.zhbj.base.BasePager;
import com.itcast.zhbj.base.impl.GovAffairsPager;
import com.itcast.zhbj.base.impl.HomePager;
import com.itcast.zhbj.base.impl.NewsCenterPager;
import com.itcast.zhbj.base.impl.SettingPager;
import com.itcast.zhbj.base.impl.SmartServicePager;
import com.itcast.zhbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 主页面fragment
 * 
 * */
public class ContentFragment extends BaseFragment {

	private static final String Tag = "ContentFragment";

	@ViewInject(R.id.vp_content)
	private NoScrollViewPager mViewPager;
	@ViewInject(R.id.rg_group)
	private RadioGroup rGroup;

	private ArrayList<BasePager> mList;// 5个标签页的集合

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view);// 注入view和事件

		// mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		// rGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}

	@Override
	public void initData() {
		// 初始化5个标签页面对象

		mList = new ArrayList<BasePager>();

		mList.add(new HomePager(mActivity));
		mList.add(new NewsCenterPager(mActivity));
		mList.add(new SmartServicePager(mActivity));
		mList.add(new GovAffairsPager(mActivity));
		mList.add(new SettingPager(mActivity));

		mViewPager.setAdapter(new ContentAdapter());

		Log.i(Tag, "mList = " + mList);

		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// 点击各个RadioButton到对应的页面
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_home:
					// 首页
					mViewPager.setCurrentItem(0, false);// 增加false参数是为了去掉页面切换的动画

					break;
				case R.id.rb_news:
					// 新闻中心
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					// 智慧服务
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_gov:
					// 政务
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					// 设置
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}

			}
		});

		// 监听ViewPager页面切换事件，初始化当前页面数据

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

				BasePager pager = mList.get(position);
				pager.initData();

				if (position == 0 || position == mList.size() - 1) {
					// 禁用侧边栏
					setSlidingMenuEnable(false);
				} else {
					// 启用侧边栏
					setSlidingMenuEnable(true);
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});

		// 手动初始化第一个页面的数据
		mList.get(0).initData();

		// 手动禁用第一个页面的侧边栏
		setSlidingMenuEnable(false);

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

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			// 获取当前页面的对象
			BasePager pager = mList.get(position);
			// 布局对象
			// pager.mRooView ：当前页面的根布局
			// 此方法导致每次都提前加载下一页数据，浪费流量和性能，不建议在此处使用
			// pager.initData();// 以子类实现为主
			container.addView(pager.mRootView);

			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
