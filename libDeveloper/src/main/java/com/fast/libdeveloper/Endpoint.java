package com.fast.libdeveloper;

import java.util.List;


public interface Endpoint {
  boolean isCustom(int index);

  boolean isMock(int index);

  int count();

  String name(int index);

  String url(int index);

  List<ExtraUrl> extraUrls(int index);

  void changeIndex(int index);
}
