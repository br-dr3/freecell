package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.dto.RequestUserCreationDTO;
import com.github.br_dr3.freecell.gateway.dto.UserDTO;
import com.github.br_dr3.freecell.repositories.UserRepository;
import com.github.br_dr3.freecell.repositories.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UsersMapper usersMapper;
    public UserDTO createUser(RequestUserCreationDTO requestUserCreation) {
        try {
            var user = User.builder()
                    .name(requestUserCreation.getName())
                    .build();
            var savedUser = userRepository.save(user);
            return usersMapper.toUserDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserNotCreatedException("User already exists");
        }
    }

    public UserDTO getUser(Long id) {
        var user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found by id '" + id + "'.");
        }
        return usersMapper.toUserDTO(user.get());
    }
}
