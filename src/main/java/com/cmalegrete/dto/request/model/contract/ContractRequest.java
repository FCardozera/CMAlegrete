package com.cmalegrete.dto.request.model.contract;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ContractRequest {
    private List<MultipartFile> file;
    private String token;
}
