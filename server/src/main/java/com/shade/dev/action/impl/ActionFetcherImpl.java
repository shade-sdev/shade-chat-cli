package com.shade.dev.action.impl;

import com.shade.dev.action.ActionFetcher;
import com.shade.dev.action.ActionType;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public class ActionFetcherImpl implements ActionFetcher {

    /**
     *
     * @param message Client's message
     * @return Valid enum value of {@link ActionType}
     */
    @Override
    public ActionType getActionType(String message) {
        int index = StringUtils.ordinalIndexOf(message, "_", 2);
        return ActionType.valueOf(message.substring(0, (index + 5)));
    }

    /**
     * @param message Client's message
     * @return Client's actual message if any
     */
    @Override
    public Optional<String> getClientMessage(String message) {
        int index = StringUtils.ordinalIndexOf(message, "_", 3);
        return Optional.of(message.substring(index + 1));
    }
}
