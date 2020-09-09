package com.lxj.xpopup.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.recyclerview.MultiItemTypeAdapter;
import com.adapter.recyclerview.RQuickAdapter;
import com.adapter.recyclerview.base.ViewHolder;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.openlib.R;

import java.util.Arrays;

/**
 * Description: Attach类型的列表弹窗
 * Create by dance, at 2018/12/12
 */
public class AttachListPopupView extends AttachPopupView {
    RecyclerView recyclerView;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    /**
     *
     * @param context
     * @param bindLayoutId layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的ImageView，和id为tv_text的TextView
     */
    public AttachListPopupView(@NonNull Context context, int bindLayoutId, int bindItemLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? R.layout._xpopup_attach_impl_list : bindLayoutId;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.recyclerView);
        if(recyclerView instanceof VerticalRecyclerView){
//            ((VerticalRecyclerView)recyclerView).setupDivider(popupInfo.isDarkTheme);
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        final RQuickAdapter<String> adapter = new RQuickAdapter<String>(getContext(), bindItemLayoutId == 0 ? R.layout._xpopup_adapter_text : bindItemLayoutId, Arrays.asList(data)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_text, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(R.id.iv_image).setVisibility(VISIBLE);
                    holder.getView(R.id.iv_image).setBackgroundResource(iconIds[position]);
                } else {
                    holder.getView(R.id.iv_image).setVisibility(GONE);
                }
                View check = holder.getView(R.id.check_view);
                if (check!=null) check.setVisibility(GONE);

                if(bindItemLayoutId==0 && popupInfo.isDarkTheme){
                    holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_white_color));
                }
                if(position == getItemCount() - 1){
                    holder.getView(R.id.view_line).setVisibility(View.GONE);
                }else{
                    holder.getView(R.id.view_line).setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getItem(position));
                }
                if (popupInfo.autoDismiss) dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        if (bindLayoutId==0 && popupInfo.isDarkTheme){
            applyDarkTheme();
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
//        recyclerView.setBackgroundResource(R.drawable._xpopup_round3_dark_bg);
//        attachPopupContainer.setBackgroundResource(R.drawable._xpopup_round3_dark_bg);
    }

    String[] data;
    int[] iconIds;

    public AttachListPopupView setStringData(String[] data, int[] iconIds) {
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

//    public AttachListPopupView setOffsetXAndY(int offsetX, int offsetY) {
//        this.defaultOffsetX += offsetX;
//        this.defaultOffsetY += offsetY;
//        return this;
//    }

    private OnSelectListener selectListener;

    public AttachListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }
}
