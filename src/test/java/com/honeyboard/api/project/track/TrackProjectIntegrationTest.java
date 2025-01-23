package com.honeyboard.api.project.track;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.util.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

class TrackProjectIntegrationTest extends BaseIntegrationTest {

    @Nested
    @DisplayName("관통 프로젝트 전체 조회")
    class GetAllTrackProjects {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("비어있지 않은 관통 프로젝트 리스트")
            void success_NotEmpty() {
                //given
                //when
                ResponseEntity<List<TrackProjectList>> response = restTemplate.exchange(
                        baseUrl + "?generationId=2",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<TrackProjectList>>() {
                        }
                );

                List<TrackProjectList> list = response.getBody();

                //then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                        () -> assertThat(list).isNotNull(),
                        () -> assertThat(list.size()).isEqualTo(3)
                );
            }

            @Test
            @DisplayName("비어있는 관통 프로젝트 리스트")
            void success_Empty() {
                //given
                for (int i = 1; i <= 3; i++) {
                    restTemplate.exchange(
                            baseUrl + "/" + i,
                            HttpMethod.DELETE,
                            null,
                            Void.class
                    );
                }

                //when
                ResponseEntity<List<TrackProjectList>> response = restTemplate.exchange(
                        baseUrl + "?generationId=2",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

                //then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT),
                        () -> {
                            List<TrackProjectList> list = response.getBody();
                            if (list != null) {
                                assertThat(list.isEmpty()).isTrue();
                            }
                        }
                );
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(strings = {"-1", "abc"})
            @DisplayName("잘못된 형식의 기수 ID 요청시 400 응답")
            void fail_InvalidGenerationId(String id) {
                // when
                ResponseEntity<Object> response = restTemplate.exchange(
                        baseUrl + "?generationId=" + id,
                        HttpMethod.GET,
                        null,
                        Object.class
                );

                // then
                assertThat(response.getStatusCode().value())
                        .isEqualTo(400);
            }

            @Test
            @DisplayName("로그인 안한 사용자 요청시 401 응답")
            void fail_Unauthorized() {
                // given
                restTemplate.getRestTemplate().getInterceptors().clear();

                // when
                ResponseEntity<Object> response = restTemplate.exchange(
                        baseUrl,
                        HttpMethod.GET,
                        null,
                        Object.class
                );

                // then
                assertThat(response.getStatusCode().value())
                        .isEqualTo(401);
            }

            @Test
            @DisplayName("존재하지 않는 기수 ID 요청시 404 응답")
            void fail_NonExistentGenerationId() {
                // given
                int nonExistentId = 9999;

                // when
                ResponseEntity<Object> response = restTemplate.exchange(
                        baseUrl + "?generationId=" + nonExistentId,
                        HttpMethod.GET,
                        null,
                        Object.class
                );

                // then
                assertThat(response.getStatusCode().value())
                        .isEqualTo(404);
            }
        }
    }

    @Nested
    @DisplayName("관통 프로젝트 상세 조회")
    class GetTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("관통 프로젝트 상세 정보를 성공적으로 조회")
            void success_GetTrackProjectDetail() {
                // given
                int projectId = 1;

                // when
                ResponseEntity<TrackProjectDetail> response = restTemplate.exchange(
                        baseUrl + "/" + projectId,
                        HttpMethod.GET,
                        null,
                        TrackProjectDetail.class
                );

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode().value()).isEqualTo(200),
                        () -> assertThat(response.getBody()).isNotNull(),
                        () -> assertThat(response.getBody().getId()).isEqualTo(projectId),
                        () -> assertThat(response.getBody().getTitle()).isNotNull(),
                        () -> assertThat(response.getBody().getObjective()).isNotNull(),
                        () -> assertThat(response.getBody().getDescription()).isNotNull(),
                        () -> assertThat(response.getBody().getCreatedAt()).isNotNull(),
                        () -> assertThat(response.getBody().getNoTeamUsers()).isNotNull(),
                        () -> assertThat(response.getBody().getTeams()).isNotNull(),
                        () -> assertThat(response.getBody().getTrackProjectBoardList()).isNotNull()
                );
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(strings={"-1","abc"})
            @DisplayName("유효하지 않은 프로젝트 ID로 조회 시 400 응답")
            void fail_GetTrackProjectDetail_InvalidId(String id) {
                // when
                ResponseEntity<TrackProjectDetail> response = restTemplate.exchange(
                        baseUrl + "/" + id,
                        HttpMethod.GET,
                        null,
                        TrackProjectDetail.class
                );

                // then
                assertThat(response.getStatusCode().value()).isEqualTo(400);
            }

            @Test
            @DisplayName("인증되지 않은 사용자(로그인 안함) 접근 시 401 응답")
            void fail_GetTrackProjectDetail_Unauthorized() {
                // given
                int projectId = 1;
                restTemplate.getRestTemplate().getInterceptors().clear();

                // when
                ResponseEntity<TrackProjectDetail> response = restTemplate.exchange(
                        baseUrl + "/" + projectId,
                        HttpMethod.GET,
                        null,
                        TrackProjectDetail.class
                );

                // then
                assertThat(response.getStatusCode().value()).isEqualTo(401);
            }

            @Test
            @DisplayName("존재하지 않는 프로젝트 ID로 조회 시 404 응답")
            void fail_GetTrackProjectDetail_NonExistentId() {
                // given
                int nonExistentId = 9999;

                // when
                ResponseEntity<TrackProjectDetail> response = restTemplate.exchange(
                        baseUrl + "/" + nonExistentId,
                        HttpMethod.GET,
                        null,
                        TrackProjectDetail.class
                );

                // then
                assertThat(response.getStatusCode().value()).isEqualTo(404);
            }

        }
    }

    @Nested
    @DisplayName("관통 프로젝트 가능한 인원 검색")
    class GetTrackProjectMembers {
        @Nested
        @DisplayName("성공")
        class Success {

        }

        @Nested
        @DisplayName("실패")
        class Fail {

        }
    }

    @Nested
    @DisplayName("관통 프로젝트 생성")
    class CreateTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {

        }

        @Nested
        @DisplayName("실패")
        class Fail {

        }
    }

    @Nested
    @DisplayName("관통 프로젝트 수정")
    class UpdateTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {

        }

        @Nested
        @DisplayName("실패")
        class Fail {

        }
    }

    @Nested
    @DisplayName("관통 프로젝트 삭제")
    class DeleteTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {

        }

        @Nested
        @DisplayName("실패")
        class Fail {

        }
    }

}
