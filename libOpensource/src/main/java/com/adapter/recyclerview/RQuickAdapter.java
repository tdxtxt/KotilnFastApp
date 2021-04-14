package com.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.adapter.recyclerview.base.ItemViewDelegate;
import com.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class RQuickAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public RQuickAdapter(Context context, final int layoutId, List<T> datas, boolean isLoop)
    {
        super(context, datas, isLoop);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas == null ? new ArrayList<T>() : datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                RQuickAdapter.this.convert(holder, t, position);
            }
        });
    }
    public RQuickAdapter(final Context context, final int layoutId, List<T> datas)
    {
        this(context, layoutId, datas, false);

    }


    protected abstract void convert(ViewHolder holder, T t, int position);


}
