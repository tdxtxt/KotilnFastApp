package com.fast.libdeveloper;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


abstract class ConfigAdapter extends BaseAdapter {
  @Override
  public abstract String getItem(int position);

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public final View getView(int position, View view, ViewGroup container) {
    if (view == null) {
      view = LayoutInflater.from(container.getContext())
          .inflate(android.R.layout.simple_spinner_item, container, false);
    }
    bindView(getItem(position), view);
    return view;
  }

  @Override
  public final View getDropDownView(int position, View view, ViewGroup container) {
    if (view == null) {
      view = LayoutInflater.from(container.getContext())
          .inflate(android.R.layout.simple_spinner_item, container, false);
    }
    bindView(getItem(position), view);
    return view;
  }

  private void bindView(String item, View view) {
    ConfigAdapter.ViewHolder holder;
    if (view.getTag() instanceof ConfigAdapter.ViewHolder) {
      holder = (ConfigAdapter.ViewHolder) view.getTag();
    } else {
      holder = new ConfigAdapter.ViewHolder();
      holder.textView = (TextView) view.findViewById(android.R.id.text1);
      int padding = (int) view.getResources().getDimension(R.dimen.padding);
      holder.textView.setPadding(padding, padding, padding, padding);
      holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          view.getResources().getDimension(R.dimen.font_normal));
      holder.textView.setTextColor(view.getResources().getColor(R.color.debug_text_secondary));
      holder.textView.setGravity(Gravity.CENTER);
      view.setTag(holder);
    }
    holder.textView.setText(item);
  }

  private static final class ViewHolder {
    TextView textView;
  }
}
