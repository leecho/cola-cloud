/**
 * Copyright (c) 2011-2014, L.cm (596392912@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.honvay.cola.cloud.framework.util;

import com.honvay.cola.cloud.framework.util.bean.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * <p> 基于CGlib，扩展BeanUtils，对于复杂类型的CGlib更有优势 </p>
 *
 * @author L.cm
 * @date 2016-04-15
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils {

  protected BeanUtils() {
    /* 保护 */
  }

  /**
   * 实例化对象
   *
   * @param clazz 类
   * @return 对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T newInstance(Class<?> clazz) {
    try {
      return (T) clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * 实例化对象
   *
   * @param clazzStr 类名
   * @return {T}
   */
  public static <T> T newInstance(String clazzStr) {
    try {
      Class<?> clazz = Class.forName(clazzStr);
      return newInstance(clazz);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * copy 对象属性到另一个对象，默认不使用Convert
   *
   * @param clazz 类名
   * @return {T}
   */
  public static <T> T copy(Object src, Class<T> clazz) {
    BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);
    T to = newInstance(clazz);
    copier.copy(src, to, null);
    return to;
  }


  /**
   * 拷贝对象
   *
   * @param src 源对象
   * @param dist 需要赋值的对象
   */
  public static void copy(Object src, Object dist) {
    BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), false);
    copier.copy(src, dist, null);
  }


  /**
   * 将对象装成map形式 注意：生成的是unmodifiableMap
   *
   * @param src 源对象
   * @return {Map<K, V>}
   */
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> toMap(Object src) {
    return BeanMap.create(src);
  }

  /**
   * 复制bean属性,忽略null属性
   */
  public static void copyPropertiesIgnoreNull(Object from,Object to){
      copyProperties(from,to,new NullAwareBeanUtilsBean());
  }

  /**
   * 复制bean，不过滤null属性
   * @param from
   * @param to
   */
  public static void copyPropertiesAllowNull(Object from,Object to){
      copyProperties(from,to,new BeanUtilsBean());
  }

  private static void copyProperties(Object from,Object to,BeanUtilsBean bub){
      try {
          bub.copyProperties(to,from);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }
}
