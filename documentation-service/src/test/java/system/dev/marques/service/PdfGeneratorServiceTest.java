package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.exception.PdfConverterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.QueueDtoCreator.createApprovedProposalDto;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceTest {

    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    @Mock
    private TemplateEngine templateEngineMock;

    @Test
    void generate_ReturnsPDF_WhenSuccessful() {
        when(templateEngineMock.process(anyString(), any(Context.class))).thenReturn("<h1>Hello</h1>");

        ApprovedProposalDto dto = createApprovedProposalDto();
        byte[] pdf = pdfGeneratorService.generate(dto);

        assertThat(pdf).isNotNull().isNotEmpty();
    }

    @Test
    void generate_ThrowsException_WhenFailedToGeneratePDF() {
        when(templateEngineMock.process(anyString(), any(Context.class))).thenReturn("");

        ApprovedProposalDto dto = createApprovedProposalDto();

        assertThatExceptionOfType(PdfConverterException.class)
                .isThrownBy(() -> pdfGeneratorService.generate(dto))
                .withMessageContaining("Error while trying to generate PDF: ");
    }


}