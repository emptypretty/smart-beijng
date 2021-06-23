package com.itcast.zhbj.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itcast.zhbj.R;
import com.itcast.zhbj.base.BaseMenuDetailPager;
import com.itcast.zhbj.domain.photosBean;
import com.itcast.zhbj.domain.photosBean.PhotoNews;
import com.itcast.zhbj.global.GlobalConstants;
import com.itcast.zhbj.utils.CacheUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements
		OnClickListener {

	@ViewInject(R.id.lv_list)
	private ListView lvList;
	@ViewInject(R.id.gv_list)
	private GridView gvList;
	private ArrayList<PhotoNews> mPhotoList;

	private ImageButton btnDisplay;

	public PhotosMenuDetailPager(Activity activity, ImageButton btnDisplay) {
		super(activity);
		this.btnDisplay = btnDisplay;
		btnDisplay.setOnClickListener(this);
	}

	@Override
	public View initViews() {
		/*
		 * TextView view = new TextView(mActivity); view.setTextSize(22);
		 * view.setTextColor(Color.RED); view.setGravity(Gravity.CENTER);// 居中显示
		 * view.setText("菜单详情页-组图");
		 */

		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

		String cache = CacheUtils.getCache(mActivity,
				GlobalConstants.PHOTOS_URL);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();

		httpUtils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String result = responseInfo.result;
						processData(result);

						CacheUtils.setCache(mActivity,
								GlobalConstants.PHOTOS_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();

					}
				});

	}

	private void processData(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		photosBean photosBean = gson.fromJson(result, photosBean.class);

		mPhotoList = photosBean.data.news;

		// 给ListView设置数据
		lvList.setAdapter(new PhotosAdapter());
		// 给GridView设置数据
		gvList.setAdapter(new PhotosAdapter());

	}

	class PhotosAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotosAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPhotoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photo,
						null);

				viewHolder = new ViewHolder();

				viewHolder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			PhotoNews item = (PhotoNews) getItem(position);
			viewHolder.tvTitle.setText(item.title);

			mBitmapUtils.display(viewHolder.ivPic, item.listimage);
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivPic;
	}

	private boolean isListView = true;

	@Override
	public void onClick(View v) {

		if (isListView) {
			lvList.setVisibility(View.GONE);
			gvList.setVisibility(View.VISIBLE);

			btnDisplay.setImageResource(R.drawable.icon_pic_list_type);

			isListView = false;
		} else {
			lvList.setVisibility(View.VISIBLE);
			gvList.setVisibility(View.GONE);

			btnDisplay.setImageResource(R.drawable.icon_pic_grid_type);

			isListView = true;
		}

	}
}
