package com.honvay.cola.cloud.framework.util;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class SerializeUtils {

  private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

  /**
   * 反序列化
   */
  public static Serializable deserialize(byte[] bytes) {
    Serializable result = SerializationUtils.deserialize(bytes);
    logger.debug("deserialize : class=" + result.getClass().getName());
    return result;
  }

  public static boolean isEmpty(byte[] data) {
    return (data == null || data.length == 0);
  }

  /**
   * 序列化
   */
  public static byte[] serialize(Serializable object) {
    byte[] result = null;
    if (object != null) {
      result = SerializationUtils.serialize(object);
    } else {
      logger.error("serialize object is null");
    }
    logger.debug("serialize : result=" + result.length);
    return result;
  }
}
