package com.honvay.cola.cloud.framework.util;

import java.util.Map;

/**
 * Created by gangsun on 2017/6/21.
 */
public class MapUtils {

  public static <K, V> Map<String, V> populateMap(Map<String, V> map, Object... data) {
    for (int i = 0; i < data.length; ) {
      map.put((String) data[i++], (V) data[i++]);
    }
    return map;
  }

}
