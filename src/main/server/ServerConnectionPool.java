package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerConnectionPool {

    ChatServer server;
    ThreadPoolExecutor executor;
    public ArrayList<ServerConnection> connectionManager;

    public ServerConnectionPool(ChatServer server) throws IOException {
        executor = new ThreadPoolExecutor(
                10,
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8)
        );

        connectionManager = new ArrayList<>();
    }

    public boolean add(ServerConnection connection) {
        if(connectionManager.contains(connection)) return false;

        connectionManager.add(connection);
        executor.execute(connection);

        return true;
    }

    public boolean remove(ServerConnection connection) {
        if(connectionManager.contains(connection)) {
            connectionManager.remove(connection);
            return true;
        }

        return false;
    }

    public ServerConnection findWithName(String name) {
        for(ServerConnection sc : connectionManager) {
            if(sc.clientName.equals(name)) return sc;
        }
        return  null;
    }
}
