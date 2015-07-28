package com.tutorial.deeplayer.app.deeplayer.utils;

public interface LeakTracing {

    void init();

    void traceLeakage(Object reference);

}