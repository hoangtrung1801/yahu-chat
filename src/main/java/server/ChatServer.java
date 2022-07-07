package server;

import com.formdev.flatlaf.FlatLightLaf;
import dto.ConversationDto;
import dto.UserDto;
import org.modelmapper.ModelMapper;
import shared.Constants;
import utility.HibernateUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatServer {

    public static ServerSocket server;
    public static ServerConnectionManager connectionManager;
    public static ChatServerGUI gui;

    private boolean isRunning;

    public ChatServer() {
            FlatLightLaf.setup();
            gui = new ChatServerGUI();
            gui.setListener(new ChatServerGUI.StatusListener() {
                @Override
                public void start() {
                    Thread thread = new Thread(ChatServer.this::initServer);
                    thread.start();
                }

                @Override
                public void stop() {
                    closeServer();
                }
            });
            gui.setVisible(true);
    }

    private void initServer() {
        try {
            HibernateUtils.getEntityManager();
            server = new ServerSocket(Constants.PORT);
            connectionManager = new ServerConnectionManager();
            isRunning = true;

            System.out.println("Server is opening at port " + Constants.PORT);
            gui.log("Server is opening at port " + Constants.PORT);

            while(isRunning) {
                Socket clientSocket = server.accept();
                ServerConnection connection = new ServerConnection(clientSocket);
                System.out.println("NEW CLIENT ENTERED");
                gui.log("NEW CLIENT ENTERED");

                connectionManager.add(connection);
                new Thread(connection).start();
            }
        } catch (Exception e) {
            isRunning = false;
            e.printStackTrace();
        }
    }

    public static void sendOnlineUsersEvent() {
        /*
            ONLINE_USERS_EVENT
            List<UserDto>
         */
        ModelMapper modelMapper = new ModelMapper();
        Set<UserDto> onlineUsers = connectionManager
                .getManager()
                .stream().map(serverConnection -> modelMapper.map(serverConnection.user, UserDto.class))
                .collect(Collectors.toSet());

        for(ServerConnection sc: connectionManager.getManager()) {
            sc.sendData(Constants.USER_LOGGED);
            sc.sendObject(onlineUsers);
        }
    }

    public static void sendListConversationsEvent() {
         /*
            LIST_CONVERSATIONS_EVENT
         */
        ModelMapper modelMapper = new ModelMapper();
        Set<UserDto> onlineUsers = connectionManager
                .getManager()
                .stream().map(serverConnection -> modelMapper.map(serverConnection.user, UserDto.class))
                .collect(Collectors.toSet());

        for(ServerConnection sc: connectionManager.getManager()) {
            sc.sendData(Constants.LIST_CONVERSATIONS_EVENT);

            List<ConversationDto> listConversations = new ArrayList<>();
        }
    }

    private void closeServer() {
        try {
            gui.log("Close server");
            for(ServerConnection sc: connectionManager.getManager()) sc.close();
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
