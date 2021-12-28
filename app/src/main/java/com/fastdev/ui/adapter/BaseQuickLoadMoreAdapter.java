package com.fastdev.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fastdev.data.BaseListBody;
import com.fastdev.data.response.SourceBean;
import com.fastdev.ui.R;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public abstract class BaseQuickLoadMoreAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> implements LoadMoreModule {
    int emptyLayoutResId;

    public BaseQuickLoadMoreAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.emptyLayoutResId = R.layout.baselib_layout_empty;
    }

    public BaseQuickLoadMoreAdapter(int layoutResId, int emptyLayoutResId) {
        super(layoutResId);
        this.emptyLayoutResId = emptyLayoutResId;
    }

    public void updateData(int pageNum, BaseListBody<T> data){
        if(data == null || data.getList() == null) return;
        if(pageNum == 1){//下拉刷新
            setNewInstance(data.getList());
        }else{//加载更多
            addData(data.getList());
        }
        getLoadMoreModule().loadMoreComplete();
        if(!data.isNextPage()) getLoadMoreModule().loadMoreEnd();
    }

    public void updateData(int pageNum, List<T> data){
        if(pageNum == 1){
            setNewInstance(data);
            getLoadMoreModule().loadMoreComplete();
            if(data == null || data.size() == 0){
                if(emptyLayoutResId > 0) setEmptyView(emptyLayoutResId);
                getLoadMoreModule().loadMoreEnd();
            }
        }else{
            if(data == null || data.size() == 0){
                getLoadMoreModule().loadMoreComplete();
                getLoadMoreModule().loadMoreEnd();
            }else{
                addData(data);
                getLoadMoreModule().loadMoreComplete();
            }
        }
    }

    public void updateItem(T item){
        if(item == null) return;
        int index = getData().indexOf(item);
        if(index > -1){
            getData().set(index, item);
            notifyItemChanged(index);
        }
    }
}
