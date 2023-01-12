package com.wirehood.adminservice.utils;

import org.modelmapper.ModelMapper;

public class MapperUtils {

    public static <T> T map(Object source, Class<T> destination) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destination);
    }
}
