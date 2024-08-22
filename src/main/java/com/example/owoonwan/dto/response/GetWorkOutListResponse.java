package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetWorkOutListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWorkOutListResponse {
    List<GetWorkOutListDto> groupList;
}
