package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EntityMapperTest {
    
    private EntityMapper<String, Integer> mapper;
    
    @BeforeEach
    public void setUp() {
        mapper = new EntityMapper<String, Integer>() {
            @Override
            public Integer toEntity(String dto) {
                if (dto == null) {
                    return null;
                }
                return Integer.parseInt(dto);
            }

            @Override
            public String toDto(Integer entity) {
                if (entity == null) {
                    return null;
                }
                return entity.toString();
            }

            @Override
            public List<Integer> toEntity(List<String> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                return dtoList.stream()
                        .map(dto -> this.toEntity(dto))
                        .collect(Collectors.toList());
            }

            @Override
            public List<String> toDto(List<Integer> entityList) {
                if (entityList == null) {
                    return null;
                }
                return entityList.stream()
                        .map(entity -> this.toDto(entity))
                        .collect(Collectors.toList());
            }
        };
    }
    
    @Test
    public void testToEntity_SingleItem() {
        Integer result = mapper.toEntity("123");
        assertEquals(123, result);
    }
    
    @Test
    public void testToDto_SingleItem() {
        String result = mapper.toDto(456);
        assertEquals("456", result);
    }
    
    @Test
    public void testToEntity_List() {
        List<String> dtoList = Arrays.asList("1", "2", "3");
        List<Integer> result = mapper.toEntity(dtoList);
        
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }
    
    @Test
    public void testToDto_List() {
        List<Integer> entityList = Arrays.asList(4, 5, 6);
        List<String> result = mapper.toDto(entityList);
        
        assertEquals(3, result.size());
        assertEquals("4", result.get(0));
        assertEquals("5", result.get(1));
        assertEquals("6", result.get(2));
    }
    
    @Test
    public void testToEntity_NullDto() {
        Integer result = mapper.toEntity((String)null);
        assertNull(result);
    }
    
    @Test
    public void testToDto_NullEntity() {
        String result = mapper.toDto((Integer)null);
        assertNull(result);
    }
    
    @Test
    public void testToEntity_NullDtoList() {
        List<Integer> result = mapper.toEntity((List<String>)null);
        assertNull(result);
    }
    
    @Test
    public void testToDto_NullEntityList() {
        List<String> result = mapper.toDto((List<Integer>)null);
        assertNull(result);
    }
}