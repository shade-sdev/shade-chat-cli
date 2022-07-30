package com.shade.dev;

import com.shade.dev.utils.ShadeLogger;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;

/**
 * <p>Connection Class which manages the interactions between the client and server</p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public class Connection implements Runnable {

    /**
     * <p>Client</p>
     */
    @Getter
    private final Socket client;

    /**
     * <p>Server for accessing connections</p>
     */
    private final Server server;
    /**
     * <p>Readers & Writers</p>
     */
    private final BufferedReader in;
    private final PrintWriter out;
    /**
     * <p>Client's nickname</p>
     */
    @Getter
    private String nickname = "newClient";

    private final ShadeLogger shadeLogger;

    /**
     * <p>Constructor</p>
     *
     * @param client The Socket client
     */
    public Connection(Socket client, Server server) throws IOException {
        this.client = client;
        this.shadeLogger = new ShadeLogger(Connection.class.getName());
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.server = server;
    }

    /**
     * Method to be executed when an Object of Connection is instantiated.
     * Handles interactions between the {@link Server} & {@link #client}
     */
    @Override
    public void run() {
        try {

            setNickname();
            server.broadcast(nickname + " joined the chat.");

            String message;
            while ((message = in.readLine()) != null) {

                if (message.startsWith("/quit")) {
                    removeClientFromConnectionList();
                } else {
                    server.broadcast(nickname + ": " + message);
                }

            }
        } catch (IOException e) {
            shutdownClient();
        }
    }

    /**
     * <p>Method to send messages to the client</p>
     *
     * @param message Message to be sent to the client
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * <p>Method to shut down the client properly</p>
     */
    public void shutdownClient() {
        try {
            in.close();
            out.close();

            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            shadeLogger.log(INFO, "Cannot shutdown Client");
        }

    }

    /**
     * <p>Method for validating nickname</p>
     */
    private void setNickname() {

        boolean nicknameValid = false;

        while (!nicknameValid) {

            out.println("Please enter a nickname");

            try {
                String nicknameCandidate = in.readLine();
                boolean nicknameUnique = server.getConnections().stream()
                        .filter(connection -> !connection.getClient().isClosed())
                        .anyMatch(connection -> Objects.equals(connection.nickname, nicknameCandidate));

                if (Objects.nonNull(nicknameCandidate) && !nicknameUnique) {
                    nicknameValid = true;
                    nickname = nicknameCandidate;
                    shadeLogger.log(INFO, String.format("%s connected", nickname));
                }
            } catch (IOException e) {
                shutdownClient();
            }


        }
    }

    private void removeClientFromConnectionList() {
        server.broadcast(nickname + " left the chat.");
        server.setConnections(server.getConnections().stream()
                .filter(connection -> !connection.getNickname().equals(nickname))
                .collect(Collectors.toCollection(ArrayList::new)));
    }
}
