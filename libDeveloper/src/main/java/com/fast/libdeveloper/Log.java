package com.fast.libdeveloper;


final class Log {
  private final int level;
  private final String tag;
  private final String message;
  boolean sent;

  public Log(int level, String tag, String message) {
    this.level = level;
    this.tag = tag;
    this.message = message;
  }

  public int level() {
    return level;
  }

  public String levelString() {
    switch (level) {
      case android.util.Log.VERBOSE:
        return "V";
      case android.util.Log.DEBUG:
        return "D";
      case android.util.Log.INFO:
        return "I";
      case android.util.Log.WARN:
        return "W";
      case android.util.Log.ERROR:
        return "E";
      case android.util.Log.ASSERT:
        return "A";
      default:
        return "?";
    }
  }

  public String tag() {
    return tag;
  }

  public String rawMessage() {
    return message;
  }

  public String message() {
    return String.format("%22s %s %s", tag, levelString(),
        /* Indent newlines to match the original indentation. */
        message.replaceAll("\\n", "\n                         "));
  }
}
