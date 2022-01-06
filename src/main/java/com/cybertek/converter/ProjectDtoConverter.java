package com.cybertek.converter;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ProjectDtoConverter implements Converter<String, ProjectDTO> {

    @Autowired
    ProjectService projectService;


    @Override
    public ProjectDTO convert(String pCode) {
        return projectService.getByProjectCode(pCode);
    }
}
