package com.example.Library.Controllers;

import com.example.Library.Services.AuthorService;
import com.example.Library.TestingUtil;
import com.example.Library.domain.DTO.AuthorDTO;
import com.example.Library.domain.Entities.AuthorEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorControllerIntegrationTest {

    private AuthorService authorService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Create() throws Exception {
        AuthorEntity testAuthorA = TestingUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson)).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthorA = TestingUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value("80"));

    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        authorService.save(testAuthorEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value("80")
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus200WhenAuthorExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        authorService.save(testAuthorEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus404WhenAuthorDoesntExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenItExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        authorService.save(testAuthorEntityA);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(80));

    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus404WhenAuthorDoesntExist() throws Exception {
        AuthorDTO testAuthorDTOa = TestingUtil.createTestAuthorDTOa();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDTOa);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus200WhenAuthorExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(testAuthorEntityA);

        AuthorDTO testAuthorDTOa = TestingUtil.createTestAuthorDTOa();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDTOa);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateAuthorsUpdatedExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(testAuthorEntityA);

        AuthorEntity authorDTO = TestingUtil.createTestAuthorB();
        authorDTO.setId(savedAuthorEntity.getId());
        String authorDtoUpdateJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/" + savedAuthorEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorDtoUpdateJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(authorDTO.getAge()));

    }

    @Test
    public void testThatTestPartialUpdateExistingAuthorReturnsHttpStatus200() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(testAuthorEntityA);

        AuthorDTO testAuthorDTOa = TestingUtil.createTestAuthorDTOa();
        testAuthorDTOa.setName("Updated");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDTOa);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatTestPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(testAuthorEntityA);

        AuthorDTO testAuthorDTOa = TestingUtil.createTestAuthorDTOa();
        testAuthorDTOa.setName("Updated");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDTOa);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/" + savedAuthorEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorDtoJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDTOa.getAge()));

    }

    @Test
    public void testThatTestDeleteAuthorReturnsHttpStatus204whenAuthorNonExistant() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatTestDeleteAuthorReturnsHttpStatus204whenAuthorExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestingUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(testAuthorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}