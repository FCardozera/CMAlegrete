package com.cmalegrete.service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cmalegrete.dto.request.model.contract.ContractRequest;
import com.cmalegrete.exception.generic.FileException;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.sendcontracttoken.SendContractTokenRepository;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

@RequiredArgsConstructor
@Service
public class ContractService extends UtilService {

    private final EmailSendService emailSendService;

    private final SendContractTokenRepository sendContractTokenRepository;
    
    public ResponseEntity<Object> sendContract(ContractRequest request) {
        if (request.getFile() == null) {
            throw new FileException("Um arquivo é necessário");
        }

        if (request.getFile().size() > 1) {
            throw new FileException("Só é permitido o envio de 01 arquivo.");
        }

        String contentType = request.getFile().get(0).getContentType();
        if (contentType == null) {
            throw new FileException("Formato de arquivo inválido");
        }

        if (!contentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new FileException("Formato de arquivo inválido");
        }

        MemberEntity member = super.memberRepository.findById(getMemberbyToken(request.getToken()).getId()).get();
        try {
            member.setContract(request.getFile().getFirst().getBytes());
            super.memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Enviar e-mail com arquivo para o circulo militar
        emailSendService.sendContractToTeam(member, request.getFile());

        return ResponseEntity.ok().build();
    }

    private MemberEntity getMemberbyToken(String token) {
        return (MemberEntity)sendContractTokenRepository.findByToken(token).get().getUser();
    }
}
