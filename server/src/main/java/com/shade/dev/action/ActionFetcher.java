package com.shade.dev.action;

import java.util.Optional;

/**
 * <p>Methods to extract ActionType and any client message</p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public interface ActionFetcher {

    ActionType getActionType(String message);

    Optional<String> getClientMessage(String message);

}
