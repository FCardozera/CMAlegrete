package com.cmalegrete.service.site;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cmalegrete.model.log.LogEnum;
import com.cmalegrete.model.member.MemberEntity;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.service.util.UtilService;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class EmailSendService extends UtilService {

    private final JavaMailSender javaMailSender;
    private final ScheduledExecutorService emailExecutor = Executors.newScheduledThreadPool(5);

    // Limita o número de tentativas
    private static final int MAX_RETRIES = 5;
    private static final long DELAY_BETWEEN_EMAILS = 4000L; // 4 segundos de intervalo

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.reset-password-url}")
    private String resetPasswordUrl;

    // Método de envio com controle de tentativas
    private void enviarComRetry(Runnable emailTask, AtomicInteger attempts) {
        emailExecutor.schedule(() -> {
            try {
                emailTask.run();
            } catch (Exception e) {
                if (attempts.incrementAndGet() < MAX_RETRIES) {
                    enviarComRetry(emailTask, attempts);
                } else {
                    e.printStackTrace();
                }
            }
        }, DELAY_BETWEEN_EMAILS, TimeUnit.MILLISECONDS);
    }

    @Async
    public void sendPasswordResetConfirmationEmailToUser(UserEntity user) {
        String htmlMessage = generatePasswordResetMessage(user);

        enviarEmailTexto(user.getEmail(), "Redefinição de Senha", htmlMessage);
    }

    @Async
    public void sendConfirmationEmailToUser(MemberEntity member, byte[] contratoBytes, String token) {
        Runnable emailTask = () -> {
            String htmlMemberMsg = EmailContentGeneratorService.generateMemberConfirmationMessage(member, token);
            try {
                enviarEmailComAnexo(
                        member.getEmail(),
                        "Confirmação de Envio de Aplicação - Círculo Militar de Alegrete",
                        htmlMemberMsg,
                        contratoBytes,
                        "contrato_" + member.getName().toLowerCase() + ".pdf");
            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        // Executa a tarefa de envio com tentativas e delay entre os envios
        enviarComRetry(emailTask, new AtomicInteger(0));
    }

    @Async
    public void sendMessageToTeam(String destinatario, String assunto, String htmlMsg) {
        Runnable emailTask = () -> enviarEmailTexto(destinatario, assunto, htmlMsg);

        enviarComRetry(emailTask, new AtomicInteger(0));
    }

    @Async
    public void sendContractToTeam(MemberEntity member, byte[] file, String fileName) {
        Runnable emailTask = () -> {
            String htmlContractMsg = EmailContentGeneratorService.generateContractMessageForTeam(member);
            try {
                enviarEmailComAnexo("circulomilitardealegrete1@gmail.com", "Recebimento de Contrato - " + member.getName(),
                        htmlContractMsg, file, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        enviarComRetry(emailTask, new AtomicInteger(0));
    }

    @Async
    public void sendReceivedContractConfirmationToMember(MemberEntity member) {
        Runnable emailTask = () -> {
            String htmlContractMsg = EmailContentGeneratorService.generateContractReceivedConfirmation(member);
            try {
                enviarEmailTexto(member.getEmail(),
                        "Confirmação de Recebimento de Contrato - Círculo Militar de Alegrete",
                        htmlContractMsg);
                logEmailSendUser(member.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        enviarComRetry(emailTask, new AtomicInteger(0));
    }

    private String generatePasswordResetMessage(UserEntity user) {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Prezado(a) ").append(user.getName()).append(",</p>")
           .append("<p>Abaixo segue o link para a redefinição de sua senha:</p>")
           .append(generateResetPasswordLink(user.getPasswordResetToken()))
           .append("<p>Por favor, entre em contato conosco caso não tenha sido você que solicitou a redefinição.</p>")
           .append("<p>Atenciosamente,<br>Equipe</p>")
           .append("<br><p style='font-size:8;'>Este é um e-mail automático, por favor não responder.</p>");

        return msg.toString();
    }

    private String generateResetPasswordLink(String token) {
        return "<a href=\"" + resetPasswordUrl + "/" + token + "\">Clique aqui para redefinir sua senha</a>";
    }

    private void enviarEmailTexto(String destinatario, String assunto, String htmlMsg) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            htmlMsg += "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";
            helper.setText(htmlMsg, true);

            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarEmailComAnexo(String destinatario, String assunto, String htmlMsg, byte[] arquivoAnexoBytes,
            String nomeArquivoAnexo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            htmlMsg += "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";
            helper.setText(htmlMsg, true);

            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);
            helper.addAttachment(nomeArquivoAnexo, new ByteArrayDataSource(arquivoAnexoBytes, "application/pdf"));

            javaMailSender.send(mimeMessage);
            logEmailSendUser(destinatario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logEmailSendUser(String email) {
        log(LogEnum.INFO, "Email sent to: " + email, HttpStatus.OK.value());
    }
}
