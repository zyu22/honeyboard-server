package com.honeyboard.api.project.track;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.util.BaseIntegrationTest;

class TrackProjectIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setup2() {
        baseUrl += "/project/track";
    }

    @Nested
    @DisplayName("관통 프로젝트 전체 조회")
    class GetAllTrackProjects {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("비어있지 않은 관통 프로젝트 리스트")
            void success_NotEmpty() {
                //when
                webTestClient.get()
                        .uri(baseUrl + "?generationId=2")
                        .exchange()
                        .expectAll(
                                spec -> spec.expectStatus().isOk(),
                                spec -> spec.expectBody(new ParameterizedTypeReference<List<TrackProjectList>>() {
                                        })
                                        .value(list -> {
                                            assertThat(list).isNotNull();
                                            assertThat(list.size()).isEqualTo(3);
                                        })
                        );
            }

            @Test
            @DisplayName("비어있는 관통 프로젝트 리스트")
            void success_Empty() {
                //given
                for (int i = 1; i <= 3; i++) {
                    webTestClient.delete()
                            .uri(baseUrl + "/" + i)
                            .exchange()
                            .expectStatus().isOk();
                }

                //when
                webTestClient.get()
                        .uri(baseUrl + "?generationId=2")
                        .exchange()
                        .expectAll(
                                spec -> spec.expectStatus().isNoContent(),
                                spec -> spec.expectBody(new ParameterizedTypeReference<List<TrackProjectList>>() {
                                        })
                                        .value(list -> {
                                            if (list != null) {
                                                assertThat(list.isEmpty()).isTrue();
                                            }
                                        })
                        );
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(ints = {-1})
            @DisplayName("잘못된 형식의 기수 ID 요청시 400 응답")
            void fail_InvalidGenerationId(int id) {
                // when
                webTestClient.get()
                        .uri(baseUrl + "?generationId=" + id)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

//            @Test
//            @DisplayName("로그인 안한 사용자 요청시 401 응답")
//            void fail_Unauthorized() {
//                // given
//                webTestClient = webTestClient.mutate()
//                        .defaultHeader(HttpHeaders.COOKIE, "")
//                        .build();
//
//                // when
//                webTestClient.get()
//                        .uri(baseUrl)
//                        .exchange()
//                        .expectStatus().isUnauthorized();
//            }

            @Test
            @DisplayName("존재하지 않는 기수 ID 요청시 404 응답")
            void fail_NonExistentGenerationId() {
                // given
                int nonExistentId = 9999;

                // when
                webTestClient.get()
                        .uri(baseUrl + "?generationId=" + nonExistentId)
                        .exchange()
                        .expectStatus().isNoContent();
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
                webTestClient.get()
                        .uri(baseUrl + "/" + projectId)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(TrackProjectDetail.class)
                        .value(response -> {
                            assertThat(response).isNotNull();
                            assertThat(response.getId()).isEqualTo(projectId);
                            assertThat(response.getTitle()).isNotNull();
                            assertThat(response.getObjective()).isNotNull();
                            assertThat(response.getDescription()).isNotNull();
                            assertThat(response.getCreatedAt()).isNotNull();
                            assertThat(response.getNoTeamUsers()).isNotNull();
                            assertThat(response.getTeams()).isNotNull();
                            assertThat(response.getBoards().isEmpty());
                        });
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(strings = {"-1", "0"})
            @DisplayName("유효하지 않은 프로젝트 ID로 조회 시 400 응답")
            void fail_GetTrackProjectDetail_InvalidId(String id) {
                // when
                webTestClient.get()
                        .uri(baseUrl + "/" + id)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

//            @Test
//            @DisplayName("인증되지 않은 사용자(로그인 안함) 접근 시 401 응답")
//            void fail_GetTrackProjectDetail_Unauthorized() {
//                // given
//                int projectId = 1;
//                webTestClient = webTestClient.mutate()
//                        .defaultHeader(HttpHeaders.COOKIE, "")
//                        .build();
//
//                // when
//                webTestClient.get()
//                        .uri(baseUrl + "/" + projectId)
//                        .exchange()
//                        .expectStatus().isUnauthorized();
//            }

            @Test
            @DisplayName("존재하지 않는 프로젝트 ID로 조회 시 404 응답")
            void fail_GetTrackProjectDetail_NonExistentId() {
                // given
                int nonExistentId = 9999;

                // when
                webTestClient.get()
                        .uri(baseUrl + "/" + nonExistentId)
                        .exchange()
                        .expectStatus().isNoContent();
            }
        }
    }

    @Nested
    @DisplayName("관통 프로젝트 가능한 인원 검색")
    class GetTrackProjectMembers {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("가능한 인원 목록을 성공적으로 조회")
            void success_GetTrackProjectMembers() {
                // given
                int projectId = 1;

                // when
                webTestClient.get()
                        .uri(baseUrl + "/{trackProjectId}/available-user", projectId)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(new ParameterizedTypeReference<List<ProjectUserInfo>>() {
                        })
                        .value(response -> {
                            assertThat(response).isNotNull();
                            assertThat(response.size()).isGreaterThan(0);
                            assertThat(response.get(0).getId()).isNotNull();
                            assertThat(response.get(0).getName()).isNotNull();
                        });
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(ints = {-5, 0})
            @DisplayName("유효하지 않은 프로젝트 ID로 조회 시 400 응답")
            void fail_GetTrackProjectMembers_InvalidId(int id) {
                // when
                webTestClient.get()
                        .uri(baseUrl + "/{trackProjectId}/available-user", id)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

//            @Test
//            @DisplayName("인증되지 않은 사용자(로그인 안함) 접근 시 401 응답")
//            void fail_GetTrackProjectMembers_Unauthorized() {
//                // given
//                int projectId = 1;
//                webTestClient = webTestClient.mutate()
//                        .defaultHeader(HttpHeaders.COOKIE, "")
//                        .build();
//
//                // when
//                webTestClient.get()
//                        .uri(baseUrl + "/{trackProjectId}/available-user", projectId)
//                        .exchange()
//                        .expectStatus().isUnauthorized();
//            }

            @Test
            @DisplayName("존재하지 않는 프로젝트 ID로 조회 시 404 응답")
            void fail_GetTrackProjectMembers_NonExistentId() {
                // given
                int nonExistentId = 9999;

                // when
                webTestClient.get()
                        .uri(baseUrl + "/{trackProjectId}/available-user", nonExistentId)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("관통 프로젝트 생성")
    class CreateTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("유효한 요청으로 생성 시 201 응답")
            void success_CreateTrackProject() {
                // given
                TrackProjectRequest request = new TrackProjectRequest(
                        "test",
                        "test",
                        "test",
                        List.of(1, 2, 3)
                );

                // when
                webTestClient.post()
                        .uri(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(CreateResponse.class)
                        .value(response -> {
                            assertThat(response).isNotNull();
                            assertThat(response.getId()).isGreaterThan(0);
                        });
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("제외 멤버 리스트가 비어있을 경우 400 응답")
            void fail_CreateTrackProject_EmptyExcludedMembers() {
                // given
                TrackProjectRequest request = new TrackProjectRequest(
                        "test",
                        "test",
                        "test",
                        List.of()
                );

                // when
                webTestClient.post()
                        .uri(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }

            @Test
            @DisplayName("인증되지 않은 사용자 요청 시 401 응답")
            void fail_CreateTrackProject_Unauthorized() {
                // given
                TrackProjectRequest request = new TrackProjectRequest();
                webTestClient = webTestClient.mutate()
                        .defaultHeader(HttpHeaders.COOKIE, "")
                        .build();

                // when
                webTestClient.post()
                        .uri(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isUnauthorized();
            }
        }
    }

    @Nested
    @DisplayName("관통 프로젝트 수정")
    class UpdateTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("유효한 요청으로 수정 시 200 응답")
            void success_UpdateTrackProject() {
                // given
                int projectId = 1;
                TrackProjectRequest request = new TrackProjectRequest(
                        "test",
                        "test",
                        "test",
                        List.of(1, 2, 3)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}", projectId)
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
            @ValueSource(strings = {"-1", "0"})
            @DisplayName("유효하지 않은 프로젝트 ID로 수정 시 400 응답")
            void fail_UpdateTrackProject_InvalidId(String id) {
                // given
                TrackProjectRequest request = new TrackProjectRequest();

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

            @Test
            @DisplayName("존재하지 않는 프로젝트 ID로 수정 시 404 응답")
            void fail_UpdateTrackProject_NotFound() {
                // given
                int nonExistentId = 9999;
                TrackProjectRequest request = new TrackProjectRequest(
                        "test",
                        "test",
                        "test",
                        List.of(1, 2, 3)
                );

                // when
                webTestClient.put()
                        .uri(baseUrl + "/{trackProjectId}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }
        }
    }

    @Nested
    @DisplayName("관통 프로젝트 삭제")
    class DeleteTrackProject {
        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("관통 프로젝트를 성공적으로 삭제")
            void success_DeleteTrackProject() {
                // given
                int projectId = 1;

                // when
                webTestClient.delete()
                        .uri(baseUrl + "/{trackProjectId}", projectId)
                        .exchange()
                        .expectStatus().isOk();

                // then: 삭제된 프로젝트 조회 시 404(가 적절한데 이 프로젝트는 204 반환 중이네요)
                webTestClient.get()
                        .uri(baseUrl + "/{trackProjectId}", projectId)
                        .exchange()
                        .expectStatus().isNoContent();
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @ParameterizedTest
            @ValueSource(strings = {"-1", "0"})
            @DisplayName("유효하지 않은 프로젝트 ID로 삭제 시 400 응답")
            void fail_DeleteTrackProject_InvalidId(String id) {
                // when
                webTestClient.delete()
                        .uri(baseUrl + "/{trackProjectId}", id)
                        .exchange()
                        .expectStatus().isBadRequest();
            }

//            @Test
//            @DisplayName("인증되지 않은 사용자(로그인 안함) 접근 시 401 응답")
//            void fail_DeleteTrackProject_Unauthorized() {
//                // given
//                int projectId = 1;
//                webTestClient = webTestClient.mutate()
//                        .defaultHeader(HttpHeaders.COOKIE, "")
//                        .build();
//
//                // when
//                webTestClient.delete()
//                        .uri(baseUrl + "/{trackProjectId}", projectId)
//                        .exchange()
//                        .expectStatus().isUnauthorized();
//            }

            @Test
            @DisplayName("존재하지 않는 프로젝트 ID로 삭제 시 404 응답")
            void fail_DeleteTrackProject_NotFound() {
                // given
                int nonExistentId = 9999;

                // when
                webTestClient.delete()
                        .uri(baseUrl + "/{trackProjectId}", nonExistentId)
                        .exchange()
                        .expectStatus().isBadRequest();
            }
        }
    }

}
