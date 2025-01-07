package com.honeyboard.api.common.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 생성 후 생성된 게시글의 id를 반환해줌
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponse {
    private int id;
}
