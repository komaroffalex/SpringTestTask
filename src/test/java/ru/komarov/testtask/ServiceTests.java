package ru.komarov.testtask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.komarov.testtask.entity.User;
import ru.komarov.testtask.entity.UserStatus;
import ru.komarov.testtask.service.ServiceImpl;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTests {

    @Autowired
    ServiceImpl facade;

    @Test
    public void upsertUserTest() {
        assertThat(facade.upsertUser("alex", "komarov", "mail", "777")).isPositive();
    }

    @Test
    public void getUserTest() {
        User testUser = new User();
        testUser.setFirstName("alex");
        testUser.setLastName("komarov");
        testUser.setEmail("mail");
        testUser.setPhone("777");
        Long userId = facade.upsertUser(testUser.getFirstName(), testUser.getLastName(),
                testUser.getEmail(), testUser.getPhone());
        User getUser = facade.findUser(userId.toString());
        assertThat(getUser.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(getUser.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(getUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(getUser.getPhone()).isEqualTo(testUser.getPhone());
    }

    @Test
    public void getUserStatusTest() {
        User testUser = new User();
        testUser.setFirstName("alex");
        testUser.setLastName("komarov");
        testUser.setEmail("mail");
        testUser.setPhone("777");
        Long userId = facade.upsertUser(testUser.getFirstName(), testUser.getLastName(),
                testUser.getEmail(), testUser.getPhone());
        assertThat(facade.getStatus(userId.toString()).toString()).isEqualTo("OFFLINE");
    }

    @Test
    public void changeUserStatusTest() {
        User testUser = new User();
        testUser.setFirstName("alex");
        testUser.setLastName("komarov");
        testUser.setEmail("mail");
        testUser.setPhone("777");
        Long userId = facade.upsertUser(testUser.getFirstName(), testUser.getLastName(),
                testUser.getEmail(), testUser.getPhone());
        facade.setStatus(userId.toString(), UserStatus.ONLINE);
        assertThat(facade.getStatus(userId.toString()).toString()).isEqualTo("ONLINE");
    }
}
