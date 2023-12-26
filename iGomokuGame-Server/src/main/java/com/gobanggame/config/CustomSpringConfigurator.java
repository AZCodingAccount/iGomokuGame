package com.gobanggame.config;

import jakarta.websocket.server.ServerEndpointConfig;

/*
* websocket里面获取bean的配置
* */
public class CustomSpringConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return ApplicationContextProvider.getApplicationContext().getBean(clazz);
    }
}
