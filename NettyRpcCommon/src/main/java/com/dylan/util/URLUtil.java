package com.dylan.util;


import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;


public class URLUtil {

    private URLUtil() {
    }

    public static String fullURL(String endpoint, String uri) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(endpoint), "Endpoint can't be null");
        Preconditions.checkArgument(StringUtils.isNoneBlank(uri), "URI can't be null");
        if (endpoint.endsWith("/")) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
                return endpoint + uri;
            }
            return endpoint + uri;
        }
        if (uri.startsWith("/")) {
            return endpoint + uri;
        }

        return endpoint + "/" + uri;
    }

}
