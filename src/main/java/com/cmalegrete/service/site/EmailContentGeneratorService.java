package com.cmalegrete.service.site;

import org.springframework.stereotype.Service;

import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.service.util.UtilService;


@Service
public class EmailContentGeneratorService extends UtilService {
    public static String generateMemberConfirmationMessage(MemberEntity member, String token) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(toCapitalize(member.getName())).append(",</p>")
                .append("<p>É com grande satisfação que confirmamos o recebimento de sua aplicação. A seguir, estão os dados fornecidos durante o processo de inscrição:</p>")
                .append("<p><strong>Nome Completo:</strong> ").append(toCapitalize(member.getName()))
                .append("<br>")
                .append("<strong>CPF:</strong> ").append(member.getCpf()).append("<br>")
                .append("<strong>E-mail:</strong> ").append(member.getEmail()).append("<br>")
                .append("<strong>Telefone:</strong> ").append(member.getPhoneNumber()).append("<br>");

        if (member.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(member.getMilitaryOrganization())
                    .append("</p>");
        } else {
            msg.append("<strong>Ex-militar/não é militar</strong> ")
                    .append("</p>");
        }

        msg.append(
                "<p>Em anexo, você encontrará o <strong>contrato não assinado</strong>, que deverá ser devidamente assinado digitalmente para concluir sua aplicação.</p>")
                .append("<p>Para assinar o contrato, siga os passos abaixo:</p>")
                .append("<ol>")
                .append("<li>Assine o documento digitalmente utilizando a plataforma <a href=\"https://assinador.iti.br/assinatura/\" target=\"_blank\">Assinatura Digital do GOV.BR</a>.</li>")
                .append("<li>Acesse o link abaixo para fazer o envio do contrato assinado:</li>")
                .append("</ol>")
                .append(generateContractLink(token))
                .append("<p>Seu contrato será analisado pela nossa equipe, e você receberá um retorno em até <strong>10 dias úteis</strong>.</p>")
                .append("<p>Caso tenha qualquer dúvida ou precise atualizar alguma das informações fornecidas, por favor, entre em contato conosco através do nosso <a href=\"https://cmalegrete.com.br\">site</a>.</p>")
                .append("<p>Agradecemos pela confiança e estamos à disposição para qualquer esclarecimento.</p>")
                .append("<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>")
                .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor, não responda a esta mensagem.</p>");

        return msg.toString();
    }

    public static String generateContractMessageForTeam(MemberEntity member) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezada Equipe do Círculo Militar de Alegrete,</p>")
                .append("<p>Informamos que um novo contrato foi submetido para análise. Abaixo estão os dados do requerente:</p>")
                .append("<p><strong>Nome Completo:</strong> ").append(toCapitalize(member.getName()))
                .append("<br>")
                .append("<strong>CPF:</strong> ").append(member.getCpf()).append("<br>")
                .append("<strong>E-mail:</strong> ").append(member.getEmail()).append("<br>")
                .append("<strong>Telefone:</strong> ").append(member.getPhoneNumber()).append("<br>");

        if (member.getMilitaryOrganization() != null) {
            msg.append("<strong>Organização Militar:</strong> ").append(member.getMilitaryOrganization())
                    .append("</p>");
        } else {
            msg.append("<strong>Ex-militar/não é militar</strong> ")
                    .append("</p>");
        }

        msg.append("<p>Por favor, prossigam com a análise do contrato o mais breve possível.</p>")
                .append("<p>Atenciosamente,<br>Sistema de Gestão do Círculo Militar de Alegrete</p>");

        return msg.toString();
    }

    public static String generateContractReceivedConfirmation(MemberEntity member) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(toCapitalize(member.getName())).append(",</p>")
                .append("<p>Confirmamos o recebimento de seu contrato assinado. Nossa equipe está analisando o documento e entraremos em contato em breve.</p>")
                .append("<p>O prazo estimado para retorno é de até <strong>10 dias úteis</strong>. Caso tenha alguma dúvida ou precise de mais informações, sinta-se à vontade para entrar em contato conosco pelo telefone ou e-mail fornecido.</p>")
                .append("<p>Agradecemos pela confiança e permanecemos à disposição.</p>")
                .append("<p>Atenciosamente,<br>Equipe do Círculo Militar de Alegrete</p>")
                .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor, não responda a esta mensagem.</p>");

        return msg.toString();
    }

    private static String generateContractLink(String token) {
        return "<a href=http://cmalegrete.com.br/enviar-contrato?token=" + token
                + "><strong>[Clique aqui para enviar o contrato assinado]</strong></a>";
    }
}
