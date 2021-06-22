package com.itcast.zhbj.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itcast.zhbj.MainActivity;
import com.itcast.zhbj.base.BaseMenuDetailPager;
import com.itcast.zhbj.base.BasePager;
import com.itcast.zhbj.base.impl.menudetail.InteractMenuDetailPager;
import com.itcast.zhbj.base.impl.menudetail.NewsMenuDetailPager;
import com.itcast.zhbj.base.impl.menudetail.PhotosMenuDetailPager;
import com.itcast.zhbj.base.impl.menudetail.TopicMenuDetailPager;
import com.itcast.zhbj.domain.NewsMenu;
import com.itcast.zhbj.fragment.LeftMenuFragment;
import com.itcast.zhbj.global.GlobalConstants;
import com.itcast.zhbj.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mPagers;

	private NewsMenu mNewsMenu;

	public NewsCenterPager(Activity activity) {
		super(activity);
		//
	}

	@Override
	public void initData() {
		System.out.println("新闻中心初始化了。。。。。");
		/*
		 * // 给空的帧布局动态添加布局对象 TextView view = new TextView(mActivity);
		 * 
		 * view.setTextSize(22); view.setTextColor(Color.RED);
		 * view.setGravity(Gravity.CENTER); view.setText("新闻中心");
		 * flContainer.addView(view);
		 */

		tv_title.setText("新闻");

		String cache = CacheUtils.getCache(mActivity,
				GlobalConstants.CATEGORY_URL);

		if (!TextUtils.isEmpty(cache)) {

			System.out.println("发现缓存了！！！");
			processData(cache);
		}

		// 从服务器获取数据
		getDataFromServer();

	}

	// 服务器的地址
	// 加网络权限
	private void getDataFromServer() {
		// XUtils

		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL,
				new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub

						String result = responseInfo.result;
						System.out.println("服务器分类数据 ：" + result);
						processData(result);

						// 写缓存
						CacheUtils.setCache(mActivity,
								GlobalConstants.CATEGORY_URL, result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
								.show();

					}
				});
	}

	// 解析数据
	protected void processData(String json) {
		// TODO Auto-generated method stub

		Gson gson = new Gson();
		mNewsMenu = gson.fromJson(json, NewsMenu.class);
		System.out.println("解析结果：" + mNewsMenu);

		// 找到侧边栏对象

		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment fragment = mainUI.getLeftMenuFragment();

		fragment.setMenuData(mNewsMenu.data);

		// 网络请求成功之后，初始化四个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				mNewsMenu.data.get(0).children));// 通过构造方法传递数据
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotosMenuDetailPager(mActivity));
		mPagers.add(new InteractMenuDetailPager(mActivity));

		// 设置新闻菜单详情页为默认页面
		setMenuDetailPager(0);
	}

	// 修改菜单详情页
	public void setMenuDetailPager(int position) {
		System.out.println("新闻中心要修改菜单详情页啦:" + position);
		BaseMenuDetailPager pager = mPagers.get(position);

		// 清除之前帧布局显示的内容
		flContainer.removeAllViews();
		// 修改当前帧布局显示的内容
		flContainer.addView(pager.mRootView);
		// 初始化当前页面的数据
		pager.initData();

		// 修改标题
		tv_title.setText(mNewsMenu.data.get(position).title);
	}

}
