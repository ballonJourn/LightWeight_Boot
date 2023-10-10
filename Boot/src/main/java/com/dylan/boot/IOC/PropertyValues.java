package com.dylan.boot.IOC;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    public List<PropertyValue> propertyValueList = new ArrayList<>();
//    new PropertyValues(String s); 像这样构造函数传参的时候才需要public  PropertyValues
//    public  PropertyValues(PropertyValue pv){
//    }
    public void addPropertyValue(PropertyValue pv){
        this.propertyValueList.add(pv);
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }
}
