package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDtoRequest itemDtoRequest;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("desc1")
                .available(true)
                .build();
        itemDtoRequest = ItemDtoRequest.builder()
                .id(1L)
                .name("item1")
                .description("desc1")
                .available(true)
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(service.add(anyLong(), any(ItemDtoRequest.class)))
                .thenReturn(itemDtoRequest);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRequest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoRequest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRequest.getDescription())));
    }

    @Test
    void getAllTest() throws Exception {
        when(service.getAllUserItems(anyLong(), anyLong(), anyLong()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                        .param("from", String.valueOf(0))
                        .param("size",String.valueOf(0))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[0].name",is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description",is(itemDto.getDescription()), String.class));
    }

}