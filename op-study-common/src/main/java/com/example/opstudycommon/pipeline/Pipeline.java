package com.example.opstudycommon.pipeline;

/**
 * @author xxs
 * @Date 2024/6/28 22:40
 * Pipeline
 */
public class Pipeline<I,O> {

    private final Handler<I,O> currentHandler;

    public Pipeline(Handler<I,O> currentHandler) {
        this.currentHandler = currentHandler;
    }

    public <V> Pipeline<I,V> addHandler(Handler<O,V> handler) {
        return new Pipeline<>(input -> handler.handler(currentHandler.handler(input)));
    }

    O execute(I input) {
        return currentHandler.handler(input);
    }

}
