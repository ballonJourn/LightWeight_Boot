package com.dylan.rpc.exchange;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = -7845678377043292305L;

    /**
     * 是否解码失败
     */
    private boolean broken = false;

    private Object requestData;

    public Object getRequestData() {
        return this.requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

}
