package com.honvay.cola.cloud.framework.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * CollectionUtils
 *
 * @author YRain
 * @createtime 12/29/17
 */
public abstract class CollectionUtils {

    public static List extractToList(Collection collection, String propertyName) {
        ArrayList list = new ArrayList(collection.size());
        try {
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
