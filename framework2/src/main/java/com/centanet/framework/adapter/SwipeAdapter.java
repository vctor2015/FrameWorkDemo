package com.centanet.framework.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centanet.framework.R;
import com.centanet.framework.adapter.vhs.DefVH;
import com.centanet.framework.adapter.vhs.MoreVH;
import com.centanet.framework.iml.SwipeHelper;
import com.centanet.framework.iml.SwipeItemCallback;
import com.centanet.framework.iml.SwipeRefreshCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vctor2015 on 16/7/15.
 * <p>
 * {@link #getLoadStatus()}加载状态-1:初始化;0:普通状态;1:下拉刷新;2:加载更多;3:加载出错
 * <p>
 * 描述:列表adapter基类
 */
@SuppressWarnings("all")
public abstract class SwipeAdapter<E, UVH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SwipeHelper<DefVH, MoreVH, UVH> {

    private static final int ITEM_DEF = 100;
    private static final int ITEM_MORE = 101;
    /**
     * 用户数据
     */
    protected final ArrayList<E> mList = new ArrayList<>();
    /**
     * 点击回调
     */
    protected final SwipeItemCallback<E> mSwipeItemCallback;
    final LayoutInflater mLayoutInflater;
    private SwipeRefreshCallback mSwipeRefreshCallback = new SwipeRefreshCallback() {
        @Override
        public void downRefresh() {

        }

        @Override
        public void upRefresh(int count) {

        }

        @Override
        public void refreshComplete() {

        }
    };

    private int mLoadStatus = -1;//加载状态
    private boolean mHasMore;//是否有更多选项

    SwipeAdapter(Context context, SwipeItemCallback<E> swipeItemCallback) {
        mSwipeItemCallback = swipeItemCallback;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 设置刷新回调
     */
    public void setSwipeRefreshCallback(SwipeRefreshCallback swipeRefreshCallback) {
        mSwipeRefreshCallback = swipeRefreshCallback;
    }

    /**
     * 加载数据
     *
     * @param temp  每次请求到的数据
     * @param total 列表数据的总数
     */
    public void load(List<E> temp, int total) {
        mSwipeRefreshCallback.refreshComplete();
        switch (mLoadStatus) {
            case 1:
                mLoadStatus = 0;
                mList.clear();
                if (temp != null) {
                    mList.addAll(temp);
                }
                mHasMore = mList.size() < total;
                notifyDataSetChanged();
                break;
            case 2:
                mLoadStatus = 0;
                if (temp == null) {
                    mHasMore = mList.size() < total;
                    notifyDataSetChanged();
                } else {
                    mList.addAll(temp);
                    mHasMore = mList.size() < total;
                    if (mHasMore) {
                        notifyItemRangeInserted(getItemCount() - temp.size(), mList.size());
                    } else {
                        notifyItemRangeChanged(mList.size() - temp.size(), mList.size());
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 加载数据[需判断是够有更多数据]
     *
     * @param temp    每次请求到的数据
     * @param hasMore 是否有更多
     */
    public void load(List<E> temp, boolean hasMore) {
        mSwipeRefreshCallback.refreshComplete();
        switch (mLoadStatus) {
            case 1:
                mLoadStatus = 0;
                mList.clear();
                if (temp != null) {
                    mList.addAll(temp);
                }
                mHasMore = hasMore;
                notifyDataSetChanged();
                break;
            case 2:
                mLoadStatus = 0;
                mHasMore = hasMore;
                if (temp == null) {
                    notifyDataSetChanged();
                } else {
                    mList.addAll(temp);
                    if (mHasMore) {
                        notifyItemRangeInserted(getItemCount() - temp.size(), mList.size());
                    } else {
                        notifyItemRangeChanged(mList.size() - temp.size(), mList.size());
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新错误
     */
    public void error() {
        mSwipeRefreshCallback.refreshComplete();
        switch (mLoadStatus) {
            case 1:
                mLoadStatus = 3;
                mList.clear();
                notifyDataSetChanged();
                break;
            case 2:
                mLoadStatus = 3;
                notifyItemChanged(mList.size());
                break;
            default:
                break;
        }
    }

    /**
     * 重置
     */
    public void reset() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取用户数据列表
     */
    public ArrayList<E> getList() {
        return mList;
    }

    @Override
    public int getItemViewType(int position) {
        int userDataSize = mList.size();
        if (userDataSize == 0) {
            return ITEM_DEF;
        } else {
            return position < userDataSize ? getUserItemViewType(position) : ITEM_MORE;
        }
    }

    /**
     * 用户的ItemViewType
     */
    @SuppressWarnings("all")
    protected int getUserItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_DEF:
                return defViewHolder(parent);
            case ITEM_MORE:
                return moreViewHolder(parent);
            default:
                return userViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_DEF:
                bindDefViewHolder((DefVH) holder);
                break;
            case ITEM_MORE:
                bindMoreViewHolder((MoreVH) holder);
                break;
            default:
                bindUserViewHolder(castVH(holder), position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSwipeItemCallback.callback(view, holder.getAdapterPosition(),
                                mList.get(holder.getAdapterPosition()));
                    }
                });
                break;
        }
    }

    /**
     * 用户数据为空,则显示默认页面
     */
    @Override
    public int getItemCount() {
        int userDataSize = mList.size();
        return userDataSize == 0 ? 1 : mHasMore ? userDataSize + 1 : userDataSize;
    }

    @Override
    public DefVH defViewHolder(ViewGroup parent) {
        return new DefVH(mLayoutInflater.inflate(R.layout.item_swipe_def, parent, false));
    }

    @Override
    public MoreVH moreViewHolder(ViewGroup parent) {
        return new MoreVH(mLayoutInflater.inflate(R.layout.item_swipe_more, parent, false));
    }

    @Override
    public void bindDefViewHolder(DefVH holder) {
        switch (mLoadStatus) {
            case 0:
                holder.mAtvSwipeDef.setVisibility(View.VISIBLE);
                holder.mAtvSwipeDef.setText(defText());
                break;
            case 3:
                holder.mAtvSwipeDef.setVisibility(View.VISIBLE);
                holder.mAtvSwipeDef.setText(defErrorText());
                break;
            default:
                holder.mAtvSwipeDef.setVisibility(View.GONE);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoadStatus == 3) {
                    mLoadStatus = 1;
                    notifyItemChanged(0);
                    mSwipeRefreshCallback.downRefresh();
                }
            }
        });
    }

    @Override
    public void bindMoreViewHolder(MoreVH holder) {
        switch (mLoadStatus) {
            case 3:
                holder.mAtvSwipeMore.setText(moreErrorText());
                holder.mPbSwipeMore.setVisibility(View.GONE);
                break;
            default:
                holder.mAtvSwipeMore.setText(moreText());
                holder.mPbSwipeMore.setVisibility(View.VISIBLE);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoadStatus == 3) {
                    mLoadStatus = 2;
                    notifyItemChanged(mList.size());
                    mSwipeRefreshCallback.upRefresh(mList.size());
                }
            }
        });
    }

    @Override
    public int userDataCount() {
        return mList.size();
    }

    /**
     * 加载状态
     */
    public int getLoadStatus() {
        return mLoadStatus;
    }

    /**
     * 设置加载状态
     */
    public void setLoadStatus(int loadStatus) {
        mLoadStatus = loadStatus;
    }

    /**
     * 是否有更多
     */
    public boolean isHasMore() {
        return mHasMore;
    }

    /**
     * 没有数据时显示的默认文本
     */
    @StringRes
    protected int defText() {
        return R.string.swipe_def_text;
    }

    /**
     * 加载出错显示文本
     */
    @StringRes
    @SuppressWarnings("all")
    protected int defErrorText() {
        return R.string.swipe_def_error;
    }

    /**
     * 更多加载显示的默认文本
     */
    @StringRes
    @SuppressWarnings("all")
    protected int moreText() {
        return R.string.swipe_loading_more;
    }

    /**
     * 加载更多出错显示的文本
     */
    @StringRes
    @SuppressWarnings("all")
    protected int moreErrorText() {
        return R.string.swipe_loading_error;
    }

    /**
     * 用户{@link android.support.v7.widget.RecyclerView.ViewHolder}转化
     */
    protected abstract UVH castVH(RecyclerView.ViewHolder holder);

}
