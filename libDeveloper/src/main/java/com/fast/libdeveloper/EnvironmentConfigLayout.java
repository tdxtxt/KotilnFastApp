package com.fast.libdeveloper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import static com.fast.libdeveloper.Constants.*;


public class EnvironmentConfigLayout extends LinearLayout implements View.OnClickListener {
  private static final int[] MOCK_DELAY_SECONDS = { 1, 3, 5, 10, 20 };

  private Spinner spinnerEndpoint;
  private View btnEdit;
  private EditText remoteDebuggerAddrView;
  private Switch remoteDebuggerSwitch;
  private View layoutMockDelay;
  private Spinner spinnerMockDelay;
  private CustomEndpointDialog dialogCustomEndpoint;

  private ConfigAdapter adapterEndpoint;
  private ConfigAdapter adapterMockDelay;

  private final List<ExtraUrl> currentExtraUrlList = new ArrayList<>();
  private Endpoint endpoint;
  private String currentBaseUrl;
  private int currentEndPointIndex;
  private int currentMockDelayIndex;
  private RemoteDebugger remoteDebugger;

  public EnvironmentConfigLayout(Context context) {
    super(context);
    initData();
  }

  public EnvironmentConfigLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initData();
  }

  public EnvironmentConfigLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initData();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public EnvironmentConfigLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initData();
  }

  public void setEndpoint(Endpoint endpoint) {
    this.endpoint = endpoint;

    restoreViewState();
  }

  public void setLumberYard(LumberYard lumberYard) {
    remoteDebugger = new RemoteDebugger(lumberYard);
  }

  private void initData() {
    currentEndPointIndex = getEndpointIndex(getContext(), 0);
    currentBaseUrl =
            getEndpointUrl(getContext(), endpoint == null ? null : endpoint.url(currentEndPointIndex));
    List<ExtraUrl> extraUrlList = getEndpointExtraUrl(getContext(),
            endpoint == null ? null : endpoint.extraUrls(currentEndPointIndex));
    if (extraUrlList != null) {
      currentExtraUrlList.clear();
      currentExtraUrlList.addAll(extraUrlList);
    }
    currentMockDelayIndex = getMockDelayIndex(getContext());
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    spinnerEndpoint = findViewById(R.id.spinner_endpoint);
    remoteDebuggerAddrView = findViewById(R.id.remote_debugger_addr);
    remoteDebuggerSwitch = findViewById(R.id.remote_debugger_switch);
    String remoteDebuggerAddr = getRemoteDebuggerAddr(getContext());
    remoteDebuggerAddrView.setText(remoteDebuggerAddr);
    remoteDebuggerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (remoteDebugger == null) {
          remoteDebuggerSwitch.setChecked(!isChecked);
          return;
        }
        if (isChecked) {
          String addr = remoteDebuggerAddrView.getText().toString();
          // ws://10.16.74.230:8765/echo
          remoteDebugger.start("ws://" + addr + "/echo");
          setRemoteDebuggerAddr(getContext(), addr);
        } else {
          remoteDebugger.stop();
        }
      }
    });
    btnEdit = findViewById(R.id.iv_edit);
    layoutMockDelay = findViewById(R.id.layout_mock_delay);
    spinnerMockDelay = findViewById(R.id.spinner_mock_delay);

    spinnerEndpoint.setAdapter(adapterEndpoint = new ConfigAdapter() {
      @Override
      public String getItem(int position) {
        if (endpoint != null) {
          return endpoint.name(position);
        }
        return null;
      }

      @Override
      public int getCount() {
        return endpoint == null ? 0 : endpoint.count();
      }
    });
    spinnerEndpoint.setSelection(currentEndPointIndex);
    spinnerEndpoint.setOnItemSelectedListener(new SimpleItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (endpoint != null && currentEndPointIndex != position) {
          if (endpoint.isCustom(position)) {
            showCustomEndpointDialog(position);
          } else {
            currentEndPointIndex = position;
            if (endpoint != null) {
              setEndpoint(getContext(), currentEndPointIndex, endpoint.name(position),
                      endpoint.url(position), endpoint.extraUrls(position));
              endpoint.changeIndex(currentEndPointIndex);
              Utils.restart(getContext());
            }
          }
        }
      }
    });

    spinnerMockDelay.setAdapter(adapterMockDelay = new ConfigAdapter() {
      @Override
      public String getItem(int position) {
        return String.valueOf(MOCK_DELAY_SECONDS[position]);
      }

      @Override
      public int getCount() {
        return MOCK_DELAY_SECONDS.length;
      }
    });
    spinnerMockDelay.setSelection(currentMockDelayIndex);
    spinnerMockDelay.setOnItemSelectedListener(new SimpleItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (currentMockDelayIndex != position) {
          currentMockDelayIndex = position;
          setMockDelayIndex(getContext(), currentMockDelayIndex);
        }
      }
    });

    btnEdit.setOnClickListener(this);
    findViewById(R.id.tvBtn_dev).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        view.getContext().startActivity(new Intent("com.dev.tools.ui"));
      }
    });
    ((TextView)findViewById(R.id.tv_version)).setText("测试版本号:"+Utils.getVersionCode(getContext()));
  }

  @Override
  protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);

    if (this.equals(changedView) && visibility == VISIBLE) {
      restoreViewState();
    }
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    super.onRestoreInstanceState(state);

    restoreViewState();
  }

  @Override
  protected void onDetachedFromWindow() {
    if (dialogCustomEndpoint != null && dialogCustomEndpoint.isShowing()) {
      dialogCustomEndpoint.dismiss();
    }
    if (remoteDebugger != null) {
      remoteDebugger.stop();
    }
    super.onDetachedFromWindow();
  }

  private void restoreViewState() {
    initData();

    btnEdit.setVisibility(
            endpoint != null && endpoint.isCustom(currentEndPointIndex) ? VISIBLE : GONE);
    layoutMockDelay.setVisibility(
            endpoint != null && endpoint.isMock(currentEndPointIndex) ? VISIBLE : GONE);
    spinnerEndpoint.setSelection(currentEndPointIndex);
    adapterEndpoint.notifyDataSetChanged();
    spinnerMockDelay.setSelection(currentMockDelayIndex);
    adapterMockDelay.notifyDataSetChanged();
  }

  private void showCustomEndpointDialog(final int newSelection) {
    final String baseUrl =
            currentBaseUrl == null ? endpoint.url(currentEndPointIndex) : currentBaseUrl;
    final List<ExtraUrl> extraUrlList = endpoint.extraUrls(newSelection);
    dialogCustomEndpoint = new CustomEndpointDialog(getContext(), baseUrl, extraUrlList,
            new CustomEndpointDialog.SetCustomEndpointListener() {
              @Override
              public void onSetCustomEndpoint(String name, String baseUrl,
                                              List<ExtraUrl> extraUrlList) {
                currentEndPointIndex = newSelection;
                setEndpoint(getContext(), currentEndPointIndex, name, baseUrl, extraUrlList);
                if(endpoint != null) endpoint.changeIndex(currentEndPointIndex);
                Utils.restart(getContext());
              }

              @Override
              public void onCancel() {
                spinnerEndpoint.setSelection(currentEndPointIndex);
              }
            });
    dialogCustomEndpoint.show();
  }

  @Override
  public void onClick(View v) {
    showCustomEndpointDialog(currentEndPointIndex);
  }

  static void setEndpoint(Context context, int index, String name, String baseUrl,
                          List<ExtraUrl> extraUrlList) {
    SharedPreferences.Editor editor = Prefers.with(context).load().editor();
    editor.putInt(Constants.KEY_ENDPOINT_CURRENT_INDEX, index);
    editor.putString(Constants.KEY_ENDPOINT_NAME, name);
    editor.putString(Constants.KEY_ENDPOINT_BASE_URL, baseUrl);
    for (int i = 0; i < extraUrlList.size(); i++) {
      editor.putString(Constants.KEY_ENDPOINT_EXTRA_URL_LIST_URL + i, extraUrlList.get(i).url());
      editor.putString(Constants.KEY_ENDPOINT_EXTRA_URL_LIST_DESCRIPTION + i,
              extraUrlList.get(i).description());
    }
    editor.commit();
  }

  static void setMockDelayIndex(Context context, int index) {
    Prefers.with(context).load().save(Constants.KEY_MOCK_DELAY_INDEX, index);
  }

  static void setRemoteDebuggerAddr(Context context, String addr) {
    Prefers.with(context).load().save(Constants.KEY_REMOTE_DEBUGGER_ADDR, addr);
  }

  static String getRemoteDebuggerAddr(Context context) {
    return Prefers.with(context).load().getString(Constants.KEY_REMOTE_DEBUGGER_ADDR, "10.16.74.230:8765");
  }

  static int getEndpointIndex(Context context, int defaultValue) {
    return Prefers.with(context).load().getInt(Constants.KEY_ENDPOINT_CURRENT_INDEX, defaultValue);
  }

  static String getEndpointUrl(Context context, String defaultValue) {
    return Prefers.with(context).load().getString(Constants.KEY_ENDPOINT_BASE_URL, defaultValue);
  }

  static String getEndpointName(Context context, String defaultValue) {
    return Prefers.with(context).load().getString(Constants.KEY_ENDPOINT_NAME, defaultValue);
  }

  static List<ExtraUrl> getEndpointExtraUrl(Context context, List<ExtraUrl> defValue) {
    if (defValue != null) {
      for (int i = 0; i < defValue.size(); i++) {
        ExtraUrl extraUrl = defValue.get(i);
        extraUrl.url =
                Prefers.with(context).load().getString(Constants.KEY_ENDPOINT_EXTRA_URL_LIST_URL + i, extraUrl.url());
        extraUrl.description = Prefers.with(context).load()
                .getString(Constants.KEY_ENDPOINT_EXTRA_URL_LIST_DESCRIPTION + i, extraUrl.description());
      }
    }
    return defValue;
  }

  static int getMockDelayIndex(Context context) {
    return Prefers.with(context).load().getInt(Constants.KEY_MOCK_DELAY_INDEX, 0);
  }

  static int getMockDelayMilliseconds(Context context) {
    return MOCK_DELAY_SECONDS[getMockDelayIndex(context)] * 1000;
  }

  private static class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  }
}
