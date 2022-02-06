package com.agent_srv.controller.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface AbstractConverter<DM, DTO> {
    DM convertToDM(DTO dto);
    DTO convertToDTO(DM dm);

    default List<DTO> convertToDTOs(List<DM> dms) {
        return dms.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    default List<DM> convertToDMs(List<DTO> dtos) {
        return dtos.stream()
            .map(this::convertToDM)
            .collect(Collectors.toList());
    }
}
