package com.fightcent.imagepicker.util;

import java.util.Collection;

/**
 * Created by andy.guo on 2016/10/10.
 * 集合处理功能类
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static int size(Collection<?> list) {
        if (CollectionUtil.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

}
