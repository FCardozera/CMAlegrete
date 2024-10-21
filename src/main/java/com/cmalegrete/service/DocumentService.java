package com.cmalegrete.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.stereotype.Service;

import com.cmalegrete.service.util.UtilService;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import com.spire.presentation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DocumentService extends UtilService{

    // Método principal que recebe os dados a serem substituídos e o nome do usuário
    public byte[] replaceTextDOCXAndConvertToPdf(Map<String, String> replacements) {
        try {
            // Carrega o template como InputStream
            InputStream templateStream = getClass().getClassLoader()
                    .getResourceAsStream("docs/contrato_template.docx");

            if (templateStream == null) {
                throw new FileNotFoundException("Template não encontrado em: docs/contrato_template.docx");
            }

            // Carrega o documento .docx do template
            try (XWPFDocument doc = new XWPFDocument(templateStream)) {

                // Substitui as strings com base no Map de chaves e valores
                replacements.forEach((originalText, updatedText) -> replaceText(doc, originalText, updatedText));

                // Salva o documento modificado na memória
                ByteArrayOutputStream docOutputStream = new ByteArrayOutputStream();
                doc.write(docOutputStream);

                // Converte para PDF e retorna os bytes
                return convertDOCXToPdf(docOutputStream.toByteArray());

            } catch (Exception e) {
                e.printStackTrace();
                return new byte[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[1];
        }
    }

    public byte[] replaceTextPPTXAndConvertToPdf(Map<String, String> replacements) {
        try {
            // Carrega o template PPTX como InputStream
            InputStream templateStream = getClass().getClassLoader()
                    .getResourceAsStream("docs/modelo_carteirinha.pptx");

            if (templateStream == null) {
                throw new FileNotFoundException("Template não encontrado em: docs/modelo_carteirinha.pptx");
            }

            // Carrega o documento PPTX
            try (XMLSlideShow ppt = new XMLSlideShow(templateStream)) {

                // Substitui as strings com base no Map de chaves e valores
                replacements.forEach((originalText, updatedText) -> replaceTextInPPTX(ppt, originalText, updatedText));

                // Salva o documento modificado na memória
                ByteArrayOutputStream pptOutputStream = new ByteArrayOutputStream();
                ppt.write(pptOutputStream);

                return convertPPTXtoPDF(pptOutputStream.toByteArray());

            } catch (Exception e) {
                e.printStackTrace();
                return new byte[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[1];
        }
    }

    // Substitui o texto em todos os slides do PPTX
    private void replaceTextInPPTX(XMLSlideShow ppt, String originalText, String updatedText) {
        ppt.getSlides().forEach(slide -> {
            for (XSLFShape shape : slide.getShapes()) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    String text = textShape.getText();
                    if (text != null && text.contains(originalText)) {
                        textShape.setText(text.replace(originalText, updatedText));
                    }
                }
            }
        });
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

    // Converte o arquivo .docx para PDF usando docx4j
    private byte[] convertDOCXToPdf(byte[] docxBytes) {
        try {
            ByteArrayInputStream inputDocStream = new ByteArrayInputStream(docxBytes);
            XWPFDocument doc = new XWPFDocument(inputDocStream);
            PdfOptions pdfOptions = PdfOptions.create();
            ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(doc, outputPdfStream, pdfOptions);
            doc.close();
            inputDocStream.close();
            outputPdfStream.close();
            return outputPdfStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[1];
    }

    // Converte o arquivo pptx usando spire
    private byte[] convertPPTXtoPDF(byte[] pptxBytes) {
        try {
            ByteArrayInputStream inputPptxStream = new ByteArrayInputStream(pptxBytes);
            Presentation pptx = new Presentation();
            pptx.loadFromStream(inputPptxStream, FileFormat.PPTX_2019);
            ByteArrayOutputStream outputPdfStream = new ByteArrayOutputStream();
            pptx.saveToFile(outputPdfStream, FileFormat.PDF);
            inputPptxStream.close();
            outputPdfStream.close();
            return outputPdfStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[1];
    }
}
