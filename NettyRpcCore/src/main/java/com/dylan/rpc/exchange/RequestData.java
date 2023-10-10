package com.dylan.rpc.exchange;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class RequestData implements Serializable {

    private static final long serialVersionUID = -2133278172077516380L;

    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] parameters;
    private String version = "";
    private String group = "";

    public String getRpcServiceName() {
        return interfaceName + group + version;
    }
}
