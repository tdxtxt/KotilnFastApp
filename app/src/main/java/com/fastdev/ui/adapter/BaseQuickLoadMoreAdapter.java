package com.fastdev.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fastdev.data.BaseListBody;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public abstract class BaseQuickLoadMoreAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> implements LoadMoreModule {
    public BaseQuickLoadMoreAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
//        setEmptyView(R.layout.base_statelayout_empty);
    }

    public BaseQuickLoadMoreAdapter(int layoutResId) {
        super(layoutResId);
//        setEmptyView(R.layout.base_statelayout_empty);
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
}
