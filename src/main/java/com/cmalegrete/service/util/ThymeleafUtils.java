package com.cmalegrete.service.util;

public class ThymeleafUtils {

    public String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return String.format("(%s) %s-%s",
                phone.substring(0, 2),
                phone.substring(2, 7),
                phone.substring(7));
    }

    public String formatRole(String role) {
        if (role.equals("ADMIN")) {
            return "Administrador";
        } else if (role.equals("ASSISTANT")) {
            return "Assistente";
        } else {
            return "Sócio";
        }
    }
}
