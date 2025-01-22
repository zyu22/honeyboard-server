/* 테스트용 데이터 */
/* Tag 데이터 */
INSERT INTO tag (id, name)
VALUES (1, 'test1'),
       (2, 'test2'),
       (3, 'test3'),
       (4, 'test4'),
       (5, 'test5');

/* Programming Language 데이터 */
INSERT INTO programming_language (id, name)
VALUES (1, 'test1'),
       (2, 'test2'),
       (3, 'test3'),
       (4, 'test4');

/* Generation 데이터 */
INSERT INTO generation (id, name, is_active)
VALUES (1, 1, false),
       (2, 2, true);

/* 테스트용 유저 데이터 */
INSERT INTO user (email, password, name, generation_id, role, login_type, is_ssafy)
VALUES ('test1@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'USER', 'EMAIL',
        true),
       ('test2@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'USER', 'EMAIL',
        true),
       ('test3@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 2, 'USER', 'EMAIL',
        true),
       ('test4@test.com', '$2a$10$yWtRq0uy1BxqQxZgx1F3.OxVF8A5UwB.a/7p2rWPx0RTOCKIUJCGe', 'test', 1, 'ADMIN', 'EMAIL',
        true);

/* 알고리즘 가이드 테스트 데이터 */
INSERT INTO algorithm_guide (title, content, thumbnail, user_id, generation_id)
VALUES ('test', 'test', 'test', 1, 1),
       ('test', 'test', 'test', 1, 1),
       ('test', 'test', 'test', 2, 1),
       ('test', 'test', 'test', 3, 2);

/* 알고리즘 문제 테스트 데이터 */
INSERT INTO algorithm_problem (title, url, user_id)
VALUES ('test', 'test1', 1),
       ('test', 'test2', 1),
       ('test', 'test3', 2),
       ('test', 'test4', 3);

/* 알고리즘 문제 태그 연결 */
INSERT INTO algorithm_problem_tag (algorithm_problem_id, tag_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

/* 알고리즘 풀이 */
INSERT INTO algorithm_problem_solution (problem_id, title, summary, content, user_id, runtime, memory, language_id,
                                        generation_id)
VALUES (1, 'test', 'test', 'test', 1, 100, 256, 1, 1),
       (1, 'test', 'test', 'test', 2, 120, 512, 1, 1),
       (2, 'test', 'test', 'test', 3, 150, 1024, 2, 2),
       (3, 'test', 'test', 'test', 1, 80, 128, 1, 1);

/* 웹 가이드 */
INSERT INTO web_guide (title, content, thumbnail, user_id, generation_id)
VALUES ('test', 'test', 'test', 1, 1),
       ('test', 'test', 'test', 2, 1),
       ('test', 'test', 'test', 3, 2);

/* 웹 추천 사이트 */
INSERT INTO web_recommend (title, url, content, user_id, generation_id)
VALUES ('test', 'test1', 'test', 1, 1),
       ('test', 'test2', 'test', 2, 1),
       ('test', 'test3', 'test', 3, 2);

/* Youtube 데이터 */
INSERT INTO youtube (video_id, title, channel, generation_id)
VALUES ('test1', 'test', 'test', 1),
       ('test2', 'test', 'test', 1),
       ('test3', 'test', 'test', 2);

/* Chat 데이터 */
INSERT INTO chat (content, user_id, sender, generation_id)
VALUES ('test', 1, 'test', 1),
       ('test', 2, 'test', 1),
       ('test', 3, 'test', 2);

/* Schedule 데이터 */
INSERT INTO schedule (content, start_date, end_date, schedule_type, is_public, user_id)
VALUES ('test', '2024-03-01', '2024-03-31', 'NORMAL', true, 1),
       ('test', '2024-04-01', '2024-05-31', 'PROJECT', true, 2),
       ('test', '2024-03-15', '2024-03-16', 'NORMAL', false, 3);

/* Track Project 데이터 */
INSERT INTO track_project (title, objective, description, thumbnail, user_id, generation_id)
VALUES ('test', 'test', 'test', 'test', 1, 2),
       ('test', 'test', 'test', 'test', 2, 2),
       ('test', 'test', 'test', 'test', 3, 2);

/* Track Team 데이터 */
INSERT INTO track_team (generation_id, track_project_id, is_completed)
VALUES (2, 1, true),
       (2, 2, false);

/* Track Team Member 데이터 */
INSERT INTO track_team_member (track_team_id, user_id, role)
VALUES (1, 1, 'leader'),
       (1, 2, 'member'),
       (2, 2, 'leader'),
       (2, 3, 'member');

/* Track Project Board 데이터 */
INSERT INTO track_project_board (
    id, 
    track_team_id, 
    track_project_id,
    user_id,
    title, 
    url, 
    content,
    thumbnail,
    is_deleted,
    created_at,
    updated_at
) VALUES 
(1, 1, 1, 1, 'test', 'test', 'test', 'test', false, NOW(), NOW()),
(2, 1, 1, 1, 'test', 'test', 'test', 'test', false, NOW(), NOW()),
(3, 2, 2, 2, 'test', 'test', 'test', 'test', false, NOW(), NOW());

/* Track Excluded Member 데이터 */
INSERT INTO track_excluded_member (project_id, user_id)
VALUES (1, 3),
       (2, 1);

/* Finale Team 데이터 */
INSERT INTO finale_team (generation_id, submitted_at)
VALUES (2, CURRENT_DATE),
       (2, CURRENT_DATE);

/* Finale Team Member 데이터 */
INSERT INTO finale_team_member (finale_team_id, user_id, role)
VALUES (1, 1, 'leader'),
       (1, 2, 'member'),
       (2, 2, 'leader'),
       (2, 3, 'member');

/* Finale Project 데이터 */
INSERT INTO finale_project (team_id, url, title, description, thumbnail, user_id)
VALUES (1, 'test', 'test', 'test', 'test', 1),
       (2, 'test', 'test', 'test', 'test', 2);

/* Finale Project Board 데이터 */
INSERT INTO finale_project_board (finale_project_id, finale_team_id, summary, title, content, thumbnail, user_id)
VALUES (1, 1, 'test', 'test', 'test', 'test', 1),
       (1, 1, 'test', 'test', 'test', 'test', 2),
       (2, 2, 'test', 'test', 'test', 'test', 2);

/* Finale Excluded Member 데이터 */
INSERT INTO finale_excluded_member (project_id, user_id) 
VALUES (1, 3),
       (2, 1);

/* Bookmark 데이터 */
INSERT INTO bookmark (user_id, content_type, content_id)
VALUES (1, 'ALGO_GUIDE', 1),
       (1, 'ALGO_SOLUTION', 1),
       (2, 'WEB_GUIDE', 1),
       (2, 'WEB_RECOMMEND', 1);
