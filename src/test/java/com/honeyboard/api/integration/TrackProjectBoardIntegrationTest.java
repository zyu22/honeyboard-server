package com.honeyboard.api.integration;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.config.TestConfig;
import com.honeyboard.api.config.TestRedisConfig;
import com.honeyboard.api.config.TestSecurityConfig;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.user.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/test-clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import({TestConfig.class,
        TestSecurityConfig.class,
        TestRedisConfig.class,
})
public class TrackProjectBoardIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withInitScript("sql/test-init.sql");

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();
    String baseUrl;

    @BeforeEach
    void setup() {
        // 기본 헤더만 설정
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 테스트용 사용자 생성
        User user = User.builder()
                .email("test3@test.com")
                .userId(3)
                .role("USER")
                .generationId(2)
                .build();

        // JWT 토큰 생성
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // RestTemplate에 토큰 설정
        restTemplate.getRestTemplate().getInterceptors().clear();
        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            String cookieValue = String.format("access_token=%s; refresh_token=%s",
                    accessToken, refreshToken);
            request.getHeaders().add("Cookie", cookieValue);
            return execution.execute(request, body);
        });

        // SecurityContext에 인증 정보 설정
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // baseUrl 설정
        baseUrl = "http://localhost:" + port + "/api/v1/project/track";
    }

    @Test
    @DisplayName("default: 컨테이너 실행 확인")
    void defaultTest() {
        // given: MySQL 컨테이너가 실행 중일 때(컨테이너는 이 클래스가 로드될 때 실행됨)
        // when & then: 컨테이너가 실행 중인지 확인
        assertThat(mysql.isRunning())
                .isTrue();
    }

    @Test
    @DisplayName("1. 관통 게시글 조회: 성공")
    void getTrackProjectBoard_Success() {
        // when: API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 1
        );

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull()
        );
    }

    @Test
    @DisplayName("2. 관통 게시글 조회 실패: 존재하지 않는 게시글 ID")
    void getTrackProjectBoard_Fail_NotExistBoardId() {
        // when: 존재하지 않는 게시글 ID로 API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 999
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("3. 관통 게시글 조회 실패: 음수 게시글 ID")
    void getTrackProjectBoard_Fail_NegativeBoardId() {
        // when: 음수 게시글 ID로 API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, -1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("4. 관통 게시글 조회 실패: 0인 게시글 ID")
    void getTrackProjectBoard_Fail_ZeroBoardId() {
        // when: 0인 게시글 ID로 API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 0
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("5. 관통 게시글 조회 실패: 잘못된 형식의 게시글 ID")
    void getTrackProjectBoard_Fail_InvalidBoardIdFormat() {
        // when: 잘못된 형식의 게시글 ID로 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/1/team/1/board/abc",
                HttpMethod.GET,
                null,
                String.class
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("6. 관통 게시글 조회 실패: 삭제된 게시글")
    void getTrackProjectBoard_Fail_DeletedBoard() {
        // given: 게시글 삭제
        restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                1, 1
        );

        // when: 삭제된 게시글 조회
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 1
        );

        // then: 404 Not Found 확인 
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("7. 관통 게시글 조회 실패: 존재하지 않는 팀 ID")
    void getTrackProjectBoard_Fail_NotExistTeamId() {
        // when: 존재하지 않는 팀 ID로 API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 999, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("8. 관통 게시글 조회 실패: 존재하지 않는 프로젝트 ID")
    void getTrackProjectBoard_Fail_NotExistProjectId() {
        // when: 존재하지 않는 프로젝트 ID로 API 호출
        ResponseEntity<TrackProjectBoardDetail> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                999, 1, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("9. 관통 게시글 작성: 성공")
    void createTrackProjectBoard_Success() {
        // given: 테스트용 관통 게시글 작성
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                CreateResponse.class,
                1, 1
        );

        // then: 상태코드 검증 및 생성 검증
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).isNotNull()
        );

        int boardId = response.getBody().getId();

        ResponseEntity<TrackProjectBoardDetail> response2 = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, boardId
        );

        TrackProjectBoardDetail tpbd = response2.getBody();
        assertAll(
                () -> assertThat(tpbd).isNotNull(),
                () -> assertThat(body.getTitle()).isEqualTo(tpbd.getTitle()),
                () -> assertThat(body.getContent()).isEqualTo(tpbd.getContent()),
                () -> assertThat(body.getUrl()).isEqualTo(tpbd.getUrl()),
                () -> assertThat(1).isEqualTo(tpbd.getTrackTeamId())
        );
    }

    @Test
    @DisplayName("10. 관통 게시글 작성: 실패 - 인증되지 않은 사용자")
    void createTrackProjectBoard_Fail_Unauthorized() {
        // given: 인증되지 않은 헤더
        restTemplate.getRestTemplate().getInterceptors().clear();
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                1, 1
        );

        // then: 401 Unauthorized 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("11. 관통 게시글 작성: 실패 - 유효하지 않은 트랙 프로젝트 ID")
    void createTrackProjectBoard_Fail_InvalidTrackProjectId() {
        // given: 유효하지 않은 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                -1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("12. 관통 게시글 작성: 실패 - 제목 누락")
    void createTrackProjectBoard_Fail_NoTitle() {
        // given: 제목이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                null, "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("13. 관통 게시글 작성: 실패 - 내용 누락")
    void createTrackProjectBoard_Fail_NoContent() {
        // given: 내용이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", null, "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("14. 관통 게시글 작성: 실패 - 빈 요청 본문")
    void createTrackProjectBoard_Fail_EmptyRequest() {
        // given: 빈 요청 본문
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(null, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("15. 관통 게시글 작성: 실패 - 존재하지 않는 팀 ID")
    void createTrackProjectBoard_Fail_InvalidTeamId() {
        // given: 존재하지 않는 팀 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                1, 999
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("16. 관통 게시글 작성: 실패 - 존재하지 않는 프로젝트 ID")
    void createTrackProjectBoard_Fail_NotExistTrackProjectId() {
        // given: 존재하지 않는 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board",
                HttpMethod.POST,
                request,
                Void.class,
                999, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("17. 관통 게시글 수정: 성공")
    void updateTrackProjectBoard_Success() {
        // given: 테스트용 관통 게시글 작성
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "update", "update", "update", "update"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, 1
        );

        // then: 상태코드 검증 및 생성 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TrackProjectBoardDetail> response2 = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 1
        );

        TrackProjectBoardDetail tpbd = response2.getBody();
        assertAll(
                () -> assertThat(tpbd).isNotNull(),
                () -> assertThat(body.getTitle()).isEqualTo(tpbd.getTitle()),
                () -> assertThat(body.getContent()).isEqualTo(tpbd.getContent()),
                () -> assertThat(body.getUrl()).isEqualTo(tpbd.getUrl()),
                () -> assertThat(1).isEqualTo(tpbd.getTrackTeamId())
        );
    }

    @Test
    @DisplayName("18. 관통 게시글 수정: 실패 - 인증되지 않은 사용자")
    void updateTrackProjectBoard_Fail_Unauthorized() {
        // given: 인증되지 않은 헤더
        restTemplate.getRestTemplate().getInterceptors().clear();
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, 1
        );

        // then: 401 Unauthorized 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("19. 관통 게시글 수정: 실패 - 유효하지 않은 게시글 ID")
    void updateTrackProjectBoard_Fail_InvalidBoardId() {
        // given: 유효하지 않은 게시글 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, -1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("20. 관통 게시글 수정: 실패 - 존재하지 않는 게시글")
    void updateTrackProjectBoard_Fail_NotFoundBoard() {
        // given: 존재하지 않는 게시글 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, 999
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("21. 관통 게시글 수정: 실패 - 제목 누락")
    void updateTrackProjectBoard_Fail_NoTitle() {
        // given: 제목이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                null, "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("22. 관통 게시글 수정: 실패 - 내용 누락")
    void updateTrackProjectBoard_Fail_NoContent() {
        // given: 내용이 없는 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", null, "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 1, 1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("23. 관통 게시글 수정: 실패 - 존재하지 않는 팀 ID")
    void updateTrackProjectBoard_Fail_NotExistTeamId() {
        // given: 유효하지 않은 팀 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                1, 999, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("24. 관통 게시글 수정: 실패 - 존재하지 않는 프로젝트 ID")
    void updateTrackProjectBoard_Fail_NotExistProjectId() {
        // given: 존재하지 않는 프로젝트 ID로 요청
        TrackProjectBoardRequest body = new TrackProjectBoardRequest(
                "test", "test", "test", "test"
        );
        HttpEntity<TrackProjectBoardRequest> request = new HttpEntity<>(body, headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.PUT,
                request,
                Void.class,
                999, 1, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("25. 관통 게시글 삭제: 성공")
    void deleteTrackProjectBoard_Success() {
        // given: 삭제할 게시글 ID
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 1
        );

        // then: 상태코드 검증 및 삭제 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TrackProjectBoardDetail> response2 = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                HttpMethod.GET,
                null,
                TrackProjectBoardDetail.class,
                1, 1, 1
        );

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("26. 관통 게시글 삭제: 실패 - 인증되지 않은 사용자")
    void deleteTrackProjectBoard_Fail_Unauthorized() {
        // given: 인증되지 않은 헤더
        restTemplate.getRestTemplate().getInterceptors().clear();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 1
        );

        // then: 401 Unauthorized 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("27. 관통 게시글 삭제: 실패 - 유효하지 않은 게시글 ID")
    void deleteTrackProjectBoard_Fail_InvalidBoardId() {
        // given: 유효하지 않은 게시글 ID로 요청
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, -1
        );

        // then: 400 Bad Request 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("28. 관통 게시글 삭제: 실패 - 존재하지 않는 게시글 ID")
    void deleteTrackProjectBoard_Fail_NotExistBoardId() {
        // given: 존재하지 않는 게시글 ID로 요청
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 999
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("29. 관통 게시글 삭제: 실패 - 존재하지 않는 프로젝트 ID")
    void deleteTrackProjectBoard_Fail_NotExistProjectId() {
        // given: 존재하지 않는 프로젝트 ID로 요청
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                999, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("30. 관통 게시글 삭제: 실패 - 이미 삭제된 게시글")
    void deleteTrackProjectBoard_Fail_AlreadyDeleted() {
        // given: 이미 삭제된 게시글 ID로 요청
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: 첫 번째 삭제 요청
        restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 1
        );

        // when: 두 번째 삭제 요청
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 1
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("31. 관통 게시글 삭제: 실패 - 존재하지 않는 팀 ID")
    void deleteTrackProjectBoard_Fail_NotExistTeamId() {
        // given: 존재하지 않는 팀 ID로 요청 
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when: API 호출
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{trackProjectId}/board/{boardId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                1, 999
        );

        // then: 404 Not Found 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
