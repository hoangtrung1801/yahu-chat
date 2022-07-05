package server;

import dto.ConversationDto;
import dto.UserDto;
import org.modelmapper.ModelMapper;
import utility.Constants;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatServer {

    public static ServerSocket server;
    public static ServerConnectionManager connectionManager;

    private boolean isRunning;

    public ChatServer() {
        initServer();
    }

    private void initServer() {
        try {
            server = new ServerSocket(Constants.PORT);
            connectionManager = new ServerConnectionManager();
            isRunning = true;

            System.out.println("Server is opening at port " + Constants.PORT);

            while(isRunning) {
                Socket clientSocket = server.accept();
                ServerConnection connection = new ServerConnection(clientSocket);
                System.out.println("NEW CLIENT ENTERED");

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
            sc.sendData(Constants.ONLINE_USERS_EVENT);
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

//    public static void sendDataTo

    public static void main(String[] args) {
        new ChatServer();
    }
}
