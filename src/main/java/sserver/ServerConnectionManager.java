package sserver;

import model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerConnectionManager {

    private final ThreadPoolExecutor executor;
    private final Set<ServerConnection> manager;

    public ServerConnectionManager() {
        executor = new ThreadPoolExecutor(
                10,
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8)
        );

//        manager = new ArrayList<>();
        manager = new HashSet<>();
    }

    public boolean add(ServerConnection connection) {
        if(manager.contains(connection)) return false;

        manager.add(connection);
        executor.execute(connection);

        return true;
    }

    public boolean remove(ServerConnection connection) {
        if(!manager.contains(connection)) return false;
        manager.remove(connection);
        return true;
    }

    public ServerConnection findWithUser(User user) {
        for(ServerConnection sc: manager) {
            if(sc.user.equals(user)) return sc;
        }
        return null;
    }

    public Set<ServerConnection> getManager() {
        return manager;
    }
}
