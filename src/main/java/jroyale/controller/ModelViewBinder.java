package jroyale.controller;

import java.util.HashMap;
import java.util.Map;

public class ModelViewBinder<M, V> {
    private Map<M,V> modelViewBinder = new HashMap<>();

    public void bind(M modelClass, V view) {
        modelViewBinder.put(modelClass, view);
    }

    public V getViewTroopType(M modelClass) {
        return modelViewBinder.get(modelClass);
    }
}
