package com.dylan.rpc.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class RpcProperties {
    private static final Properties prop = new Properties();
    String CONFIG_PATH = "";
    public  RpcProperties(){
        URL configURL = null;
        try {
            configURL = ClassLoader.getSystemResource(CONFIG_PATH);
        } catch (Exception e) {
//            log.info("rpc properties: {} is not exist", CONFIG_PATH);
        }
        if (configURL != null) {
            try (InputStream inputStream = configURL.openStream()) {
                prop.load(inputStream);
                System.out.println(prop.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static String getParameter(String key) {
        return prop.getProperty(key);
    }

    public static String getParameter(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public static int getParameter(String key, int defaultValue) {
        String v = getParameter(key);
        return StringUtils.isBlank(v) ? defaultValue : Integer.parseInt(v);
    }
}
