package com.honeyboard.api.project.finale.controller;

import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.project.finale.service.FinaleProjectService;
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import com.honeyboard.api.user.model.UserName;
import com.honeyboard.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// 단위테스트
@ExtendWith(MockitoExtension.class)
public class FinaleTeamControllerUnitTest {
    @Mock
    private FinaleProjectService finaleProjectService;

    @Mock
    private FinaleTeamService finaleTeamService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FinaleTeamController finaleTeamController;

    @Test
    void getAllSubmittedStatus_Success() {
        // given
        int projectId = 1;
        String date = "20240102";
        List<FinaleTeam> mockTeams = Arrays.asList(
                new FinaleTeam(1, 13, LocalDate.now(), LocalDate.now())
        );

        when(finaleTeamService.findStatusByDate(any(LocalDate.class))).thenReturn(mockTeams);

        // when
        ResponseEntity<List<FinaleTeam>> response = finaleTeamController.getAllSubmittedStatus(projectId, date);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        verify(finaleTeamService).findStatusByDate(any(LocalDate.class));
    }

    @Test
    void getRemainedUsers_WithNullGeneration_Success() {
        // given
        Integer activeGenerationId = 13;
        List<UserName> mockUsers = Arrays.asList(
                UserName.builder()
                        .userId(1)
                        .name("test")
                        .build()
        );

        when(userService.getActiveGenerationId()).thenReturn(activeGenerationId);
        when(finaleTeamService.getRemainedUsers(activeGenerationId)).thenReturn(mockUsers);

        // when
        ResponseEntity<List<UserName>> response = finaleTeamController.getRemainedUsers(null);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        verify(userService).getActiveGenerationId();
        verify(finaleTeamService).getRemainedUsers(activeGenerationId);
    }
}