package server;


import utilities.Constants;
import utilities.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerConnectionPool {

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

        try {
            while (!connection.available()) {
                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public ServerConnection findWithUserID(int userID) {
        for(ServerConnection sc: connectionManager) {
            if(sc.user.getId() == userID) return sc;
        }
        return null;
    }

    public void broadcastOnlineUsers() {
        System.out.println("BROADCAST EVENT - online users");
        String data = Helper.pack(Constants.ONLINE_USERS_EVENT,
                new ArrayList<String>(connectionManager.stream().map(sc -> sc.user.getId()+"").toList()));
        for(ServerConnection sc : connectionManager) {
            sc.sendData(data);
        }
    }
}