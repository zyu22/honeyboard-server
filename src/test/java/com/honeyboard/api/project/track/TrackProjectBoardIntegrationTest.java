package com.honeyboard.api.project.track;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.util.BaseIntegrationTest;

class TrackProjectBoardIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setup2() {
        baseUrl += "/project/track";
    }

    @Test
    @DisplayName("1. 관통 게시글 조회: 성공")
    void getTrackProjectBoard_Success() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrackProjectBoardDetail.class)
                .value(response -> assertThat(response).isNotNull());
    }

    @Test
    @DisplayName("2. 관통 게시글 조회 실패: 존재하지 않는 게시글 ID")
    void getTrackProjectBoard_Fail_NotExistBoardId() {
        // when: 존재하지 않는 게시글 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 999)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("3. 관통 게시글 조회 실패: 음수 게시글 ID")
    void getTrackProjectBoard_Fail_NegativeBoardId() {
        // when: 음수 게시글 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, -1)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("4. 관통 게시글 조회 실패: 0인 게시글 ID")
    void getTrackProjectBoard_Fail_ZeroBoardId() {
        // when: 0인 게시글 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 0)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("5. 관통 게시글 조회 실패: 잘못된 형식의 게시글 ID")
    void getTrackProjectBoard_Fail_InvalidBoardIdFormat() {
        // when: 잘못된 형식의 게시글 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/1/team/1/board/abc")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("6. 관통 게시글 조회 실패: 삭제된 게시글")
    void getTrackProjectBoard_Fail_DeletedBoard() {
        // given: 게시글 삭제
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isOk();

        // when: 삭제된 게시글 조회
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("7. 관통 게시글 조회 실패: 존재하지 않는 팀 ID")
    void getTrackProjectBoard_Fail_NotExistTeamId() {
        // when: 존재하지 않는 팀 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 999, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("8. 관통 게시글 조회 실패: 존재하지 않는 프로젝트 ID")
    void getTrackProjectBoard_Fail_NotExistProjectId() {
        // when: 존재하지 않는 프로젝트 ID로 API 호출
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 999, 1, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("9. 관통 게시글 작성: 성공")
    void createTrackProjectBoard_Success() {
        // given: 테스트용 관통 게시글 작성
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    int boardId = response.getId();

                    // 생성된 게시글 조회 및 검증
                    webTestClient.get()
                            .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, boardId)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(TrackProjectBoardDetail.class)
                            .value(tpbd -> {
                                assertThat(tpbd).isNotNull();
                                assertThat(body.getTitle()).isEqualTo(tpbd.getTitle());
                                assertThat(body.getContent()).isEqualTo(tpbd.getContent());
                                assertThat(body.getUrl()).isEqualTo(tpbd.getUrl());
                                assertThat(1).isEqualTo(tpbd.getTrackTeamId());
                            });
                });
    }

//    @Test
//    @DisplayName("10. 관통 게시글 작성: 실패 - 인증되지 않은 사용자")
//    void createTrackProjectBoard_Fail_Unauthorized() {
//        // given: 인증되지 않은 요청
//        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
//                "test", "test", "test", "test"
//        );
//
//        // when: API 호출
//        webTestClient.post()
//                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(body)
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }

    @Test
    @DisplayName("11. 관통 게시글 작성: 실패 - 유효하지 않은 트랙 프로젝트 ID")
    void createTrackProjectBoard_Fail_InvalidTrackProjectId() {
        // given: 유효하지 않은 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", -1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("12. 관통 게시글 작성: 실패 - 제목 누락")
    void createTrackProjectBoard_Fail_NoTitle() {
        // given: 제목이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                null, "test", "test", "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("13. 관통 게시글 작성: 실패 - 내용 누락")
    void createTrackProjectBoard_Fail_NoContent() {
        // given: 내용이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", null, "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("14. 관통 게시글 작성: 실패 - 빈 요청 본문")
    void createTrackProjectBoard_Fail_EmptyRequest() {
        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("15. 관통 게시글 작성: 실패 - 존재하지 않는 팀 ID")
    void createTrackProjectBoard_Fail_InvalidTeamId() {
        // given: 존재하지 않는 팀 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 1, 999)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("16. 관통 게시글 작성: 실패 - 존재하지 않는 프로젝트 ID")
    void createTrackProjectBoard_Fail_NotExistTrackProjectId() {
        // given: 존재하지 않는 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board", 999, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("17. 관통 게시글 수정: 성공")
    void updateTrackProjectBoard_Success() {
        // given: 테스트용 관통 게시글 작성
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "update", "update", "update", "update"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk();

        // then: 수정된 게시글 검증
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrackProjectBoardDetail.class)
                .value(tpbd -> {
                    assertThat(tpbd).isNotNull();
                    assertThat(body.getTitle()).isEqualTo(tpbd.getTitle());
                    assertThat(body.getContent()).isEqualTo(tpbd.getContent());
                    assertThat(body.getUrl()).isEqualTo(tpbd.getUrl());
                    assertThat(1).isEqualTo(tpbd.getTrackTeamId());
                });
    }

//    @Test
//    @DisplayName("18. 관통 게시글 수정: 실패 - 인증되지 않은 사용자")
//    void updateTrackProjectBoard_Fail_Unauthorized() {
//        // given: 인증되지 않은 요청
//        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
//                "test", "test", "test", "test"
//        );
//
//        // when: API 호출
//        webTestClient.put()
//                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(body)
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }

    @Test
    @DisplayName("19. 관통 게시글 수정: 실패 - 유효하지 않은 게시글 ID")
    void updateTrackProjectBoard_Fail_InvalidBoardId() {
        // given: 유효하지 않은 게시글 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, -1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("20. 관통 게시글 수정: 실패 - 존재하지 않는 게시글")
    void updateTrackProjectBoard_Fail_NotFoundBoard() {
        // given: 존재하지 않는 게시글 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 999)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("21. 관통 게시글 수정: 실패 - 제목 누락")
    void updateTrackProjectBoard_Fail_NoTitle() {
        // given: 제목이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                null, "test", "test", "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("22. 관통 게시글 수정: 실패 - 내용 누락")
    void updateTrackProjectBoard_Fail_NoContent() {
        // given: 내용이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", null, "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("23. 관통 게시글 수정: 실패 - 존재하지 않는 팀 ID")
    void updateTrackProjectBoard_Fail_NotExistTeamId() {
        // given: 유효하지 않은 팀 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 999, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("24. 관통 게시글 수정: 실패 - 존재하지 않는 프로젝트 ID")
    void updateTrackProjectBoard_Fail_NotExistProjectId() {
        // given: 존재하지 않는 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 999, 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("25. 관통 게시글 삭제: 성공")
    void deleteTrackProjectBoard_Success() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isOk();

        // then: 삭제 확인
        webTestClient.get()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("26. 관통 게시글 삭제: 실패 - 인증되지 않은 사용자")
    void deleteTrackProjectBoard_Fail_Unauthorized() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/board/{boardId}", 1, 1)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("27. 관통 게시글 삭제: 실패 - 유효하지 않은 게시글 ID")
    void deleteTrackProjectBoard_Fail_InvalidBoardId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/board/{boardId}", 1, -1)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("28. 관통 게시글 삭제: 실패 - 존재하지 않는 게시글 ID")
    void deleteTrackProjectBoard_Fail_NotExistBoardId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/board/{boardId}", 1, 999)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("29. 관통 게시글 삭제: 실패 - 존재하지 않는 프로젝트 ID")
    void deleteTrackProjectBoard_Fail_NotExistProjectId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/board/{boardId}", 999, 1)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("30. 관통 게시글 삭제: 실패 - 이미 삭제된 게시글")
    void deleteTrackProjectBoard_Fail_AlreadyDeleted() {
        // given: 첫 번째 삭제
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().isOk();

        // when: 두 번째 삭제 시도
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}", 1, 1, 1)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("31. 관통 게시글 삭제: 실패 - 존재하지 않는 팀 ID")
    void deleteTrackProjectBoard_Fail_NotExistTeamId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/{trackProjectId}/board/{boardId}", 1, 999)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
