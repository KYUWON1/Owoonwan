package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeleteWorkOutGroupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeleteWorkOutGroupResponse {
    private Long groupId;
    public static DeleteWorkOutGroupResponse from(DeleteWorkOutGroupDto dto){
        return new DeleteWorkOutGroupResponse(dto.getGroupId());
    }
}
