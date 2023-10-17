package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public List<Role> getAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.findByEmail(email);
        if(userByEmail == null) return true;
        boolean isCreatingNew = (id == null);
        if(isCreatingNew){
            if(userByEmail != null) return false;
        }
        else {
            if(userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User getUserById(Integer userId) throws UserNotFoundException{
        try{
            return userRepository.findById(userId).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID "+ userId);
        }
    }

    public void deleteUserById(Integer id) throws UserNotFoundException{
        Long countById = userRepository.countById(id);

        if(countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID "+ id);
        }

        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id, enabled);
    }
}
