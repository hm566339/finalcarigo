package com.carigo.dashboard.component;

import org.springframework.stereotype.Component;

import com.carigo.dashboard.client.UserClient;

@Component
public class UserClientFallback implements UserClient {
    public long countAll() {
        return 0;
    }

    @Override
    public long countBlocked() {
        return 0;

    }
}
