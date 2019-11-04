package com.zcf.web.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeferredResultHolder {
    private Map<String, DeferredResult> map = new HashMap<>();

    public Map<String, DeferredResult> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult> map) {
        this.map = map;
    }
}
