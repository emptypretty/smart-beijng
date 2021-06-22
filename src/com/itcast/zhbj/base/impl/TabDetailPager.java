package com.itcast.zhbj.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itcast.zhbj.R;
import com.itcast.zhbj.base.BaseMenuDetailPager;
import com.itcast.zhbj.domain.NewsMenu.NewsTabeData;
import com.itcast.zhbj.domain.NewsTab;
import com.itcast.zhbj.domain.NewsTab.News;
import com.itcast.zhbj.domain.NewsTab.TopNews;
import com.itcast.zhbj.global.GlobalConstants;
import com.itcast.zhbj.utils.CacheUtils;
import com.itcast.zhbj.view.RefreshView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/*
 * 页签详情页
 * 继承BaseMenuDetailPager，从代码角度来讲比较简洁
 * 但当前页不属于菜单详情页，这是干爹
 *
 * ViewPagerIndicator:
 * 1.引入ViewPagerIndicator库
 * 2.解决V4冲突，用大的版本覆盖小的版本，注意关联源码
 * */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabeData newsTabeData;// 当前页签的网络数据
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.vp_tab_detail)
	private ViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;

	@ViewInject(R.id.lv_list)
	private RefreshView lvList;

	private String mUrl;
	private ArrayList<TopNews> mTopNewsList;
	private ArrayList<News> mNewsList;

	public TabDetailPager(Activity activity, NewsTabeData newsTabeData) {
		super(activity);
		this.newsTabeData = newsTabeData;

		mUrl = GlobalConstants.SERVER_URL + newsTabeData.url;
	}

	@Override
	public View initViews() {
		/*
		 * view = new TextView(mActivity); view.setTextSize(22);
		 * view.setTextColor(Color.RED); view.setGravity(Gravity.CENTER);// 居中显示
		 * view.setText("页签");
		 */

		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		// 加载新闻头布局
		View headView = View
				.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headView);

		lvList.addHeaderView(headView);

		return view;
	}

	@Override
	public void initData() {
		// view.setText(newsTabeData.title);

		String cache = CacheUtils.getCache(mActivity, mUrl);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getDataFromServer();
	}

	// 请求服务器解析数据
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String result = responseInfo.result;
				processData(result);

				CacheUtils.setCache(mActivity, mUrl, result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	// 解析器
	protected void processData(String result) {
		Gson gson = new Gson();
		NewsTab newsTab = gson.fromJson(result, NewsTab.class);

		mTopNewsList = newsTab.data.topnews;
		if (mTopNewsList != null) {
			mViewPager.setAdapter(new TopNewsAdapter());

			mIndicator.setViewPager(mViewPager);// 将圆形指示器和viewpager绑定
			mIndicator.setSnap(true);// 快照展示
			mIndicator.onPageSelected(0);
			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					tvTitle.setText(mTopNewsList.get(position).title);

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

			// 初始化第一页头条新闻标题
			tvTitle.setText(mTopNewsList.get(0).title);
		}
		System.out.println("页签结果：" + newsTab);

		// 初始化新闻列表数据
		mNewsList = newsTab.data.news;
		if (mNewsList != null) {
			lvList.setAdapter(new NewsAdapter());
		}

	}

	// 新闻头条适配器
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {

			return mTopNewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);

			TopNews topNews = mTopNewsList.get(position);
			String topimage = topNews.topimage;// 图片的下载链接

			view.setScaleType(ScaleType.FIT_XY); // 设置缩放模式，图片宽高匹配窗体
			// 1.根据url下载图片；2.将图片设置给ImageView 3.图片缓存4.避免内存溢出
			// BitmapUtils:xUtils
			mBitmapUtils.display(view, topimage);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	// 新闻列表适配器
	class NewsAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);
				holder = new ViewHolder();

				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			News info = (News) getItem(position);

			holder.tvTitle.setText(info.title);
			holder.tvTime.setText(info.pubdate);

			mBitmapUtils.display(holder.ivIcon, info.listimage);
			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvTime;
	}
}
