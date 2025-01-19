package com.honeyboard.api.youtube.controller;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.youtube.model.request.YoutubeCreate;
import com.honeyboard.api.youtube.model.response.YoutubeList;
import com.honeyboard.api.youtube.model.response.YoutubeResponse;
import com.honeyboard.api.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/youtube")
@RequiredArgsConstructor
@Slf4j
public class YoutubeController {

    private final YoutubeService youtubeService;

    // 유튜브 검색 결과 조회
    @GetMapping("/search")
    public Map<String, Object> searchYoutubeVideo(@RequestParam String query,
                                                  @RequestParam(required = false) String pageToken) {
        log.info("유튜브 영상 검색 요청 - 키워드: {}, pageToken: {}", query, pageToken);

        YoutubeResponse response = youtubeService.searchVideos(query, pageToken);
        Map<String, Object> result = new HashMap<>();

        result.put("List<YoutubeList>", response.getYoutubeList());
        result.put("nextPageToken", response.getNextPageToken());

        return result;
    }

    //유튜브 영상 저장
    @PostMapping("/playlist")
    public ResponseEntity<?> createPlaylist(@RequestBody YoutubeCreate youtube,
                                            @CurrentUser User user) {
        log.info("유튜브 영상 저장 요청 - 제목: {}", youtube.getTitle());

        int generationId = user.getGenerationId();
        youtubeService.addYoutubeVideo(youtube, generationId);

        log.info("유튜브 영상 저장 완료 - 영상ID: {}", youtube.getVideoId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //플레이리스트 조회
    @GetMapping("/playlist")
    public ResponseEntity<?> getAllPlaylist(@CurrentUser User user) {
        log.info("플레이리스트 전체 조회 요청 - 기수: {}", user.getGenerationId());

        int generationId = user.getGenerationId();
        List<YoutubeList> playlist = youtubeService.getAllYoutubeVideos(generationId);

        if(playlist == null || playlist.isEmpty()) {
            log.info("조회된 플레이리스트 없음");
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
