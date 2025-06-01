package system.dev.marques.integration.listener;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import system.dev.marques.CreditAnalysisApplication;
import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.integration.AbstractIntegration;
import system.dev.marques.integration.config.MockConfig;
import system.dev.marques.service.CreditAnalysisService;
import system.dev.marques.service.ProducerService;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static system.dev.marques.util.QueueDtoCreator.createAnalyzedDto;
import static system.dev.marques.util.QueueDtoCreator.createProposalCreditDto;

@SpringBootTest(classes = {CreditAnalysisApplication.class, MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=7777")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CreditListenerIT extends AbstractIntegration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CreditAnalysisService creditService;

    @Autowired
    private ProducerService producerService;

    @Test
    void shouldListenAndGetCreditResultThenSendToQueue() {

        ProposalCreditDto dto = createProposalCreditDto();

        AnalyzedDto analyzedDto = createAnalyzedDto();

        BDDMockito.when(creditService.analyzeCredit(dto)).thenReturn(analyzedDto);

        rabbitTemplate.convertAndSend("", "queue.credit.analysis", dto);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(creditService, times(1)).analyzeCredit(dto));

        ArgumentCaptor<AnalyzedDto> captor = ArgumentCaptor.forClass(AnalyzedDto.class);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(producerService).sendAnalyzedProposal(captor.capture()));

        AnalyzedDto captured = captor.getValue();

        assertThat(captured.getProposalId()).isEqualTo(dto.getProposalId());
        assertThat(captured.getCpf()).isEqualTo(dto.getCpf());
    }
}
