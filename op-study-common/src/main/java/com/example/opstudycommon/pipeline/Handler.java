package com.example.opstudycommon.pipeline;

/**
 * @author xxs
 * @Date 2024/6/28 22:39
 * handler
 */
public interface Handler<I,O>{

    /**
     * handler
     * input -> output
     * @param i
     * @return
     */
    O handler(I i);
}
