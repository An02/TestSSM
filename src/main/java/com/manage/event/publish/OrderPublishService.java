package com.manage.event.publish;

public interface OrderPublishService<T> {
    void publishEvent(T event);
}
