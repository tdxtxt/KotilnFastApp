package com.fast.libdeveloper;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import androidx.drawerlayout.widget.DrawerLayout;


public class DebugDrawerLayout extends DrawerLayout {
  private EnvironmentConfigLayout layoutEnvironmentConfig;
  private LogPrinterLayout layoutLogPrint;

  private LumberYard lumberYard;
  private Endpoint endpoint;

  public DebugDrawerLayout(Context context) {
    super(context);
    init();
  }

  public DebugDrawerLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DebugDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public void setLumberYard(LumberYard lumberYard) {
    this.lumberYard = lumberYard;
  }

  public void setEndpoint(Endpoint endpoint) {
    this.endpoint = endpoint;
  }

  private void init() {

  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    layoutEnvironmentConfig = findViewById(R.id.layout_environment_config);
    layoutLogPrint = findViewById(R.id.layout_log_print);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    float width = Resources.getSystem().getDisplayMetrics().widthPixels * 9.0f / 10.0f;
    layoutEnvironmentConfig.getLayoutParams().width = (int) width;
    layoutLogPrint.getLayoutParams().width = (int) width;

    layoutEnvironmentConfig.setEndpoint(endpoint);
    layoutEnvironmentConfig.setLumberYard(lumberYard);
    layoutLogPrint.setLumberYard(lumberYard);
  }
}
