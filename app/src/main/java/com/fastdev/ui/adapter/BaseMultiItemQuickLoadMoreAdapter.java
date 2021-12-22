package com.fastdev.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fastdev.data.BaseListBody;

public abstract class BaseMultiItemQuickLoadMoreAdapter<T extends MultiItemEntity, VH extends BaseViewHolder> extends BaseMultiItemQuickAdapter<T, VH> implements LoadMoreModule {
    public BaseMultiItemQuickLoadMoreAdapter(){
        super();
//        setEmptyView(R.layout.base_statelayout_empty);
        addItemTypeLayout();
    }
    protected abstract void addItemTypeLayout();

    @Override
    protected int getDefItemViewType(int position) {
        return super.getDefItemViewType(position);
    }

    public void updateData(boolean isFirstPage, BaseListBody<T> data){
        if(data == null || data.getList() == null) return;
        if(isFirstPage){//下拉刷新
            setNewInstance(data.getList());
        }else{//加载更多
            addData(data.getList());
        }
        getLoadMoreModule().loadMoreComplete();
        if(!data.isNextPage()) getLoadMoreModule().loadMoreEnd();
    }

    public void refreshItem(T item){
        if(item == null) return;
        int index = getData().indexOf(item);
        if (index < 0) {
            return;
        }
        setData(index, item);
    }
}
