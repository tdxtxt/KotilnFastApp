package com.fast.libdeveloper;



public final class ExtraUrl {
  String description;
  String url;

  private ExtraUrl(Builder builder) {
    description = builder.description;
    url = builder.url;
  }

  public String description() {
    return description;
  }

  public String url() {
    return url;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String description;
    private String url;

    private Builder() {
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public ExtraUrl build() {
      return new ExtraUrl(this);
    }
  }
}
