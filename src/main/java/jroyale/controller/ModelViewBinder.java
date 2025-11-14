package jroyale.controller;

import java.util.HashMap;
import java.util.Map;

public class ModelViewBinder<M, V> {
    private Map<M,V> modelViewBinder = new HashMap<>();

    public void bind(M model, V view) {
        modelViewBinder.put(model, view);
    }

    public V getViewInstance(M model) {
        return modelViewBinder.get(model);
    }
}
