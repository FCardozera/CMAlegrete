package com.cmalegrete.service.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.cmalegrete.dto.request.model.util.RequestEmail;
import com.cmalegrete.exception.generic.UnauthorizedUserException;
import com.cmalegrete.exception.handler.util.HandlerExceptionUtil;
import com.cmalegrete.model.member.MemberRepository;
import com.cmalegrete.model.sendcontracttoken.SendContractTokenRepository;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRepository;
import com.cmalegrete.model.user.UserRoleEnum;

public abstract class UtilService extends HandlerExceptionUtil {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public SendContractTokenRepository sendContractTokenRepository;

    public static final String UNAUTHORIZED_ACESS_ATTEMPT = "Unauthorized access attempt";
    public static final String UNAUTHORIZED_ACESS_ATTEMPT_DOTS = "Unauthorized access attempt: ";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
    private static final int PASSWORD_LENGTH = 8;

    public boolean userExists(RequestEmail request) {
        return userRepository.findByEmail(request.getEmail()) != null;
    }

    public boolean userExists(Authentication authentication) {
        return userRepository.findByEmail(((UserEntity) authentication.getPrincipal()).getEmail()) != null;
    }

    public boolean tokenExists(String token) {
        return  !sendContractTokenRepository.findByToken(token).isEmpty();
    }

    public void verifyAuthorization(Authentication authentication, UUID id) {
        if (!userIsSameOrAdmin(authentication, id)) {
            throw new UnauthorizedUserException(
                    UNAUTHORIZED_ACESS_ATTEMPT_DOTS + ((UserEntity) authentication.getPrincipal()).getId());
        }
    }

    public boolean userIsSameOrAdmin(Authentication authentication, UUID id) {
        if (authentication == null) {
            throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        }

        return userIsSame(authentication, id) || userIsAdmin(authentication);
    }

    public boolean userIsAdmin(Authentication authentication) {
        return ((UserEntity) authentication.getPrincipal()).getRole().equals(UserRoleEnum.ADMIN);
    }

    public boolean userIsSame(Authentication authentication, UUID id) {
        if (authentication == null) {
            throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        }

        return ((UserEntity) authentication.getPrincipal()).getId().equals(id);
    }

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            password.append(ALPHABET.charAt(index));
        }
        return password.toString();
    }

    public static String formatarCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String toCapitalize(String string) {
        return Arrays.stream(string.split("\\s+"))
            .map(StringUtils::capitalize)
            .collect(Collectors.joining(" "));
    }

}