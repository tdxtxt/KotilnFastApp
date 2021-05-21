package com.fast.libdeveloper;


final class Report {
  public final String email;
  public final String title;
  public final String description;
  public final boolean includeScreenshot;
  public final boolean includeLogs;

  private Report(Builder builder) {
    email = builder.email;
    title = builder.title;
    description = builder.description;
    includeScreenshot = builder.includeScreenshot;
    includeLogs = builder.includeLogs;
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private String email;
    private String title;
    private String description;
    private boolean includeScreenshot;
    private boolean includeLogs;

    private Builder() {
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder includeScreenshot(boolean includeScreenshot) {
      this.includeScreenshot = includeScreenshot;
      return this;
    }

    public Builder includeLogs(boolean includeLogs) {
      this.includeLogs = includeLogs;
      return this;
    }

    public Report build() {
      return new Report(this);
    }
  }
}
