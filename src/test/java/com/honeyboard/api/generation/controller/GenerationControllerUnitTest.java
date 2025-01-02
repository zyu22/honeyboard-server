package com.honeyboard.api.generation.controller;

import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.generation.service.GenerationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerationControllerUnitTest {
    @Mock
    private GenerationService generationService;

    @InjectMocks
    private GenerationController generationController;

    @Test
    void getAllGenerations_Success() {
        // given
        List<Generation> mockGenerations = Arrays.asList(
                new Generation(1, "12", false),
                new Generation(2, "13", true)
        );

        when(generationService.getAllGenerations()).thenReturn(mockGenerations);

        // when
        ResponseEntity<?> response = generationController.getAllGenerations();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(generationService).getAllGenerations();
    }

    @Test
    void getAllGenerations_NoContent() {
        // given
        when(generationService.getAllGenerations()).thenReturn(Collections.emptyList());

        // when
        ResponseEntity<?> response = generationController.getAllGenerations();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}