package com.cmalegrete.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

@Service
public class DocumentService {

    // Método principal que recebe os dados a serem substituídos e o nome do usuário
    public String replaceTextAndConvertToPdf(Map<String, String> replacements, String nomeUsuarioArquivo) {
        try {
            // Carregar o template como InputStream
            InputStream templateStream = getClass().getClassLoader()
                    .getResourceAsStream("docs/contrato_template.docx");

            if (templateStream == null) {
                throw new FileNotFoundException("Template não encontrado em: docs/contrato_template.docx");
            }

            // Gerar nomes personalizados para os arquivos
            String sanitizedNome = nomeUsuarioArquivo.replace(" ", "_").toLowerCase();
            String newDocFileName = "contrato_" + sanitizedNome + ".docx";
            String newPdfFileName = "contrato_" + sanitizedNome + ".pdf";

            // Definir caminhos para salvar os arquivos (usando uma pasta temporária, por exemplo)
            String tempDir = System.getProperty("java.io.tmpdir");
            String newDocFilePath = Paths.get(tempDir, newDocFileName).toString();
            String pdfFilePath = Paths.get(tempDir, newPdfFileName).toString();

            // Manipular o template usando InputStream
            try (XWPFDocument doc = new XWPFDocument(templateStream)) {

                // Substituir texto com base no Map de chaves e valores
                replacements.forEach((originalText, updatedText) -> replaceText(doc, originalText, updatedText));

                // Salvar o documento modificado
                saveFile(newDocFilePath, doc);

                // Converter o documento para PDF
                convertToPdf(newDocFilePath, pdfFilePath);

                // Excluir o arquivo .docx temporário
                deleteFile(newDocFilePath);

                return pdfFilePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Substitui o texto no documento inteiro
    private void replaceText(XWPFDocument doc, String originalText, String updatedText) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceTextInParagraph(paragraph, originalText, updatedText);
        }
    }

    // Realiza a substituição no parágrafo
    private void replaceTextInParagraph(XWPFParagraph paragraph, String originalText, String updatedText) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null) {
            StringBuilder paragraphText = new StringBuilder();

            for (XWPFRun run : runs) {
                paragraphText.append(run.getText(0));
            }

            String updatedParagraphText = paragraphText.toString().replace(originalText, updatedText);

            if (!updatedParagraphText.equals(paragraphText.toString())) {
                for (int i = runs.size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }
                paragraph.createRun().setText(updatedParagraphText);
            }
        }
    }

    // Salva o documento modificado
    private void saveFile(String filePath, XWPFDocument document) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Converte o arquivo .docx para PDF usando docx4j
    private void convertToPdf(String docxPath, String pdfPath) {
        try {
            InputStream inputDocStream = new FileInputStream(docxPath);
            XWPFDocument doc = new XWPFDocument(inputDocStream);
            PdfOptions pdfOptions = PdfOptions.create();
            OutputStream outputPdfStream = new FileOutputStream(new File(pdfPath));
            PdfConverter.getInstance().convert(doc, outputPdfStream, pdfOptions);
            doc.close();
            inputDocStream.close();
            outputPdfStream.close();
            System.out.println("PDF gerado com sucesso: " + pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Excluir o arquivo temporário .docx
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Arquivo excluído: " + filePath);
            } else {
                System.out.println("Falha ao excluir o arquivo: " + filePath);
            }
        }
    }
}
