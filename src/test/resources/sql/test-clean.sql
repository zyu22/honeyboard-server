/* 테이블 데이터 삭제 */
SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

/* 자식 테이블부터 삭제 */
DELETE FROM bookmark;
DELETE FROM track_team_member;
DELETE FROM track_excluded_member;
DELETE FROM track_project_board;
DELETE FROM track_team;
DELETE FROM track_project;
DELETE FROM finale_team_member;
DELETE FROM finale_excluded_member;
DELETE FROM finale_project_board;
DELETE FROM finale_project;
DELETE FROM finale_team;
DELETE FROM schedule;
DELETE FROM chat;
DELETE FROM youtube;
DELETE FROM web_recommend;
DELETE FROM web_guide;
DELETE FROM algorithm_problem_solution;
DELETE FROM algorithm_problem_tag;
DELETE FROM algorithm_problem;
DELETE FROM oauth2;
DELETE FROM algorithm_guide;

/* 부모 테이블 삭제 */
DELETE FROM user;
DELETE FROM generation;
DELETE FROM tag;
DELETE FROM programming_language;

/* Auto Increment 초기화 */
ALTER TABLE bookmark AUTO_INCREMENT = 1;
ALTER TABLE track_team_member AUTO_INCREMENT = 1;
ALTER TABLE track_excluded_member AUTO_INCREMENT = 1;
ALTER TABLE track_project_board AUTO_INCREMENT = 1;
ALTER TABLE track_team AUTO_INCREMENT = 1;
ALTER TABLE track_project AUTO_INCREMENT = 1;
ALTER TABLE finale_team_member AUTO_INCREMENT = 1;
ALTER TABLE finale_excluded_member AUTO_INCREMENT = 1;
ALTER TABLE finale_project_board AUTO_INCREMENT = 1;
ALTER TABLE finale_project AUTO_INCREMENT = 1;
ALTER TABLE finale_team AUTO_INCREMENT = 1;
ALTER TABLE schedule AUTO_INCREMENT = 1;
ALTER TABLE chat AUTO_INCREMENT = 1;
ALTER TABLE youtube AUTO_INCREMENT = 1;
ALTER TABLE web_recommend AUTO_INCREMENT = 1;
ALTER TABLE web_guide AUTO_INCREMENT = 1;
ALTER TABLE algorithm_problem_solution AUTO_INCREMENT = 1;
ALTER TABLE algorithm_problem AUTO_INCREMENT = 1;
ALTER TABLE oauth2 AUTO_INCREMENT = 1;
ALTER TABLE algorithm_guide AUTO_INCREMENT = 1;
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE tag AUTO_INCREMENT = 1;
ALTER TABLE programming_language AUTO_INCREMENT = 1;
ALTER TABLE track_project_board AUTO_INCREMENT = 1;


SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;