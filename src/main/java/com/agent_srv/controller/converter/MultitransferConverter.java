package com.agent_srv.controller.converter;

import com.agent_srv.dto.MultitransferDTO;
import com.agent_srv.model.Multitransfer;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultitransferConverter implements AbstractConverter<Multitransfer, MultitransferDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public MultitransferConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        this.modelMapper = modelMapper;
    }

    @Override
    public Multitransfer convertToDM(MultitransferDTO multitransferDTO) {
        if (multitransferDTO == null)
            return null;

        return modelMapper.map(multitransferDTO, Multitransfer.class);    }

    @Override
    public MultitransferDTO convertToDTO(Multitransfer multitransfer) {
        if (multitransfer == null)
            return null;
        return modelMapper.map(multitransfer, MultitransferDTO.class);
    }

}
