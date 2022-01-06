package com.cybertek.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {  // <T> buraya da yazabiliriz....

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> T convert(Object objectToBeConverted, T convertedObject){
        return modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass());
    }

/*    public <T> T convertToEntity(Object objectToBeConverted, T convertedObject){
        return modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass());
    }

    public <T> T convertToDto(Object objectToBeConverted, T convertedObject){
        return modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass());
    }
*/
}
