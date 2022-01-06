package com.cybertek.converter;

import com.cybertek.dto.RoleDTO;
import com.cybertek.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;    // be carefull for the import statement..
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class RoleDtoConverter implements Converter<String, RoleDTO> {

    @Autowired
    RoleService roleService;

    @Override
    public RoleDTO convert(String source) {
        Long id = Long.parseLong(source);
        return roleService.findById(id);
    }
}
