package com.sscc.common;

import java.util.Map;

import com.baomidou.mybatisplus.generator.InjectionConfig;

public abstract class InjectionConfigSupper extends InjectionConfig {
    
    private Map<String, Map<String,Object>> myMap;
    
    @Override
    public abstract void initMap();

    public Map<String, Object> getMap(String tableName) {
        // TODO Auto-generated method stub
        
        return myMap.get(tableName);
    }

    public Map<String, Map<String, Object>> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, Map<String, Object>> myMap) {
        this.myMap = myMap;
    }
}
