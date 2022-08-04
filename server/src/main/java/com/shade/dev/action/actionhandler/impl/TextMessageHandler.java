package com.shade.dev.action.actionhandler.impl;

import com.shade.dev.Connection;
import com.shade.dev.Server;
import com.shade.dev.action.actionhandler.ActionHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>Implementation of the ActionHandler interface for chat message</p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public class TextMessageHandler implements ActionHandler {

    @Override
    public void handle(Server server, Connection connection, String message) {
        if (Objects.nonNull(message) && StringUtils.isNoneBlank(message) && StringUtils.isNotEmpty(message)) {
            server.broadcast(String.format("%s: %s", connection.getNickname(), message));
        }
    }
}
