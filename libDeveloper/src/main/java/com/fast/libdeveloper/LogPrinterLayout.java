package com.fast.libdeveloper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


final class LogPrinterLayout extends LinearLayout implements View.OnClickListener {
  private final CompositeDisposable subscriptions = new CompositeDisposable();

  private RecyclerView recyclerView;
  private LogAdapter adapter;

  private LumberYard lumberYard;

  public LogPrinterLayout(Context context) {
    super(context);
    initData();
  }

  public LogPrinterLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initData();
  }

  public LogPrinterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initData();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public LogPrinterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initData();
  }

  public void setLumberYard(LumberYard lumberYard) {
    this.lumberYard = lumberYard;
  }

  private void initData() {

  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    recyclerView = (RecyclerView) findViewById(R.id.recycler_log_printer);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setStackFromEnd(true);
    layoutManager.setReverseLayout(true);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new LogAdapter();
    recyclerView.setAdapter(adapter);

    findViewById(R.id.btn_scroll_to_top).setOnClickListener(this);
    findViewById(R.id.btn_scroll_to_bottom).setOnClickListener(this);
    findViewById(R.id.btn_clear).setOnClickListener(this);
    findViewById(R.id.btn_share).setOnClickListener(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    adapter.setLogs(lumberYard.bufferedLogs());
    subscriptions.add(lumberYard.logs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Log>() {
          @Override
          public void accept(Log log) throws Exception {
            adapter.addLog(log);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable.getMessage());
          }
        }));
  }

  @Override
  protected void onDetachedFromWindow() {
    adapter.clear();
    subscriptions.clear();
    super.onDetachedFromWindow();
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.btn_scroll_to_top) {
      int count = adapter.getItemCount();
      int position = count - 1;
      if (position > 0) {
        recyclerView.scrollToPosition(position);
      }
    } else if (id == R.id.btn_scroll_to_bottom) {
      recyclerView.scrollToPosition(0);
    } else if (id == R.id.btn_clear) {
      adapter.clear();
      lumberYard.clear();
    } else if (id == R.id.btn_share) {
      subscriptions.add(Utils.logsShare(getContext(), lumberYard));
    }
  }

  private static class LogAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final LinkedList<Log> logs = new LinkedList<>();

    void setLogs(List<Log> logs) {
      if (logs != null) {
        this.logs.clear();
        this.logs.addAll(logs);
        notifyDataSetChanged();
      }
    }

    void clear() {
      logs.clear();
      notifyDataSetChanged();
    }

    void addLog(Log log) {
      logs.addLast(log);
      notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      holder.setEntry(logs.get(position));
    }

    @Override
    public int getItemCount() {
      return logs.size();
    }
  }

  private static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvLevel;
    TextView tvTag;
    TextView tvMessage;

    ViewHolder(View itemView) {
      super(itemView);

      tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
      tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
      tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
    }

    static @DrawableRes
    int backgroundForLevel(int level) {
      switch (level) {
        case android.util.Log.VERBOSE:
        case android.util.Log.DEBUG:
          return R.color.debug_log_accent_debug;
        case android.util.Log.INFO:
          return R.color.debug_log_accent_info;
        case android.util.Log.WARN:
          return R.color.debug_log_accent_warn;
        case android.util.Log.ERROR:
        case android.util.Log.ASSERT:
          return R.color.debug_log_accent_error;
        default:
          return R.color.debug_log_accent_unknown;
      }
    }

    void setEntry(Log log) {
      if (log != null) {
        itemView.setBackgroundResource(backgroundForLevel(log.level()));
        tvLevel.setText(log.levelString());
        tvTag.setText(log.tag());
        tvMessage.setText(log.message());
      }
    }
  }
}
