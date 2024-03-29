package ru.komarov.testtask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.komarov.testtask.entity.User;
import ru.komarov.testtask.entity.UserStatus;
import ru.komarov.testtask.service.ServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@RestController
@RequestMapping
public class UserController {

    private ServiceImpl service;

    @Autowired
    public void setService(@NotNull final ServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public String list() {
        return "index";
    }

    /**
     * Change user's status.
     *
     * @param id user's ID
     * @param status user's new status
     * @return user's id, old status and new status
     */
    @RequestMapping(value = "/user/status", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> changeUserStatus(@RequestParam String id, @RequestParam String status) {
        if (null == id || null == status) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            UserStatus oldStatus = service.getStatus(id);
            service.setStatus(id, UserStatus.valueOfString(status));
            String resp = "{\"id\":" + id + ",\"old_status\":" + "\"" +oldStatus.toString() + "\"" + ",\"new_status\":"
                    + "\"" + UserStatus.valueOfString(status).toString() + "\"" + "}";
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get user by the specified ID.
     *
     * @param id user's ID
     * @return user's info
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> getUserById(@RequestParam String id) {
        if (null == id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(service.findUser(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Upsert new user into the DB.
     *
     * @param firstname user's first name
     * @param lastname user's last name
     * @param email user's email
     * @param phone user's phone number
     * @return user's id
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> upsertNewUser(@RequestParam String firstname, @RequestParam String lastname,
                                                @RequestParam String email, @RequestParam String phone) {
        if (null == firstname || null == lastname || null == email || null == phone) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Long newId = service.upsertUser(firstname, lastname, email, phone);
        final String resp = "{\"id\":" + newId.toString() + "}";
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
}
