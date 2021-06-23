package com.itcast.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itcast.zhbj.R;

public class RefreshView extends ListView implements OnScrollListener {

	private View mHeaderView;
	private int mHeaderViewHeight;
	private int startY = -1;

	private static final int STATE_PULL_TO_REFRESH = 0;// 下拉刷新状态
	private static final int STATE_RELEASE_TO_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新

	private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前状态,默认下拉刷新

	private TextView tvState;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbLoading;

	private RotateAnimation animUp;
	private RotateAnimation animDown;

	private OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	private boolean isLoadMore;// 标记是否正在加载更多

	public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();
		initFooterView();

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

		tvState = (TextView) mHeaderView.findViewById(R.id.tv_state);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

		// 隐藏头布局
		// 获取当前头布局的高度，然后设置负paddingTop，布局就会向上走
		// int height = mHeaderView.getHeight();
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

		initArrowAnim();
		// 更新刷新时间
		setRefreshTime();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 没有获取到按下的事件（按住头条新闻滑动时，按下事件被Viewpager消费了）
				startY = (int) ev.getY();// 重新获取起点位置
			}
			int endY = (int) ev.getY();

			int dy = endY - startY;

			// 如果正在刷新，什么都不做
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int firstVisiblePosition = this.getFirstVisiblePosition();// 当前显示的第一个item的位置
			if (dy > 0 && firstVisiblePosition == 0) {
				// 下拉动作&当前在listView的顶部
				int padding = -mHeaderViewHeight + dy;

				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 切换到松开刷新状态

					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (padding <= 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					// 切换到下拉刷新状态
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				// 通过修改padding来设置当前刷新控件的最新位置
				mHeaderView.setPadding(0, padding, 0, 0);
				return true;// 消费此事件，处理下拉刷新控件的滑动，不需要listView原生效果参与
			}
			break;
		case MotionEvent.ACTION_UP:

			startY = -1;// 起始坐标归零

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// 切换成正在刷新
				mCurrentState = STATE_REFRESHING;

				// 完整显示刷新控件

				mHeaderView.setPadding(0, 0, 0, 0);
				refreshState();
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void initArrowAnim() {
		// 箭头向上
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(300);
		animUp.setFillAfter(true);// 保持住动画结束的状态

		// 箭头向下
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(300);
		animDown.setFillAfter(true);// 保持住动画结束的状态

	}

	// 根据当前状态刷新界面
	private void refreshState() {

		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:

			tvState.setText("下拉刷新");
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animDown);
			break;
		case STATE_RELEASE_TO_REFRESH:

			tvState.setText("松开刷新");
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animUp);

			break;
		case STATE_REFRESHING:
			tvState.setText("正在刷新");
			pbLoading.setVisibility(View.VISIBLE);
			ivArrow.clearAnimation();// 需要先清理动画才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);

			if (mListener != null) {
				mListener.onRefresh();
			}

			break;

		default:
			break;
		}

	}

	// 设置刷新回调监听
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	// 回调接口，通知刷新状态
	public interface OnRefreshListener {
		// 下拉刷新的回调
		public void onRefresh();

		// 加载的回调接口
		public void onLoadMore();

	}

	// 刷新结束，隐藏控件
	public void OnRefreshComplete() {

		if (!isLoadMore) {
			// 隐藏控件
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			// 初始化状态
			tvState.setText("下拉刷新");
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			mCurrentState = STATE_PULL_TO_REFRESH;

			// 更新刷新时间
			setRefreshTime();
		} else {
			// 隐藏加载更多的控件
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadMore = false;
		}

	}

	// 设置刷新事件
	private void setRefreshTime() {
		// HH：24小时制
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());
		tvTime.setText(time);
	}

	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.pull_to_refresh_footer, null);
		addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();

		// 隐藏脚布局
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

		if (scrollState == SCROLL_STATE_IDLE) {// 空闲状态
			int lastVisiblePosition = getLastVisiblePosition();// 当前显示的最后一个item的位置
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {

				isLoadMore = true;
				// 显示加载中
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);// 显示在租后一个item的位置
				// 加载更多数据

				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

}
