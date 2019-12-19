package com.lrouter.core;

import android.net.Uri;
import android.os.Bundle;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  @Author: lqc
 */
public class UriCompact {

    /**
     * 获取uri 参数
     * @param uri
     * @return 参数集合
     */
    public static Set<String> getQueryParameterNames(Uri uri) {
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));
            // Move start to end of name.
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }

    /**
     * 根据URI解析数据放到bundle中
     * @param uri 数据URI
     * @return {@link Bundle}
     */
    public static Bundle parseExtras(Uri uri) {
        Bundle bundle = new Bundle();
        // parameter
        Set<String> names = UriCompact.getQueryParameterNames(uri);
        for (String name : names) {
            String value = uri.getQueryParameter(name);
            bundle.putString(name,value);
        }
        return bundle;
    }

}
