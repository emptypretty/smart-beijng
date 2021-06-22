package com.itcast.zhbj.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itcast.zhbj.MainActivity;
import com.itcast.zhbj.R;
import com.itcast.zhbj.base.impl.NewsCenterPager;
import com.itcast.zhbj.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 侧边栏fragment
 * 
 * */
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_menu)
	private ListView lvList;

	public ArrayList<NewsMenuData> data;// 分类的网络数据

	private int mCurrentPos;// 当前选中的菜单位置

	private LeftMenuAdapter mAdapter;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);

		return view;
	}

	// 设置侧边栏数据
	// 通过此方法，可以从新闻中心页面将网络数据传递过来
	public void setMenuData(ArrayList<NewsMenuData> data) {
		System.out.println("侧边栏拿到的数据" + data.size());

		mCurrentPos = 0;

		this.data = data;
		mAdapter = new LeftMenuAdapter();
		lvList.setAdapter(mAdapter);

		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { // TODO Auto-generated method stub

				mCurrentPos = position;// 更新当前点击位置
				// 刷新ListView
				mAdapter.notifyDataSetChanged();

				// 收回侧边栏
				toggle();

				setMenuDetailPager(position);

			}
		});

	}

	// 修改菜单详情页
	protected void setMenuDetailPager(int position) {
		// 修改新闻中心的帧布局
		// 获取新闻中心的对象
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment fragment = mainUI.getContentFragment();
		NewsCenterPager pager = fragment.getNewsCenterPager();

		// 由新闻中心修改菜单详情页
		pager.setMenuDetailPager(position);
	}

	// 控制侧边栏的开关
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 如果当前为开, 则关;反之亦然
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(mActivity, R.layout.list_item_left_menu,
					null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

			// 设置TextView的可用或不可用来控制颜色

			if (mCurrentPos == position) {
				// 当前item被选中
				tvMenu.setEnabled(true);
			} else {
				// 未选中
				tvMenu.setEnabled(false);
			}

			NewsMenuData info = (NewsMenuData) getItem(position);
			tvMenu.setText(info.title);

			return view;
		}

	}
}
