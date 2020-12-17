package com.oscill.controller;

import androidx.annotation.NonNull;

public abstract class OscillProperty<V> extends BaseOscillProperty<Integer,V> {

    public OscillProperty(@NonNull Oscill oscill) {
        super(oscill);
    }
}
