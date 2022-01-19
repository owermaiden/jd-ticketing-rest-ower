package com.cybertek.controller;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.RoleDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.enums.Gender;
import com.cybertek.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc  // configuration
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJhZG1pbiIsImxhc3ROYW1lIjoiYWRtaW4iLCJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpZCI6MSwiZXhwIjoxNjQyNjIzNTQ0LCJpYXQiOjE2NDI1ODc1NDQsInVzZXJuYW1lIjoiYWRtaW5AYWRtaW4uY29tIn0.xYbDOb4pCUOP4XBOdZOXg4j2iwpPH4ok5pHvdx1qrd0";

    static UserDTO userDTO;
    static ProjectDTO projectDTO;

    @BeforeAll
    static void setup(){

        userDTO = UserDTO.builder()
                .id(2L)
                .firstName("mike")
                .lastName("smith")
                .userName("ozzy@cybertekschool.com")
                .passWord("abc123")
                .confirmPassword("abc123")
                .role(new RoleDTO(2L,"Manager"))
                .gender(Gender.MALE)
                .build();

        projectDTO = ProjectDTO.builder()
                .projectCode("Api1")
                .projectName("Api-ozzy")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("Api Test")
                .projectStatus(Status.OPEN)
                .completeTaskCounts(0)
                .unfinishedTaskCounts(0)
                .build();
    }

    @Test
    public void givenNoToken_whenGetSecureRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/project/Api1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenToken_getAllProjects() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/project")
                        .header("Authorization",token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").value("10001")) // I have added this line..
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].assignedManager.userName").isNotEmpty());

    }

    @Test
    public void givenToken_createProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/project")
                        .header("Authorization",token)
                        .content(toJsonString(projectDTO)) // we need to convert our project dto to JSON...
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.projectCode").isNotEmpty());
    }

    @Test
    public void givenToken_updateProject() throws Exception {

        projectDTO.setId(2L); // we are updating the project which id=2..

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/project")
                        .header("Authorization",token)
                        .content(toJsonString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project is updated"));
    }

    @Test
    public void givenToken_deleteProject() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/project/"+projectDTO.getProjectCode())
                        .header("Authorization",token)
                        .content(toJsonString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    // this method converts our object to JSON...
    protected static String toJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}