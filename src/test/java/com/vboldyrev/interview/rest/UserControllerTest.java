package com.vboldyrev.interview.rest;

import com.vboldyrev.interview.RsaTaskApp;
import com.vboldyrev.interview.dto.User;
import com.vboldyrev.interview.repository.UserRepository;
import com.vboldyrev.interview.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RsaTaskApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    final String USER_NAME = "vlad";
    final String URL = "/users/" + USER_NAME + "/last_access_date";
    final int ELEVEN_SECONDS = 11;

    @Test
    public void testUserWrite() {
        clearCacheAndDeleteUser();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        Optional<User> user = userRepository.findById(USER_NAME);
        assertTrue(user.isPresent());
    }

    @Test
    public void testInitialUserWrite() {
        clearCacheAndDeleteUser();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        Optional<String> oUserLastAccessDate = Optional.ofNullable(response.getBody());
        assertEquals("", oUserLastAccessDate.orElse(""));
    }

    @Test
    public void testSameCachedTime() {
        clearCacheAndDeleteUser();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        ResponseEntity<String> response1 = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        assertEquals(response1, response2);
    }

    @Test
    public void testDifferentTime() {
        clearCacheAndDeleteUser();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        ResponseEntity<String> response1 = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        delay(ELEVEN_SECONDS);
        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        assertNotEquals(response1, response2);
    }

    @Test
    public void testCashedTimeDifferentFromDatabaseTime(){
        clearCacheAndDeleteUser();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(URL),
                HttpMethod.GET, entity, String.class);
        String databaseDate = userRepository.findById(USER_NAME).map(User::getDate)
                .map(date -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date)).orElse("");
        String serviceDate = response.getBody();
        assertNotEquals(databaseDate, serviceDate);
    }

    private void clearCacheAndDeleteUser() {
        userRepository.delete(new User(USER_NAME, null));
        userService.clearUserCacheByName(USER_NAME);
    }

    private void delay(int sec) {
        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException ignored) {}
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}