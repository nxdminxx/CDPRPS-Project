package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final SHSRDAO<User> userRepository;

    public UserService(SHSRDAO<User> userRepository) {
        this.userRepository = userRepository;
    }

    public String updateUser(User user) throws ExecutionException, InterruptedException {
        return userRepository.update(user);
    }

    public String createUser(User user) throws ExecutionException, InterruptedException {
        //to check whether userId is taken or not
        List<User> userList = userRepository.getAll();
        for(User user1:userList){
            if(user.getUserId().equals(user1.getUserId())){
                return "Failed to create user with id " + user.getUserId() + ",please choose another Id";
            }
        }
        return userRepository.save(user);
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        return userRepository.get(userId);
    }

    public List<User> getUserList() throws ExecutionException, InterruptedException {
        return userRepository.getAll();
    }

    public String deleteUser(String userId) throws ExecutionException, InterruptedException {
        return userRepository.delete(userId);
    }

    public List<User> getAdminList() throws ExecutionException, InterruptedException {
        List<User> userList = userRepository.getAll();
        for(int i = userList.size()-1; i >= 0; i--){
            if(!userList.get(i).getRole().equals("ADMIN")){
                userList.remove(i);
            }
        }
        return userList;
    }
}
