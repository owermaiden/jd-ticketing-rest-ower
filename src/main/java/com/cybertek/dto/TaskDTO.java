package com.cybertek.dto;

import com.cybertek.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"}, ignoreUnknown = true)
public class TaskDTO {

    private Long id;
    private ProjectDTO project;
    private UserDTO assignedEmployee;
    private String taskSubject;
    private String taskDetail;
    private Status taskStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;


}
