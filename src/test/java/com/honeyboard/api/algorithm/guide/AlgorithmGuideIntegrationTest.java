package com.honeyboard.api.algorithm.guide;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.config.*;

import com.honeyboard.api.util.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.common.response.PageResponse;

class AlgorithmGuideIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("1. 알고리즘 개념 전체 조회(검색어 X): 성공")
    void getAllAlgorithmGuides_Success() {
        // given: API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 응답 검증
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().size()).isEqualTo(4));

        // then: 생성일자 기준 내림차순 정렬 확인
        List<AlgorithmGuideList> content = list.getContent();
        for (int i = 0; i < content.size() - 1; i++) {
            LocalDateTime a = LocalDateTime.parse(content.get(i).getCreatedAt(), TestConfig.formatter);
            LocalDateTime b = LocalDateTime.parse(content.get(i + 1).getCreatedAt(), TestConfig.formatter);
            assertThat(a).isAfterOrEqualTo(b);
        }
    }

    @Test
    @DisplayName("2. 알고리즘 개념 전체 조회(검색어 X) 실패: 잘못된 페이지 번호")
    void getAllAlgorithmGuides_Fail_InvalidPage() {
        // given: 잘못된 페이지 번호로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?currentPage=0";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        // then: 500 에러 확인
        // 이 경우 400 에러를 반환하는게 적절한데.... 500 에러 반환하네요
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("3. 알고리즘 개념 전체 조회(검색어 X) 실패: 잘못된 페이지 크기")
    void getAllAlgorithmGuides_Fail_InvalidPageSize() {
        // given: 잘못된 페이지 크기로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?pageSize=0";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        // then: 500 에러 확인
        // 200 을 반환하는게 맞는지..?
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("4. 알고리즘 개념 전체 조회(검색어 X) 실패: 존재하지 않는 기수")
    void getAllAlgorithmGuides_Fail_InvalidGeneration() {
        // given: 존재하지 않는 기수로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?generationId=999";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 빈 리스트 반환 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().isEmpty()).isTrue());
    }

    @Test
    @DisplayName("5. 알고리즘 개념 전체 조회(검색어 X) 실패: 데이터가 없는 페이지")
    void getAllAlgorithmGuides_Fail_EmptyPage() {
        // given: 존재하지 않는 페이지로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?currentPage=999";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 빈 리스트 반환 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().isEmpty()).isTrue());
    }

    @Test
    @DisplayName("6. 알고리즘 개념 전체 조회(검색어 O) - 성공")
    void getAllAlgorithmGuides_Success_Search() {
        // given: 검색어로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?keyword=test";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 검색 결과 및 정렬 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().size()).isEqualTo(4));

        List<AlgorithmGuideList> content = list.getContent();
        for (int i = 0; i < content.size() - 1; i++) {
            assertThat(content.get(i).getTitle().contains("test")).isTrue();
            LocalDateTime a = LocalDateTime.parse(content.get(i).getCreatedAt(), TestConfig.formatter);
            LocalDateTime b = LocalDateTime.parse(content.get(i + 1).getCreatedAt(), TestConfig.formatter);
            assertThat(a).isAfterOrEqualTo(b);
        }

        assertThat(content.get(content.size() - 1).getTitle().contains("test")).isTrue();
    }

    @Test
    @DisplayName("7. 알고리즘 개념 전체 조회(검색어 O) - 실패 (빈 검색어)")
    void getAllAlgorithmGuides_Fail_EmptyTitle() {
        // given: 빈 검색어로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?keyword=";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list1 = algorithmGuideService.getAlgorithmGuides(1, 8, 0);
        PageResponse<AlgorithmGuideList> list2 = response.getBody();

        // then: 전체 리스트와 동일한 결과 반환 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list2).isNotNull(),
                () -> assertThat(list2.getContent().size()).isEqualTo(list1.getContent().size()));
    }

    /* AlgorithmGuide 공백, 특수문자 검색어 검증이 의도된 게 아니라면 백엔드에서도 검증해야 합니다 */
    @Test
    @DisplayName("8. 알고리즘 개념 전체 조회(검색어 O) - 실패 (공백 검색어)")
    void getAllAlgorithmGuides_Fail_BlankTitle() {
        // given: 공백 검색어로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?keyword= ";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        // then: 200 응답 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("9. 알고리즘 개념 전체 조회(검색어 O) - 실패 (존재하지 않는 검색어)")
    void getAllAlgorithmGuides_Fail_NotExistTitle() {
        // given: 존재하지 않는 검색어로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?title=notexist";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 빈 리스트 반환 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().isEmpty()).isTrue());
    }

    @Test
    @DisplayName("10. 알고리즘 개념 전체 조회(검색어 O) - 실패 (특수문자 검색어)")
    void getAllAlgorithmGuides_Fail_SpecialCharacterKeyword() {
        // given: 특수문자 검색어로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide?title=!@#$%";

        // when: API 호출
        ResponseEntity<PageResponse<AlgorithmGuideList>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<AlgorithmGuideList>>() {
                });

        PageResponse<AlgorithmGuideList> list = response.getBody();

        // then: 빈 리스트 반환 확인
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.getContent().isEmpty()).isTrue());
    }

    @Test
    @DisplayName("11. 알고리즘 개념 단건 조회 - 성공")
    void getAlgorithmGuide_Success() {
        // given: 존재하는 알고리즘 가이드 ID로 API 엔드포인트 설정
        int algorithmGuideId = 4;
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/" + algorithmGuideId;

        // when: API 호출
        ResponseEntity<AlgorithmGuideDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 응답 검증
        AlgorithmGuideDetail algorithmGuide = response.getBody();

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(algorithmGuide).isNotNull(),
                () -> assertThat(algorithmGuide.getId()).isEqualTo(algorithmGuideId));
    }

    @Test
    @DisplayName("12. 알고리즘 개념 단건 조회 - 실패 (음수 ID)")
    void getAlgorithmGuide_Fail_NegativeId() {
        // given: 음수 ID로 API 엔드포인트 설정
        int algorithmGuideId = -1;
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/" + algorithmGuideId;

        // when: API 호출
        ResponseEntity<AlgorithmGuideDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("13. 알고리즘 개념 단건 조회 - 실패 (0인 ID)")
    void getAlgorithmGuide_Fail_ZeroId() {
        // given: 0인 ID로 API 엔드포인트 설정
        int algorithmGuideId = 0;
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/" + algorithmGuideId;

        // when: API 호출
        ResponseEntity<AlgorithmGuideDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /* 없는 id는 204가 아니라 404를 반환하는게 적절할 것 같습니다 */
    @Test
    @DisplayName("14. 알고리즘 개념 단건 조회 - 실패 (존재하지 않는 큰 ID)")
    void getAlgorithmGuide_Fail_NonExistentLargeId() {
        // given: 존재하지 않는 큰 ID로 API 엔드포인트 설정
        int algorithmGuideId = 999999;
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/" + algorithmGuideId;

        // when: API 호출
        ResponseEntity<AlgorithmGuideDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 404 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("15. 알고리즘 개념 단건 조회 - 실패 (잘못된 형식의 ID)")
    void getAlgorithmGuide_Fail_InvalidIdFormat() {
        // given: 잘못된 형식의 ID로 API 엔드포인트 설정
        String invalidId = "abc";
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/" + invalidId;

        // when: API 호출
        ResponseEntity<AlgorithmGuideDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("16. 알고리즘 개념 생성 - 성공")
    void createAlgorithmGuide_Success() {
        // given: API 엔드포인트와 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        AlgorithmGuideRequest body = new AlgorithmGuideRequest("test", "test", "test");

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                CreateResponse.class);

        // then: 응답 검증
        assertThat(response.getBody().getId() > 0).isTrue();
        AlgorithmGuideDetail guide = algorithmGuideService.getAlgorithmGuideDetail(response.getBody().getId(),
                3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(guide).isNotNull(),
                () -> assertThat(guide.getTitle()).isNotNull(),
                () -> assertThat(guide.getContent()).isNotNull());
    }

    @Test
    @DisplayName("17. 알고리즘 개념 생성 - 실패 (제목 누락)")
    void createAlgorithmGuide_Fail_NoTitle() {
        // given: 제목이 누락된 요청 바디로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(null, "test", "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<CreateResponse>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("18. 알고리즘 개념 생성 - 실패 (내용 누락)")
    void createAlgorithmGuide_Fail_NoContent() {
        // given: 내용이 누락된 요청 바디로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("test", null, "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<CreateResponse>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("19. 알고리즘 개념 생성 - 실패 (빈 제목)")
    void createAlgorithmGuide_Fail_EmptyTitle() {
        // given: 빈 제목으로 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("", "test", "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<CreateResponse>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("20. 알고리즘 개념 생성 - 실패 (빈 내용)")
    void createAlgorithmGuide_Fail_EmptyContent() {
        // given: 빈 내용으로 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest("test", "", "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<CreateResponse>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("21. 알고리즘 개념 생성 - 실패 (제목 최대 길이 초과)")
    void createAlgorithmGuide_Fail_TitleTooLong() {
        // given: 최대 길이를 초과하는 제목으로 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide";
        String longTitle = "a".repeat(256);
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(longTitle, "test", "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<CreateResponse>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("22. 알고리즘 개념 수정 - 성공")
    void updateAlgorithmGuide_Success() {
        // given: 수정할 알고리즘 가이드 ID와 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: 수정 API 호출
        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 수정 성공 확인
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // when: 수정된 데이터 조회
        ResponseEntity<AlgorithmGuideDetail> getResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AlgorithmGuideDetail>() {
                });

        // then: 수정된 데이터 검증
        AlgorithmGuideDetail updatedGuide = getResponse.getBody();
        assertAll(
                () -> assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(updatedGuide).isNotNull(),
                () -> assertThat(updatedGuide.getTitle()).isEqualTo("updated title"),
                () -> assertThat(updatedGuide.getContent()).isEqualTo("updated content"));
    }

    @Test
    @DisplayName("23. 알고리즘 개념 수정 - 실패 (존재하지 않는 ID)")
    void updateAlgorithmGuide_Fail_NonExistentId() {
        // given: 존재하지 않는 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/999999";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 404 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("24. 알고리즘 개념 수정 - 실패 (권한 없음)")
    void updateAlgorithmGuide_Fail_NoPermission() {
        // given: 다른 사용자가 작성한 게시글의 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/1"; // user_id가 1인 사용자의 게시글
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "updated content",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 403 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("25. 알고리즘 개념 수정 - 실패 (제목 누락)")
    void updateAlgorithmGuide_Fail_NoTitle() {
        // given: 제목이 없는 요청 바디로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                null,
                "updated content",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("26. 알고리즘 개념 수정 - 실패 (내용 누락)")
    void updateAlgorithmGuide_Fail_NoContent() {
        // given: 내용이 없는 요청 바디로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                null,
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("27. 알고리즘 개념 수정 - 실패 (빈 제목)")
    void updateAlgorithmGuide_Fail_EmptyTitle() {
        // given: 빈 제목으로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "",
                "updated content",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("28. 알고리즘 개념 수정 - 실패 (빈 내용)")
    void updateAlgorithmGuide_Fail_EmptyContent() {
        // given: 빈 내용으로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(
                "updated title",
                "",
                "updated thumbnail");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("29. 알고리즘 개념 수정 - 실패 (제목 최대 길이 초과)")
    void updateAlgorithmGuide_Fail_TitleTooLong() {
        // given: 최대 길이를 초과하는 제목으로 요청 바디 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/4";
        String longTitle = "a".repeat(256);
        AlgorithmGuideRequest request = new AlgorithmGuideRequest(longTitle, "test", "test");
        HttpEntity<AlgorithmGuideRequest> httpRequest = new HttpEntity<>(request);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("30. 알고리즘 개념 삭제 - 성공")
    void deleteAlgorithmGuide_Success() {
        // given: API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/1";
        HttpEntity<Void> httpRequest = new HttpEntity<>(null);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 200 응답 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("31. 알고리즘 개념 삭제 - 실패 (존재하지 않는 ID)")
    void deleteAlgorithmGuide_Fail_NonExistentId() {
        // given: 존재하지 않는 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/999999";
        HttpEntity<Void> httpRequest = new HttpEntity<>(null);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 404 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("32. 알고리즘 개념 삭제 - 실패 (음수 ID)")
    void deleteAlgorithmGuide_Fail_NegativeId() {
        // given: 음수 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/-1";
        HttpEntity<Void> httpRequest = new HttpEntity<>(null);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("33. 알고리즘 개념 삭제 - 실패 (0인 ID)")
    void deleteAlgorithmGuide_Fail_ZeroId() {
        // given: 0인 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/0";
        HttpEntity<Void> httpRequest = new HttpEntity<>(null);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("34. 알고리즘 개념 삭제 - 실패 (잘못된 형식의 ID)")
    void deleteAlgorithmGuide_Fail_InvalidIdFormat() {
        // given: 잘못된 형식의 ID로 API 엔드포인트 설정
        String url = "http://localhost:" + port + "/api/v1/algorithm/guide/abc";
        HttpEntity<Void> httpRequest = new HttpEntity<>(null);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpRequest,
                new ParameterizedTypeReference<Void>() {
                });

        // then: 400 에러 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
