package com.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.adapter.recyclerview.base.ItemViewDelegate;
import com.adapter.recyclerview.base.ItemViewDelegateManager;
import com.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhy on 16/4/9.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;
    protected boolean isLoop = false;//是否轮询

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public MultiItemTypeAdapter(Context context, List<T> datas, boolean isLoop) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        this.isLoop = isLoop;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        int realPosition = isLoop ? position % mDatas.size() : position;
        return mItemViewDelegateManager.getItemViewType(mDatas.get(realPosition), realPosition);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder,View itemView){

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int realPosition = isLoop ? viewHolder.getAdapterPosition() % mDatas.size() : viewHolder.getAdapterPosition();
                    int position = realPosition;
                    mOnItemClickListener.onItemClick(v, viewHolder , position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int realPosition = isLoop ? viewHolder.getAdapterPosition() % mDatas.size() : viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, realPosition);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int realPosition = isLoop ? position % mDatas.size() : position;
        convert(holder, mDatas.get(realPosition));
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return isLoop ? Integer.MAX_VALUE : itemCount;
    }


    public T getItem(int position){
        if(position < 0) return null;
        if(position > getDatas().size() - 1) return null;
        return getDatas().get(position);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setLoop(boolean isLoop){
        this.isLoop = isLoop;
        notifyDataSetChanged();
    }

    public boolean isLoop(){
        return isLoop;
    }

    public void add(int position, T item){
        if(item == null) return;
        mDatas.add(position,item);
        notifyItemInserted(position);
        compatibilityDataSizeChanged(1);
    }

    public void addData(int position, Collection<? extends T> newData) {
        mDatas.addAll(position, newData);
        notifyItemRangeInserted(position, newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void setNewData(Collection<? extends T> newData){
        mDatas.clear();
        mDatas.addAll(newData);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(position < 0 || position >= mDatas.size()) return;
        mDatas.remove(position);
        int internalPosition = position;
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mDatas.size() - internalPosition);
    }


    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mDatas == null ? 0 : mDatas.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public static abstract class OnItemClickListener {
        public abstract void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position){ return false; }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
