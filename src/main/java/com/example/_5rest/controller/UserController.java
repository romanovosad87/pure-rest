package com.example._5rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example._5rest.dto.NoteRequestDto;
import com.example._5rest.dto.UserMapper;
import com.example._5rest.dto.UserRequestDto;
import com.example._5rest.dto.UserResponseDto;
import com.example._5rest.model.User;
import com.example._5rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    public static final String SELF = "self";
    public static final String GET_USER = "getUser";
    public static final String ADD_NOTE = "addNote";
    public static final String REMOVE_USER = "removeUser";
    public static final String SAVE_USER = "saveUser";
    public static final String GET_ALL_USERS = "getAllUsers";
    public static final String BASE_URL = "http://localhost:8080/users";
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping(value = "/all", produces = {"application/json"})
    public ResponseEntity<CollectionModel<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> dtos = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .peek(dto -> {
                    String id = dto.getId().toString();
                    Link saveUserLink = getSaveUserLink(SAVE_USER);
                    Link removeUserLink = getRemoveUserLink(id, REMOVE_USER);
                    Link addNoteLink = getAddNotesLink(id, ADD_NOTE);
                    Link getUser = getUserLink(id, GET_USER);
                    dto.add(saveUserLink, getUser, removeUserLink, addNoteLink);
                })
                .toList();

        Link selfLink = getAllUsersLink(SELF);
        CollectionModel<UserResponseDto> model = CollectionModel.of(dtos, selfLink);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserRequestDto dto) {
        User user = userMapper.toModel(dto);
        User savedUser = userService.save(user);
        String id = savedUser.getId().toString();
        UserResponseDto responseDto = userMapper.toDto(savedUser);
        Link selfLink = getSaveUserLink(SELF);
        Link getUserLink = getUserLink(id, GET_USER);
        Link removeUserLink = getRemoveUserLink(id, REMOVE_USER);
        Link allUsersLink = getAllUsersLink(GET_ALL_USERS);
        Link addNotesLink = getAddNotesLink(id, ADD_NOTE);
        responseDto.add(selfLink, getUserLink, removeUserLink, allUsersLink, addNotesLink);
        URI location = getUri(user);
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
        User user = userService.get(UUID.fromString(id));
        UserResponseDto dto = userMapper.toDto(user);
        Link selfLink = getUserLink(id, SELF);
        Link removeUserLink = getRemoveUserLink(id, REMOVE_USER);
        Link allUsersLink = getAllUsersLink(GET_ALL_USERS);
        Link addNotesLink = getAddNotesLink(id, ADD_NOTE);
        dto.add(selfLink, removeUserLink, allUsersLink, addNotesLink);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PostMapping("/{id}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> addUserNotes(@PathVariable String id,
                                                        @RequestBody NoteRequestDto dto) {
        User user = userMapper.toModel(dto, id);
        User savedUser = userService.save(user);
        UserResponseDto responseDto = userMapper.toDto(savedUser);
        Link selfLink = getAddNotesLink(savedUser.getId().toString(), SELF);
        Link userLink = getUserLink(id, GET_USER);
        Link allUsersLink = getAllUsersLink(GET_ALL_USERS);
        Link removeUserLink = getRemoveUserLink(id, REMOVE_USER);
        responseDto.add(selfLink, userLink, allUsersLink, removeUserLink);
        URI location = getUri(savedUser);
        return ResponseEntity.created(location).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        userService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private URI getUri(User user) {
        return ServletUriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{id}")
                .buildAndExpand(user.getId().toString())
                .toUri();
    }

    private Link getAllUsersLink(String rel) {
        WebMvcLinkBuilder builder = linkTo(methodOn(UserController.class)
                .getAllUsers());
        return getLink(rel, builder);
    }

    private Link getSaveUserLink(String rel) {
        WebMvcLinkBuilder builder = linkTo(methodOn(UserController.class)
                .saveUser(null));
        return getLink(rel, builder);
    }

    private Link getRemoveUserLink(String id, String rel) {
        WebMvcLinkBuilder builder = linkTo(methodOn(UserController.class)
                .remove(id));
        return getLink(rel, builder);
    }

    private Link getAddNotesLink(String id, String rel) {
        WebMvcLinkBuilder builder = linkTo(methodOn(UserController.class)
                .addUserNotes(id, null));
        return getLink(rel, builder);
    }

    private Link getUserLink(String id, String rel) {
        WebMvcLinkBuilder builder = linkTo(methodOn(UserController.class)
                .getUser(id));
        return getLink(rel, builder);
    }

    private static Link getLink(String rel, WebMvcLinkBuilder builder) {
        return rel.equals(SELF) ? builder.withSelfRel() : builder.withRel(rel);
    }
}

