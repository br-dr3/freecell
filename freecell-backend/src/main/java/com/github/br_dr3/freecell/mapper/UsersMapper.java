package com.github.br_dr3.freecell.mapper;

import com.github.br_dr3.freecell.gateway.dto.UserDTO;
import com.github.br_dr3.freecell.repositories.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public UserDTO toUserDTO(User savedUser) {
        return UserDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .build();
    }
}
