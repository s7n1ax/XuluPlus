/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.util;

public class OyPair<T, S> {
    T key;
    S value;

    public OyPair(T key, S value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return this.key;
    }

    public S getValue() {
        return this.value;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void setValue(S value) {
        this.value = value;
    }

    public T getFirst() {
        return this.key;
    }

    public void setFirst(T f) {
        this.key = f;
    }

    public S getSecond() {
        return this.value;
    }

    public void setSecond(S s) {
        this.value = s;
    }
}

