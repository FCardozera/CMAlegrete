package com.cmalegrete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    // Método para envio de e-mail com mensagem HTML
    @Async
    public void enviarEmailTexto(String destinatario, String assunto, String htmlMsg) {
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

    // Novo método para envio de e-mail com anexo
    @Async
    public void enviarEmailComAnexo(String destinatario, String assunto, String htmlMsg, String caminhoAnexo, String nomeArquivoAnexo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            htmlMsg += "<img src='cid:logoImage' alt='Logo do Clube Militar de Alegrete' style='height:270px;' />";
            helper.setText(htmlMsg, true);

            // Adiciona logo no corpo do e-mail
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            // Adiciona o anexo
            FileSystemResource arquivoAnexo = new FileSystemResource(new File(caminhoAnexo));
            helper.addAttachment(nomeArquivoAnexo, arquivoAnexo);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
