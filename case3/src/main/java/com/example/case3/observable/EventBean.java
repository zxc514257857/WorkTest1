package com.example.case3.observable;

import java.util.Arrays;

public class EventBean<T> {

    private int code;
    private T[] data;

    public EventBean(int code) {
        this.code = code;
    }

    @SafeVarargs
    public EventBean(int code, T... data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "code=" + code +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}