package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userGireesh = new User("pareekg008@gmail.com", "password", "Gireesh", "Pareek");
        userGireesh.addRole(roleAdmin);

        User savedUser = userRepository.save(userGireesh);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles() {
        User userGopal = new User("gopalpareek960@gmail.com", "password", "Gopal", "Pareek");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        userGopal.addRole(roleEditor);
        userGopal.addRole(roleAssistant);

        User savedUser = userRepository.save(userGopal);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<User> usersList = new ArrayList<>();
        users.forEach(usersList::add);
        usersList.forEach(user -> {
            System.out.println(user);
        });
        assertThat(usersList.size()).isGreaterThan(0);
    }

    @Test
    public void testGetUserById() {
        User fetchedUser = userRepository.findById(1).get();
        assertThat(fetchedUser).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User user = userRepository.findById(1).get();
        user.setEnabled(true);
        user.setEmail("firstnamegireeshpareek@rocketmail.com");

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = userRepository.findById(2).get();
        Role roleEditor = new Role(3);
        Role salesPerson = new Role(2);
        user.getRoles().remove(roleEditor);
        user.getRoles().add(salesPerson);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testDeleteUserById() {
        Integer userId = 9;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "firstnamegireeshpareek@rocketmail.com";
        User user = userRepository.findByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> userList = page.getContent();
        userList.forEach(user -> System.out.println(user));
        assertThat(userList.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyword,pageable);

        List<User> userList = page.getContent();
        userList.forEach(user -> System.out.println(user));

        assertThat(userList).size().isGreaterThan(0);
    }
}
