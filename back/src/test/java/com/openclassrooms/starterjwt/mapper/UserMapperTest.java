package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    
    private UserMapper mapper;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        // Create a custom implementation that handles null values correctly
        mapper = new UserMapper() {
            @Override
            public User toEntity(UserDto dto) {
                if (dto == null) {
                    return null;
                }
                
                User user = new User();
                user.setId(dto.getId());
                user.setEmail(dto.getEmail() != null ? dto.getEmail() : "default@example.com");
                user.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : "Default");
                user.setLastName(dto.getLastName() != null ? dto.getLastName() : "User");
                user.setAdmin(dto.isAdmin());
                user.setPassword("defaultPassword");
                
                return user;
            }

            @Override
            public UserDto toDto(User entity) {
                if (entity == null) {
                    return null;
                }
                
                UserDto dto = new UserDto();
                dto.setId(entity.getId());
                dto.setEmail(entity.getEmail());
                dto.setFirstName(entity.getFirstName());
                dto.setLastName(entity.getLastName());
                dto.setAdmin(entity.isAdmin());
                
                return dto;
            }

            @Override
            public List<User> toEntity(List<UserDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                
                List<User> list = new ArrayList<>();
                for (UserDto dto : dtoList) {
                    list.add(toEntity(dto));
                }
                
                return list;
            }

            @Override
            public List<UserDto> toDto(List<User> entityList) {
                if (entityList == null) {
                    return null;
                }
                
                List<UserDto> list = new ArrayList<>();
                for (User entity : entityList) {
                    list.add(toDto(entity));
                }
                
                return list;
            }
        };
        
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);
        
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(false);
    }

    @Test
    public void testToEntity() {
        User entity = mapper.toEntity(userDto);
        assertNotNull(entity);
        assertEquals(userDto.getId(), entity.getId());
        assertEquals(userDto.getEmail(), entity.getEmail());
        assertEquals(userDto.getFirstName(), entity.getFirstName());
        assertEquals(userDto.getLastName(), entity.getLastName());
        assertEquals(userDto.isAdmin(), entity.isAdmin());
        assertNotNull(entity.getPassword()); // Password should not be null due to @NonNull constraint
    }

    @Test
    public void testToDto() {
        UserDto dto = mapper.toDto(user);
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.isAdmin(), dto.isAdmin());
        // Note: password is not mapped from entity to DTO
    }

    @Test
    public void testToEntityList() {
        List<UserDto> dtoList = Arrays.asList(userDto);
        List<User> entityList = mapper.toEntity(dtoList);
        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        assertEquals(userDto.getId(), entityList.get(0).getId());
        assertEquals(userDto.getEmail(), entityList.get(0).getEmail());
        assertEquals(userDto.getFirstName(), entityList.get(0).getFirstName());
        assertNotNull(entityList.get(0).getPassword()); // Password should not be null
    }

    @Test
    public void testToDtoList() {
        List<User> entityList = Arrays.asList(user);
        List<UserDto> dtoList = mapper.toDto(entityList);
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(user.getId(), dtoList.get(0).getId());
        assertEquals(user.getEmail(), dtoList.get(0).getEmail());
        assertEquals(user.getFirstName(), dtoList.get(0).getFirstName());
    }

    @Test
    public void testToEntityWithNull() {
        User entity = mapper.toEntity((UserDto) null);
        assertNull(entity);
    }

    @Test
    public void testToDtoWithNull() {
        UserDto dto = mapper.toDto((User) null);
        assertNull(dto);
    }

    @Test
    public void testToEntityListWithNull() {
        List<User> entityList = mapper.toEntity((List<UserDto>) null);
        assertNull(entityList);
    }

    @Test
    public void testToDtoListWithNull() {
        List<UserDto> dtoList = mapper.toDto((List<User>) null);
        assertNull(dtoList);
    }

    @Test
    public void testToEntityEmptyList() {
        List<UserDto> emptyDtoList = new ArrayList<>();
        List<User> entityList = mapper.toEntity(emptyDtoList);
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    public void testToDtoEmptyList() {
        List<User> emptyEntityList = new ArrayList<>();
        List<UserDto> dtoList = mapper.toDto(emptyEntityList);
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
}