package dto;

import model.User;

import java.io.Serializable;
import java.util.Set;

public class OnlineUsersDto implements Serializable {

    private Set<UserDto> onlineUsers;

    public OnlineUsersDto() {}

    public OnlineUsersDto(Set<UserDto> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public Set<UserDto> getOnlineUsers() {
        return onlineUsers;
    }

    @Override
    public String toString() {
        return "OnlineUsersDto{" +
                "onlineUsers=" + onlineUsers +
                '}';
    }

    public void setOnlineUsers(Set<UserDto> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
