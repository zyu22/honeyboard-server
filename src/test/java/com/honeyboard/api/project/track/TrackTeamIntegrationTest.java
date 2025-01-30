package com.honeyboard.api.project.track;

import com.honeyboard.api.project.model.TeamRequest;
import com.honeyboard.api.util.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

class TrackTeamIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setup2() {
        baseUrl += "/project/track";
    }

    @Nested
    @DisplayName("관통 팀 생성")
    class CreateTrackTeam {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("유효한 요청으로 생성 시 201 응답")
            void success_CreateTrackTeam() {
                // given
                int projectId = 1;
                int teamId = 3;
                int leaderId = 3;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.post()
                        .uri(baseUrl + "/{trackProjectId}/team", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated();
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("팀장 ID가 없는 경우 400 응답")
            void fail_CreateTrackTeam_NoLeader() {
                // given
                int projectId = 1;
                int teamId = 3;
                int leaderId = 3214213;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.post()
                        .uri(baseUrl + "/{trackProjectId}/team", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }

            @ParameterizedTest
            @ValueSource(strings = {"-1", "0"})
            @DisplayName("유효하지 않은 프로젝트 ID로 생성 시 400 응답")
            void fail_CreateTrackTeam_InvalidProjectId(String id) {
                // given
                int teamId = 3;
                int leaderId = 3;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.post()
                        .uri(baseUrl + "/{trackProjectId}/team", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

            @Test
            @DisplayName("존재하지 않는 관통 프로젝트 ID로 생성 시 404 응답")
            void fail_createTrackTeam_NotFound() {
                // given
                int projectId = 9999;
                int teamId = 3;
                int leaderId = 3;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.post()
                        .uri(baseUrl + "/{trackProjectId}/team", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }
        }
    }

    @Nested
    @DisplayName("관통 팀 수정")
    class UpdateTrackTeam {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("유효한 요청으로 수정 시 200 응답")
            void success_UpdateTrackTeam() {
                // given
                int projectId = 1;
                int teamId = 1;
                int leaderId = 1;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}", projectId, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk();
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("유효하지 않은 프로젝트 ID로 수정 시 400 응답")
            void fail_UpdateTrackTeam_InvalidProjectId(int id) {
                // given
                int teamId = 1;
                int leaderId = 1;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}", id, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

            @Test
            @DisplayName("존재하지 않는 팀 ID로 수정 시 404 응답")
            void fail_UpdateTrackTeam_NotFound() {
                // given
                int projectId = 1;
                int teamId = 9999;
                int leaderId = 1;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}", projectId, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }

            @Test
            @DisplayName("새 팀장이 다른 팀에 속해있을 경우 409 응답")
            void fail_UpdateTrackTeam_Conflict1() {
                // given
                int projectId = 1;
                int teamId = 1;
                int leaderId = 2;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(4)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}", projectId, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is4xxClientError();
            }

            @Test
            @DisplayName("새 팀원이 다른 팀에 속해있을 경우 409 응답")
            void fail_UpdateTrackTeam_Conflict2() {
                // given
                int projectId = 1;
                int teamId = 1;
                int leaderId = 3;
                TeamRequest request = new TeamRequest(
                        teamId,
                        leaderId,
                        List.of(2)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}", projectId, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isEqualTo(HttpStatus.CONFLICT);
            }
        }
    }

}
