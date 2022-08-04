package com.shade.dev.action.actionhandler;

import com.shade.dev.Connection;
import com.shade.dev.Server;

/**
 * <p>Interface to the handle interactions between the client and the server/p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public interface ActionHandler {
    void handle(Server server, Connection connection, String message);
}
