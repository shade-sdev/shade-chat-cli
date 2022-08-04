package com.shade.dev;

import com.shade.dev.action.ActionFetcher;
import com.shade.dev.action.actionhandler.ActionHandler;
import com.shade.dev.action.actionhandler.impl.QuitServerHandler;
import com.shade.dev.action.actionhandler.impl.TextMessageHandler;
import com.shade.dev.action.impl.ActionFetcherImpl;
import com.shade.dev.exception.InvalidActionTypeException;

public class Message {

    private final ActionFetcher actionFetcher = new ActionFetcherImpl();
    private final String message;
    private final Server server;
    private final Connection connection;


    public Message(String message, Server server, Connection connection) {
        this.message = message;
        this.server = server;
        this.connection = connection;
    }

    public void handle() {
        switch (actionFetcher.getActionType(message)) {

            case TEXT_MESSAGE_TYPE:
                ActionHandler textMessageHandler = new TextMessageHandler();
                textMessageHandler.handle(server, connection, actionFetcher.getClientMessage(message).orElse(null));
                break;

            case IMAGE_MESSAGE_TYPE:
                break;
            case RENAME_NICK_TYPE:
                break;
            case QUIT_SERVER_TYPE:
                ActionHandler quitServerHandler = new QuitServerHandler();
                quitServerHandler.handle(server, connection, actionFetcher.getClientMessage(message).orElse(null));
                break;

            default:
                throw new InvalidActionTypeException("Invalid Action Type");
        }
    }
}
