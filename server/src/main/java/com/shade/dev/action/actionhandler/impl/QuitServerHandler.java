package com.shade.dev.action.actionhandler.impl;

import com.shade.dev.Connection;
import com.shade.dev.Server;
import com.shade.dev.action.actionhandler.ActionHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * <p>Implementation of the ActionHandler interface when client closes the connection</p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public class QuitServerHandler implements ActionHandler {

    @Override
    public void handle(Server server, Connection connection, String message) {
        server.broadcast(connection.getNickname() + " left the chat.");
        server.setConnections(server.getConnections().stream()
                .filter(conn -> !conn.getNickname().equals(connection.getNickname()))
                .collect(Collectors.toCollection(ArrayList::new)));
    }
}
