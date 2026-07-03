package me.decce.kerria;

import com.mojang.blaze3d.pipeline.RenderCall;

import java.util.function.Consumer;

public class DeferredRenderCall<T> implements RenderCall {
    private final Consumer<T> consumer;
    private final T value;

    public DeferredRenderCall(Consumer<T> consumer, T value) {
        this.consumer = consumer;
        this.value = value;
    }

    @Override
    public void execute() {
        consumer.accept(value);
    }
}
