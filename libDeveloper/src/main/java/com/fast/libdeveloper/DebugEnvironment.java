package com.fast.libdeveloper;

import java.util.List;


public class DebugEnvironment {
  public final int index;
  public final String name;
  public final String url;
  public final List<ExtraUrl> extraUrls;

  public static Builder builder() {
    return new Builder();
  }

  private DebugEnvironment(Builder builder) {
    index = builder.index;
    name = builder.name;
    url = builder.url;
    extraUrls = builder.extraUrls;
  }

  public static final class Builder {
    private int index;
    private String name;
    private String url;
    private List<ExtraUrl> extraUrls;

    private Builder() {
    }

    public Builder index(int index) {
      this.index = index;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder extraUrls(List<ExtraUrl> extraUrls) {
      this.extraUrls = extraUrls;
      return this;
    }

    public DebugEnvironment build() {
      return new DebugEnvironment(this);
    }
  }
}
