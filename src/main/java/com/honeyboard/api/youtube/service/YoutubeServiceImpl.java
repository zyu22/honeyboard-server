package com.honeyboard.api.youtube.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.youtube.mapper.YoutubeMapper;
import com.honeyboard.api.youtube.model.request.YoutubeCreate;
import com.honeyboard.api.youtube.model.response.YoutubeList;
import com.honeyboard.api.youtube.model.response.YoutubeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService {

    private final YoutubeMapper youtubeMapper;
    @Value("${YOUTUBE_API_KEY}")
    private String apiKey;

    // 유튜브 검색 결과 조회
    @Override
    public YoutubeResponse searchVideos(String query, String pageToken) {
        log.info("유튜브 영상 검색 시작 - 검색어: {}, pageToken: {}", query, pageToken);

        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("YouTube API 키가 설정되지 않았습니다.");
        }

        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                request -> {}
        ).setApplicationName("youtube-search").build();

        try {
            // 검색 요청 설정
            YouTube.Search.List search = youtube.search()
                    .list(Collections.singletonList("snippet"))
                    .setQ(query)
                    .setType(Collections.singletonList("video"))
                    .setMaxResults(9L)  // 9개씩 검색
                    .setKey(apiKey);

            // pageToken이 있으면 설정
            if (pageToken != null && !pageToken.isEmpty()) {
                search.setPageToken(pageToken);
            }

            SearchListResponse searchResponse = search.execute();

            if (searchResponse == null || searchResponse.getItems() == null) {
                throw new IllegalArgumentException("검색 결과를 가져올 수 없습니다.");
            }

            List<YoutubeList> youtubeList = new ArrayList<>();
            for(SearchResult item : searchResponse.getItems()) {
                if (item.getId() == null || item.getId().getVideoId() == null || item.getSnippet() == null) {
                    log.warn("잘못된 형식의 검색 결과를 건너뜁니다.");
                    continue;
                }

                YoutubeList video = new YoutubeList();
                video.setVideoId(item.getId().getVideoId());
                video.setTitle(item.getSnippet().getTitle());
                video.setChannel(item.getSnippet().getChannelTitle());
                youtubeList.add(video);
            }

            YoutubeResponse response  = new YoutubeResponse ();
            response .setYoutubeList(youtubeList);
            response .setNextPageToken(searchResponse.getNextPageToken());

            log.info("유튜브 영상 검색 완료 - 검색된 영상 수: {}, 다음 페이지 토큰: {}", youtubeList.size(), searchResponse.getNextPageToken());

            return response;

        } catch (IOException e) {
            log.error("YouTube API 호출 중 오류 발생", e);
            throw new IllegalArgumentException("YouTube API 호출 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생", e);
            throw new RuntimeException("YouTube 검색 중 예기치 않은 오류가 발생했습니다.");
        }
    }

    //유튜브 영상 저장
    @Override
    public void addYoutubeVideo(YoutubeCreate youtube, int generationId) {
        log.info("유튜브 영상 저장 시작 - 제목: {}", youtube.getTitle());

        if (youtube == null) {
            throw new IllegalArgumentException("영상 정보가 없습니다.");
        }

        if (youtubeMapper.existsByVideoId(youtube.getVideoId(), generationId) > 0) {
            throw new BusinessException(ErrorCode.DUPLICATE_VIDEO);
        }

        int result = youtubeMapper.insertYoutubeVideo(youtube, generationId);

        if (result != 1) {
            throw new IllegalArgumentException("영상 저장에 실패했습니다.");
        }

        log.info("유튜브 영상 저장 완료 - videoId: {}", youtube.getVideoId());
    }

    // 플레이리스트 조회
    @Override
    public List<YoutubeList> getAllYoutubeVideos(int generationId) {
        log.info("플레이리스트 조회 시작 - 기수: {}", generationId);

        List<YoutubeList> youtubeList = youtubeMapper.selectAllYoutubeVideo(generationId);

        log.info("플레이리스트 조회 완료 - 조회된 영상 수: {}", youtubeList.size());
        return youtubeList;
    }

    // 플레이리스트 삭제
    @Override
    public void deleteYoutubeVideo(int id) {
        log.info("플레이리스트에서 영상 삭제 시작 - youtubeId: {}", id);

        if (id <= 0) {
            throw new IllegalArgumentException("삭제할 영상 ID가 없습니다.");
        }

        int result = youtubeMapper.deleteYoutubeVideo(id);

        if (result != 1) {
            throw new IllegalArgumentException("영상 삭제에 실패했습니다.");
        }

        log.info("플레이리스트에서 영상 삭제 완료 - videoId: {}", id);
    }
}
