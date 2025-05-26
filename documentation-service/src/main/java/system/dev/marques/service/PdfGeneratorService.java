package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import system.dev.marques.dto.ApprovedProposalDto;

import java.io.ByteArrayOutputStream;


//todo make a dedicate exception
@Component
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public byte[] generate(ApprovedProposalDto dto) {
        double installmentsValue = dto.getRequestedAmount() / dto.getInstallments();
        dto.setInstallmentsValue(installmentsValue);
        Context context = new Context();
        context.setVariable("dto", dto);
        String html = templateEngine.process("documentation", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
