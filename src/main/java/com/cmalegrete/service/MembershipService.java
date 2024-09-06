package com.cmalegrete.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.exception.generic.EmailAlreadyRegisteredException;
import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MembershipService extends UtilService {

    private final EmailSendService emailSendService;

    private final DocumentService documentService;

    @Value("${spring.mail.enable}")
    private boolean mailEnabled;

    public ResponseEntity<Object> sendMembershipRequest(MemberRegisterRequest request) {
        verifyIfUserExists(request);
        UserEntity member = createNewMember(request);
        super.userRepository.save(member);
        logMemberRegistration(member);

        // Gerar o contrato com as informações fornecidas
        String caminhoContrato = gerarContrato(request);

        if (mailEnabled) {
            // Envia o e-mail com o contrato anexado para o novo membro
            emailSendService.sendConfirmationEmailToUser(request, caminhoContrato);
            // Envia o alerta para a equipe do clube
            emailSendService.sendAlertEmailToTeam(request);
        }

        return ResponseEntity.ok().build();
    }

    // Verifica se o usuário já está registrado
    private void verifyIfUserExists(MemberRegisterRequest request) {
        if (super.userExists(request)) {
            throw new EmailAlreadyRegisteredException("Email registration attempt: " + request.getEmail());
        }
    }

    // Cria um novo objeto MemberEntity a partir da requisição
    private UserEntity createNewMember(MemberRegisterRequest request) {
        return new MemberEntity(request);
    }

    // Registra o log da inscrição do membro
    private void logMemberRegistration(UserEntity member) {
        log(LogEnum.INFO, "Member registered: " + member.getId(), HttpStatus.CREATED.value());
    }
    
    // Gera o contrato em PDF com as substituições especificadas
    private String gerarContrato(MemberRegisterRequest request) {
        Map<String, String> replacements = new HashMap<>();

        // Substituições de texto no contrato
        replacements.put("#NOMEMAIUSCULO#", request.getName().toUpperCase());
        replacements.put("#NOME#", Arrays.stream(request.getName().split("\\s+"))
            .map(StringUtils::capitalize)
            .collect(Collectors.joining(" ")));
        replacements.put("#ENDERECO#", request.getAddress());
        replacements.put("#CPF#", UtilService.formatarCpf(request.getCpf()));
        replacements.put("#DATA#", getDataAtualPorExtenso());
    
        return documentService.replaceTextAndConvertToPdf(replacements, request.getName());
    }

    // Método utilitário para obter a data atual por extenso
    private String getDataAtualPorExtenso() {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("pt", "BR"));
        return dataAtual.format(formatter);
    }
}
