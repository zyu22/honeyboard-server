/* Generation : 기수 */
CREATE TABLE generation
( -- 0
    id        INT PRIMARY KEY,
    name      INT     NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT false
);

-- User 테이블 생성 (사용자)
CREATE TABLE user
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255),
    name          VARCHAR(100)        NOT NULL,
    generation_id INT                 NOT NULL, /* 기수 */
    role          VARCHAR(20)         NOT NULL DEFAULT 'USER',
    login_type    VARCHAR(20)         NOT NULL,
    is_ssafy      BOOLEAN                      DEFAULT true, /* 싸탈 여부 : false는 싸탈 바꿔도됨*/
    created_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (generation_id)
        REFERENCES generation (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


/* OAuth2 */
CREATE TABLE oauth2
( -- 0
    id          INT PRIMARY KEY AUTO_INCREMENT,
    user_id     INT          NOT NULL,
    provider    VARCHAR(20)  NOT NULL, /* GOOGLE, KAKAO, NAVER */
    provider_id VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY unique_provider_account (provider, provider_id)
);

-- Algorithm_Guide 테이블 생성 (알고리즘 개념) 
CREATE TABLE algorithm_guide
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(255) NOT NULL, /* 알고리즘 제목*/
    content       TEXT         NOT NULL, /* 알고리즘 풀이 - Toast API*/
    thumbnail     VARCHAR(255), /* 썸네일 이미지 URl*/
    user_id       INT          NOT NULL, /* 작성자 ID */
    generation_id INT          NOT NULL, /* 기수 ID */
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted    BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

-- Algorithm_Problem 테이블 생성 (알고리즘 문제)
CREATE TABLE algorithm_problem
( -- 0
    id         INT PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(255)        NOT NULL, /* 문제 이름 */
    url        VARCHAR(255) UNIQUE NOT NULL, /* 문제 링크 */
    user_id    INT                 NOT NULL, /* 작성자 ID */
    created_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN             NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/* Programming Language 테이블 생성 */
CREATE TABLE programming_language
( -- 0
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

--  Algorithm_Problem_Solution 테이블 생성 (알고리즘 해결)
CREATE TABLE algorithm_problem_solution
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    problem_id    INT      NOT NULL, /* 문제 ID */
    title         TEXT     NOT NULL, /* 제목 */
    summary       TEXT     NOT NULL, /* 요약 */
    content       TEXT     NOT NULL, /* 알고리즘 코드 */
    user_id       INT      NOT NULL, /* 작성자 */
    runtime       INT, /* 실행 시간 */
    memory        INT, /* 메모리 */
    language_id   INT      NOT NULL, /* 사용 언어 */
    generation_id INT      NOT NULL, /* 기수 ID */
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted    BOOLEAN  NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (language_id) REFERENCES programming_language (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

--  Tag 테이블 생성 (알고리즘 분류)
CREATE TABLE tag
( -- 0
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    UNIQUE KEY uk_tag_name (name) /* 태그 이름은 동일하지 않게 UNIQUE */
);

/* Algorithm Problem Tag */
CREATE TABLE algorithm_problem_tag
( -- 0
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    algorithm_problem_id INT      NOT NULL, /* 알고리즘 문제 ID */
    tag_id               INT      NOT NULL, /* 문제에 들어갈 TAG ID */
    created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (algorithm_problem_id) REFERENCES algorithm_problem (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_algo_problem_tag (algorithm_problem_id, tag_id)
);

--  Web_Guide 테이블 생성 (웹 개념)
CREATE TABLE web_guide
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(255) NOT NULL,
    content       TEXT         NOT NULL,
    thumbnail     VARCHAR(255), /* 썸네일 이미지 URl*/
    user_id       INT          NOT NULL,
    generation_id INT          NOT NULL, /* 기수 ID */
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted    BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

--  Web_Recommend 테이블 생성 (웹 추천)
CREATE TABLE web_recommend
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(255)        NOT NULL,
    url           VARCHAR(255) UNIQUE NOT NULL, /* 추천 사이트 링크 */
    content       TEXT                NOT NULL,
    user_id       INT                 NOT NULL,
    generation_id INT                 NOT NULL, /* 기수 ID */
    created_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted    BOOLEAN             NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

/* Track Project (관통 프로젝트) */
CREATE TABLE track_project
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(255) NOT NULL, /* 프로젝트 이름 */
    objective     TEXT         NOT NULL, /* 프로젝트 목표 */
    description   TEXT         NOT NULL, /* 프로젝트 설명 */
    thumbnail     VARCHAR(255), /* 썸네일 이미지 URl*/
    user_id       INT          NOT NULL, /* 작성자 id */
    generation_id INT          NOT NULL, /* 기수 ID */
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted    BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

/* Track Team (관통 프로젝트 팀) */
CREATE TABLE track_team
( -- 0
    id               INT PRIMARY KEY AUTO_INCREMENT,
    generation_id    INT      NOT NULL,
    track_project_id INT      NOT NULL, /* 어떤 관통프로젝트의 팀인지 구분 : board아니고 project임*/
    is_completed     BOOLEAN  NOT NULL DEFAULT false,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (generation_id) REFERENCES generation (id),
    FOREIGN KEY (track_project_id) REFERENCES track_project (id)
);

/* Track Project Board (하위 프로젝트) */
CREATE TABLE track_project_board
( -- 0
    id               INT PRIMARY KEY AUTO_INCREMENT,
    track_project_id INT          NOT NULL, /* 상위 관통 프로젝트 ID */
    track_team_id    INT          NOT NULL, /* 수행하는 팀 ID */
    url              VARCHAR(255) NOT NULL, /* gitlab 주소 */
    title            VARCHAR(255) NOT NULL, /* 하위 프로젝트 제목 */
    content          TEXT         NOT NULL, /* 하위 프로젝트 내용 */
    thumbnail        VARCHAR(255), /* 썸네일 이미지 URl*/
    user_id          INT          NOT NULL, /* 작성자 ID */
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted       BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (track_project_id) REFERENCES track_project (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (track_team_id) REFERENCES track_team (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Track_Team_member 테이블 생성
CREATE TABLE track_team_member
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    track_team_id INT  NOT NULL,
    user_id       INT  NOT NULL,
    role          ENUM('leader', 'member') DEFAULT 'member',
    created_at    DATE NOT NULL DEFAULT (CURRENT_DATE),
    FOREIGN KEY (track_team_id) REFERENCES track_team (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    UNIQUE KEY unique_track_team_member (track_team_id, user_id)
);

/* Finale Team (최종 프로젝트 팀) */
CREATE TABLE finale_team
( -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    generation_id INT      NOT NULL,
    submitted_at  DATE     NOT NULL DEFAULT (CURRENT_DATE), /* 제출 날짜 - 매일매일 제출 체크하기 위해 제출 날짜로 비교 */
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (generation_id) REFERENCES generation (id)
);

-- track_excluded_member (관통 프로젝트 제외 인원)
CREATE TABLE track_excluded_member
( -- 0
    id         INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL, /* 생성된 track 프로젝트 ID */
    user_id    INT NOT NULL, /* 제외된 사용자 ID */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_id) REFERENCES track_project (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/* Finale Team Member */
CREATE TABLE finale_team_member
( -- 0
    id             INT PRIMARY KEY AUTO_INCREMENT,
    finale_team_id INT      NOT NULL,
    user_id        INT      NOT NULL,
    role           ENUM('leader', 'member') DEFAULT 'member',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (finale_team_id) REFERENCES finale_team (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    UNIQUE KEY unique_final_team_member (finale_team_id, user_id)
);

/* Finale Project (최종 프로젝트 보드) */
CREATE TABLE finale_project
( -- 0
    id          INT PRIMARY KEY AUTO_INCREMENT,
    team_id     INT          NOT NULL, /* Team ID */
    url         VARCHAR(255) NOT NULL, /* gitlab 주소 */
    title       VARCHAR(255) NOT NULL, /* 프로젝트 제목 */
    description TEXT         NOT NULL, /* 프로젝트 내용 */
    thumbnail   VARCHAR(255) NOT NULL, /* 썸네일 이미지 URl*/
    user_id     INT          NOT NULL, /* 작성자 ID */
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted  BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (team_id) REFERENCES finale_team (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- finale_excluded_member (파이널 프로젝트 제외 인원)
CREATE TABLE finale_excluded_member
( -- 0
    id         INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL, /* 생성된 final 프로젝트 ID */
    user_id    INT NOT NULL, /* 제외된 사용자 ID */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_id) REFERENCES finale_project (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/* Finale Project (최종 프로젝트 보드) */
-- Finale_Project_Board (파이널 하위 프로젝트 - 사용자 생성)
CREATE TABLE finale_project_board
( -- 0
    id                INT PRIMARY KEY AUTO_INCREMENT,
    finale_project_id INT          NOT NULL, /* Project ID */
    finale_team_id    INT          NOT NULL, /* team ID*/
    summary           VARCHAR(255) NOT NULL, /* 요약 */
    title             VARCHAR(255) NOT NULL, /* 프로젝트 제목 */
    content           TEXT         NOT NULL, /* 프로젝트 내용 */
    thumbnail         VARCHAR(255), /* 썸네일 이미지 URl*/
    user_id           INT          NOT NULL, /* 작성자 ID */
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted        BOOLEAN      NOT NULL DEFAULT false, /* 삭제 여부 */
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (finale_team_id) REFERENCES finale_team (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Bookmark 테이블 생성 
CREATE TABLE bookmark
( -- 0
    id           INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT      NOT NULL, /* 사용자 ID */
    content_type ENUM('ALGO_GUIDE', 'ALGO_SOLUTION', 'WEB_GUIDE', 'WEB_RECOMMEND') NOT NULL, /* 어떤거 북마크했나 */
    content_id   INT      NOT NULL,
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_bookmark (user_id, content_type, content_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

--  Youtube 테이블 생성
CREATE TABLE youtube
(                                              -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    video_id      VARCHAR(50) UNIQUE NOT NULL, -- 유튜브 영상 고유 ID
    title         VARCHAR(255)       NOT NULL, -- 영상 제목
    channel       VARCHAR(255)       NOT NULL, -- 영상 채널
    generation_id INT                NOT NULL, -- 영상을 저장한 기수
    created_at    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--  Chat 테이블 생성
CREATE TABLE chat
( -- 0
    id            int          NOT NULL AUTO_INCREMENT,
    content       text         NOT NULL,
    user_id       int          NOT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sender        varchar(100) NOT NULL,
    generation_id int          NOT NULL,
    PRIMARY KEY (id),
    KEY           fk_chat_generation (generation_id),
    KEY           chat_ibfk_1 (user_id),
    CONSTRAINT chat_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chat_generation FOREIGN KEY (generation_id) REFERENCES generation (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- schedule Table
CREATE TABLE schedule
(                                                     -- 0
    id            INT PRIMARY KEY AUTO_INCREMENT,
    content       VARCHAR(255) NOT NULL,
    start_date    DATE         NOT NULL,
    end_date      DATE         NOT NULL,
    schedule_type ENUM('NORMAL', 'PROJECT') NOT NULL, -- '일정 타입 (NORMAL:일반일정, PROJECT:프로젝트)'
    is_public     BOOLEAN,                            -- '공개여부 (일반일정만 해당)'
    user_id       INT          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- 여기부터는 쓴건지 아닌지 잘 모르겠어서 일단 넣어만 뒀습니다.
-- 1. 유저 검색
-- CREATE INDEX idx_user_name ON user(name); 
/* 사용자 이름으로 검색할 때 사용 */

-- 2. 알고리즘 가이드 검색
-- CREATE INDEX idx_algorithm_guide_title_created_at ON algorithm_guide(title, created_at);
/* 알고리즘 가이드를 제목으로 검색하고 최신순 정렬 */

-- 3. 알고리즘 문제 검색
-- CREATE INDEX idx_algorithm_problem_title_created_at ON algorithm_problem(title, created_at);
/* 알고리즘 문제를 제목으로 검색하고 최신순 정렬 */

-- 4. 알고리즘 풀이 검색 (다중 정렬 기준)
-- 4-1. 기본 최신순 정렬
-- CREATE INDEX idx_algorithm_problem_solution_created_at ON algorithm_problem_solution(created_at);

-- 4-2. 제목으로 검색 + 최신순
-- CREATE INDEX idx_solution_title_date ON algorithm_problem_solution(title, created_at DESC);

-- 4-3. 작성자로 검색 + 최신순
-- CREATE INDEX idx_solution_user_date ON algorithm_problem_solution(user_id, created_at DESC);

-- 4-4. 메모리 기준 정렬 + 최신순
-- CREATE INDEX idx_solution_memory_date ON algorithm_problem_solution(memory, created_at DESC);

-- 4-5. 실행시간 기준 정렬 + 최신순
-- CREATE INDEX idx_solution_runtime_date ON algorithm_problem_solution(runtime, created_at DESC);

-- 5. 웹 가이드 검색
-- CREATE INDEX idx_web_guide_title_created_at ON web_guide(title, created_at);
/* 웹 가이드를 제목으로 검색하고 최신순 정렬 */

-- 6. 프로젝트 관련 검색
-- 6-1. 트랙 프로젝트
-- CREATE INDEX idx_track_project_title_created_at ON track_project(title, created_at);

-- 6-2. 트랙 유닛 프로젝트
-- CREATE INDEX idx_track_unit_project_title_created_at ON track_unit_project(title, created_at);

-- 6-3. 파이널 프로젝트
-- CREATE INDEX idx_final_project_title_created_at ON final_project(title, created_at);

-- 7. 팀 검색
-- CREATE INDEX idx_team_project_type_id ON team(project_type, project_id);
/* 프로젝트 유형과 ID로 팀 검색 */

-- 8. 태그 관련 검색
-- CREATE INDEX idx_tag_name ON tag(name);
-- CREATE INDEX idx_algorithm_problem_tag_problem_tag ON algorithm_problem_tag(algorithm_problem_id, tag_id);

-- 9. 북마크 검색
-- CREATE INDEX idx_bookmark_user_content ON bookmark(user_id, content_type, content_id);
/* 사용자별 북마크 콘텐츠 검색 */

-- /* 테스트용 데이터 */
-- /* Tag 데이터 */
-- INSERT INTO tag (id, name)
-- VALUES (1, 'test1'),
--        (2, 'test2'),
--        (3, 'test3'),
--        (4, 'test4'),
--        (5, 'test5');
--
-- /* Programming Language 데이터 */
-- INSERT INTO programming_language (id, name)
-- VALUES (1, 'test1'),
--        (2, 'test2'),
--        (3, 'test3'),
--        (4, 'test4');
--
-- /* Generation 데이터 */
-- INSERT INTO generation (id, name, is_active)
-- VALUES (1, 1, false),
--        (2, 2, true);
--
-- /* 테스트용 유저 데이터 */
-- INSERT INTO user (email, password, name, generation_id, role, login_type, is_ssafy)
-- VALUES ('test1@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'USER', 'EMAIL',
--         true),
--        ('test2@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'USER', 'EMAIL',
--         true),
--        ('test3@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 2, 'USER', 'EMAIL',
--         true),
--        ('test4@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'ADMIN', 'EMAIL',
--         true);
--
-- /* 알고리즘 가이드 테스트 데이터 */
-- INSERT INTO algorithm_guide (title, content, thumbnail, user_id, generation_id)
-- VALUES ('test', 'test', 'test', 1, 1),
--        ('test', 'test', 'test', 1, 1),
--        ('test', 'test', 'test', 2, 1),
--        ('test', 'test', 'test', 3, 2);
--
-- /* 알고리즘 문제 테스트 데이터 */
-- INSERT INTO algorithm_problem (title, url, user_id)
-- VALUES ('test', 'test1', 1),
--        ('test', 'test2', 1),
--        ('test', 'test3', 2),
--        ('test', 'test4', 3);
--
-- /* 알고리즘 문제 태그 연결 */
-- INSERT INTO algorithm_problem_tag (algorithm_problem_id, tag_id)
-- VALUES (1, 1),
--        (2, 2),
--        (3, 3),
--        (4, 4);
--
-- /* 알고리즘 풀이 */
-- INSERT INTO algorithm_problem_solution (problem_id, title, summary, content, user_id, runtime, memory, language_id,
--                                         generation_id)
-- VALUES (1, 'test', 'test', 'test', 1, 100, 256, 1, 1),
--        (1, 'test', 'test', 'test', 2, 120, 512, 1, 1),
--        (2, 'test', 'test', 'test', 3, 150, 1024, 2, 2),
--        (3, 'test', 'test', 'test', 1, 80, 128, 1, 1);
--
-- /* 웹 가이드 */
-- INSERT INTO web_guide (title, content, thumbnail, user_id, generation_id)
-- VALUES ('test', 'test', 'test', 1, 1),
--        ('test', 'test', 'test', 2, 1),
--        ('test', 'test', 'test', 3, 2);
--
-- /* 웹 추천 사이트 */
-- INSERT INTO web_recommend (title, url, content, user_id, generation_id)
-- VALUES ('test', 'test1', 'test', 1, 1),
--        ('test', 'test2', 'test', 2, 1),
--        ('test', 'test3', 'test', 3, 2);
--
-- /* Youtube 데이터 */
-- INSERT INTO youtube (video_id, title, channel, generation_id)
-- VALUES ('test1', 'test', 'test', 1),
--        ('test2', 'test', 'test', 1),
--        ('test3', 'test', 'test', 2);
--
-- /* Chat 데이터 */
-- INSERT INTO chat (content, user_id, sender, generation_id)
-- VALUES ('test', 1, 'test', 1),
--        ('test', 2, 'test', 1),
--        ('test', 3, 'test', 2);
--
-- /* Schedule 데이터 */
-- INSERT INTO schedule (content, start_date, end_date, schedule_type, is_public, user_id)
-- VALUES ('test', '2024-03-01', '2024-03-31', 'NORMAL', true, 1),
--        ('test', '2024-04-01', '2024-05-31', 'PROJECT', true, 2),
--        ('test', '2024-03-15', '2024-03-16', 'NORMAL', false, 3);
--
-- /* Track Project 데이터 */
-- INSERT INTO track_project (title, objective, description, thumbnail, user_id, generation_id)
-- VALUES ('test', 'test', 'test', 'test', 1, 1),
--        ('test', 'test', 'test', 'test', 2, 1);
--
-- /* Track Team 데이터 */
-- INSERT INTO track_team (generation_id, track_project_id, is_completed)
-- VALUES (1, 1, true),
--        (1, 2, false);
--
-- /* Track Team Member 데이터 */
-- INSERT INTO track_team_member (track_team_id, user_id, role)
-- VALUES (1, 1, 'leader'),
--        (1, 2, 'member'),
--        (2, 2, 'leader'),
--        (2, 3, 'member');