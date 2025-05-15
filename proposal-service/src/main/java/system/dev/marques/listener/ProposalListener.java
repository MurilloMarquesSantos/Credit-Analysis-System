package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.AnalyzedDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.mapper.ProposalMapper;
import system.dev.marques.service.ProducerService;
import system.dev.marques.service.ProposalService;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProposalListener {

    private final ProposalService proposalService;

    private final ProducerService producerService;

    private final ProposalMapper mapper;

    @RabbitListener(queues = "queue.proposal")
    public void listenProposalQueue(ProposalDto dto) {
        Proposal savedProposal = proposalService.save(dto);

        if (savedProposal != null) {
            ProposalCreditDto creditDto = mapper.toProposalCreditDto(savedProposal);
            producerService.sendProposalToCredit(creditDto);
        }
    }

    @RabbitListener(queues = "queue.analyzed.credit")
    public void listerAnalyzedProposalQueue(AnalyzedDto dto) {
        log.info("analyzed proposal: {}", dto);
        proposalService.updateProposalStatus(dto);
    }

}
