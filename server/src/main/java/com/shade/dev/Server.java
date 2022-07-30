package com.shade.dev;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Server program that manages clients, server runs on port 9999 by default</p>
 *
 * @author Shade
 * @version 0.0.1
 * @since 2022/07
 */
public class Server implements Runnable {

    /**
     * <p>Arraylist for storing client connections</p>
     */
    @Getter
    @Setter
    private ArrayList<Connection> connections;
    /**
     * ServerSocket to be instantiated
     */
    private ServerSocket server;
    /**
     * Server running state
     */
    private boolean serverDown;
    /**
     * ThreadPool for client connections
     */
    private ExecutorService threadPool;

    /**
     * <h3>NoArgsConstructor</h3>
     * <p>Initializing local variables connections and serverDown</p>
     */
    public Server() {
        this.connections = new ArrayList<>();
        this.serverDown = false;
    }

    /**
     * <p>The method which will be executed when we instantiate an Object of Server</p>
     * <p>The methods does the following: </p>
     * <ul>
     *     <li>Instantiate ServerSocket on a port</li>
     *     <li>Listens for client connections</li>
     *     <li>Accepts client connections</li>
     *     <li>Adds connections to connection list</li>
     *     <li>Executes Connection class from threadPool</li>
     * </ul>
     */
    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            threadPool = Executors.newCachedThreadPool();

            while (!serverDown) {
                Socket client = server.accept();
                Connection clientConnection = new Connection(client, this);
                connections.add(clientConnection);
                threadPool.execute(clientConnection);
            }
        } catch (IOException e) {
            shutdownServer();
        }
    }

    /**
     * <p>Method for closing the server properly</p>
     *
     * @throws IOException if an I/0 error occurs when closing the {@link ServerSocket}
     */
    public void shutdownServer() {
        serverDown = true;

        if (!server.isClosed()) {
            try {
                server.close();
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "Cannot shutdown Server");
            }
        }

        connections.forEach(Connection::shutdownClient);
    }

    /**
     * <p> Method for sending a message to all connected clients</p>
     *
     * @param message The message to be sent to all other clients
     */
    public void broadcast(String message) {
        if (Objects.nonNull(message)) {
            connections.forEach(connection -> connection.sendMessage(message));
        }
    }
}
