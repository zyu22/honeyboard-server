package com.honeyboard.api.algorithm.guide;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.config.TestConfig;
import com.honeyboard.api.util.BaseIntegrationTest;

class AlgorithmGuideIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setup2() {
        baseUrl += "/algorithm/guide";
    }

    @Test
    @DisplayName("1. 알고리즘 개념 전체 조회(검색어 X): 성공")
    void getAllAlgorithmGuides_Success() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().size()).isEqualTo(4);

                    // 생성일자 기준 내림차순 정렬 확인
                    List<AlgorithmGuideList> content = list.getContent();
                    for (int i = 0; i < content.size() - 1; i++) {
                        LocalDateTime a = LocalDateTime.parse(content.get(i).getCreatedAt(), TestConfig.formatter);
                        LocalDateTime b = LocalDateTime.parse(content.get(i + 1).getCreatedAt(), TestConfig.formatter);
                        assertThat(a).isAfterOrEqualTo(b);
                    }
                });
    }

    @Test
    @DisplayName("2. 알고리즘 개념 전체 조회(검색어 X) 실패: 잘못된 페이지 번호")
    void getAllAlgorithmGuides_Fail_InvalidPage() {
        // when: API 호출
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(baseUrl + "?currentPage=0")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("3. 알고리즘 개념 전체 조회(검색어 X) 실패: 잘못된 페이지 크기")
    void getAllAlgorithmGuides_Fail_InvalidPageSize() {
        // when: API 호출
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(baseUrl + "?pageSize=0")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("4. 알고리즘 개념 전체 조회(검색어 X) 실패: 존재하지 않는 기수")
    void getAllAlgorithmGuides_Fail_InvalidGeneration() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?generationId=999")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().isEmpty()).isTrue();
                });
    }

    @Test
    @DisplayName("5. 알고리즘 개념 전체 조회(검색어 X) 실패: 데이터가 없는 페이지")
    void getAllAlgorithmGuides_Fail_EmptyPage() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?currentPage=999")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().isEmpty()).isTrue();
                });
    }

    @Test
    @DisplayName("6. 알고리즘 개념 전체 조회(검색어 O) - 성공")
    void getAllAlgorithmGuides_Success_Search() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?keyword=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().size()).isEqualTo(4);

                    List<AlgorithmGuideList> content = list.getContent();
                    for (int i = 0; i < content.size() - 1; i++) {
                        assertThat(content.get(i).getTitle().contains("test")).isTrue();
                        LocalDateTime a = LocalDateTime.parse(content.get(i).getCreatedAt(), TestConfig.formatter);
                        LocalDateTime b = LocalDateTime.parse(content.get(i + 1).getCreatedAt(), TestConfig.formatter);
                        assertThat(a).isAfterOrEqualTo(b);
                    }

                    assertThat(content.get(content.size() - 1).getTitle().contains("test")).isTrue();
                });
    }

    @Test
    @DisplayName("7. 알고리즘 개념 전체 조회(검색어 O) - 실패 (빈 검색어)")
    void getAllAlgorithmGuides_Fail_EmptyTitle() {
        // when & then
        webTestClient.get()
                .uri(baseUrl + "?keyword=")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });
    }

    /* AlgorithmGuide 공백, 특수문자 검색어 검증이 의도된 게 아니라면 백엔드에서도 검증해야 합니다 */
    @Test
    @DisplayName("8. 알고리즘 개념 전체 조회(검색어 O) - 실패 (공백 검색어)")
    void getAllAlgorithmGuides_Fail_BlankTitle() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?keyword= ")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("9. 알고리즘 개념 전체 조회(검색어 O) - 실패 (존재하지 않는 검색어)")
    void getAllAlgorithmGuides_Fail_NotExistTitle() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?title=notexist")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().isEmpty()).isTrue();
                });
    }

    @Test
    @DisplayName("10. 알고리즘 개념 전체 조회(검색어 O) - 실패 (특수문자 검색어)")
    void getAllAlgorithmGuides_Fail_SpecialCharacterKeyword() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "?title=!@#$%")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                })
                .value(list -> {
                    assertThat(list).isNotNull();
                    assertThat(list.getContent().isEmpty()).isTrue();
                });
    }

    @Test
    @DisplayName("11. 알고리즘 개념 단건 조회 - 성공")
    void getAlgorithmGuide_Success() {
        // given: 존재하는 알고리즘 가이드 ID
        int algorithmGuideId = 4;

        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/" + algorithmGuideId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AlgorithmGuideDetail.class)
                .value(algorithmGuide -> {
                    assertThat(algorithmGuide).isNotNull();
                    assertThat(algorithmGuide.getId()).isEqualTo(algorithmGuideId);
                });
    }

    @Test
    @DisplayName("12. 알고리즘 개념 단건 조회 - 실패 (음수 ID)")
    void getAlgorithmGuide_Fail_NegativeId() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/-1")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("13. 알고리즘 개념 단건 조회 - 실패 (0인 ID)")
    void getAlgorithmGuide_Fail_ZeroId() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/0")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("14. 알고리즘 개념 단건 조회 - 실패 (존재하지 않는 큰 ID)")
    void getAlgorithmGuide_Fail_NonExistentLargeId() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/999999")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("15. 알고리즘 개념 단건 조회 - 실패 (잘못된 형식의 ID)")
    void getAlgorithmGuide_Fail_InvalidIdFormat() {
        // when: API 호출
        webTestClient.get()
                .uri(baseUrl + "/abc")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("16. 알고리즘 개념 생성 - 성공")
    void createAlgorithmGuide_Success() {
        // given: 요청 바디 설정
        AlgorithmGuideRequest body = new AlgorithmGuideRequest("test", "test", "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateResponse.class)
                .value(response -> {
                    int guideId = response.getId();
                    assertThat(guideId > 0).isTrue();

                    // 생성된 데이터 조회 및 검증
                    webTestClient.get()
                            .uri(baseUrl + "/" + guideId)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(AlgorithmGuideDetail.class)
                            .value(guide -> {
                                assertThat(guide).isNotNull();
                                assertThat(guide.getTitle()).isNotNull();
                                assertThat(guide.getContent()).isNotNull();
                            });
                });
    }

    @Test
    @DisplayName("17. 알고리즘 개념 생성 - 실패 (제목 누락)")
    void createAlgorithmGuide_Fail_NoTitle() {
        // given: 제목이 누락된 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(null, "test", "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("18. 알고리즘 개념 생성 - 실패 (내용 누락)")
    void createAlgorithmGuide_Fail_NoContent() {
        // given: 내용이 누락된 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("test", null, "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("19. 알고리즘 개념 생성 - 실패 (빈 제목)")
    void createAlgorithmGuide_Fail_EmptyTitle() {
        // given: 빈 제목으로 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("", "test", "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("20. 알고리즘 개념 생성 - 실패 (빈 내용)")
    void createAlgorithmGuide_Fail_EmptyContent() {
        // given: 빈 내용으로 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("test", "", "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("21. 알고리즘 개념 생성 - 실패 (제목 최대 길이 초과)")
    void createAlgorithmGuide_Fail_TitleTooLong() {
        // given: 최대 길이를 초과하는 제목으로 요청 바디
        String longTitle = "a".repeat(256);
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(longTitle, "test", "test");

        // when: API 호출
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("22. 알고리즘 개념 수정 - 성공")
    void updateAlgorithmGuide_Success() {
        // given: 수정할 알고리즘 가이드 ID와 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");

        // when: 수정 API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        // then: 수정된 데이터 조회 및 검증
        webTestClient.get()
                .uri(baseUrl + "/4")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AlgorithmGuideDetail.class)
                .value(updatedGuide -> {
                    assertThat(updatedGuide).isNotNull();
                    assertThat(updatedGuide.getTitle()).isEqualTo("updated title");
                    assertThat(updatedGuide.getContent()).isEqualTo("updated content");
                });
    }

    @Test
    @DisplayName("23. 알고리즘 개념 수정 - 실패 (존재하지 않는 ID)")
    void updateAlgorithmGuide_Fail_NonExistentId() {
        // given: 존재하지 않는 ID와 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("24. 알고리즘 개념 수정 - 실패 (권한 없음)")
    void updateAlgorithmGuide_Fail_NoPermission() {
        // given: 다른 사용자가 작성한 게시글의 ID와 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/1") // user_id가 1인 사용자의 게시글
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("25. 알고리즘 개념 수정 - 실패 (제목 누락)")
    void updateAlgorithmGuide_Fail_NoTitle() {
        // given: 제목이 없는 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                null,
                "updated content",
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("26. 알고리즘 개념 수정 - 실패 (내용 누락)")
    void updateAlgorithmGuide_Fail_NoContent() {
        // given: 내용이 없는 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                null,
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("27. 알고리즘 개념 수정 - 실패 (빈 제목)")
    void updateAlgorithmGuide_Fail_EmptyTitle() {
        // given: 빈 제목으로 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "",
                "updated content",
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("28. 알고리즘 개념 수정 - 실패 (빈 내용)")
    void updateAlgorithmGuide_Fail_EmptyContent() {
        // given: 빈 내용으로 요청 바디
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "",
                "updated thumbnail");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("29. 알고리즘 개념 수정 - 실패 (제목 최대 길이 초과)")
    void updateAlgorithmGuide_Fail_TitleTooLong() {
        // given: 최대 길이를 초과하는 제목으로 요청 바디
        String longTitle = "a".repeat(256);
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(longTitle, "test", "test");

        // when: API 호출
        webTestClient.put()
                .uri(baseUrl + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("30. 알고리즘 개념 삭제 - 성공")
    void deleteAlgorithmGuide_Success() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("31. 알고리즘 개념 삭제 - 실패 (존재하지 않는 ID)")
    void deleteAlgorithmGuide_Fail_NonExistentId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/999999")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("32. 알고리즘 개념 삭제 - 실패 (음수 ID)")
    void deleteAlgorithmGuide_Fail_NegativeId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/-1")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("33. 알고리즘 개념 삭제 - 실패 (0인 ID)")
    void deleteAlgorithmGuide_Fail_ZeroId() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/0")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("34. 알고리즘 개념 삭제 - 실패 (잘못된 형식의 ID)")
    void deleteAlgorithmGuide_Fail_InvalidIdFormat() {
        // when: API 호출
        webTestClient.delete()
                .uri(baseUrl + "/abc")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
