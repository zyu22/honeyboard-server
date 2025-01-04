package com.honeyboard.api.youtube.controller;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.youtube.model.Youtube;
import com.honeyboard.api.youtube.model.YoutubeSearchResult;
import com.honeyboard.api.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/youtube")
@RequiredArgsConstructor
@Slf4j
public class YoutubeController {

    private final YoutubeService youtubeService;

    //유튜브 영상 검색
    @GetMapping("/search")
    public YoutubeSearchResult searchYoutubeVideo(@RequestParam String query,
                                                @RequestParam(required = false) String pageToken) {
        log.info("유튜브 영상 검색 요청 - 키워드: {}, pageToken: {}", query, pageToken);

        return youtubeService.searchVideos(query, pageToken);
    }

    //유튜브 영상 저장
    @PostMapping("/playlist")
    public ResponseEntity<?> createPlaylist(@RequestBody Youtube youtube) {
        log.info("유튜브 영상 저장 요청 - 제목: {}", youtube.getTitle());

        youtubeService.addYoutubeVideo(youtube);

        log.info("유튜브 영상 저장 완료 - 영상ID: {}", youtube.getVideoId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //플레이리스트 조회
    @GetMapping("/playlist")
    public ResponseEntity<?> getAllPlaylist(@CurrentUser User user) {
        log.info("플레이리스트 전체 조회 요청 - 기수: {}", user.getGenerationId());

        int generationId = user.getGenerationId();
        List<Youtube> playlist = youtubeService.getAllYoutubeVideos(generationId);

        if(playlist == null || playlist.isEmpty()) {
            log.info("조회된 플레이리스트 없음 - 기수: {}", generationId);
            return ResponseEntity.noContent().build();
        }

        log.info("플레이리스트 조회 완료 - 조회된 비디오 수: {}", playlist.size());
        return ResponseEntity.ok(playlist);
    }

    //유튜브 영상 삭제
    @DeleteMapping("/playlist/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable int id) {
        log.info("유튜브 영상 삭제 요청 - 영상ID: {}", id);

        youtubeService.deleteYoutubeVideo(id);

        log.info("유튜브 영상 삭제 완료 - 영상ID: {}", id);
        return ResponseEntity.ok().build();
    }
}
