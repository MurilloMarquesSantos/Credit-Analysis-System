package system.dev.marques.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.UserReceiptDto;
import system.dev.marques.service.S3Service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;
import static system.dev.marques.util.QueueDtoCreator.createApprovedProposalDto;
import static system.dev.marques.util.QueueDtoCreator.createUserReceiptDto;

@ExtendWith(MockitoExtension.class)
class ProposalApprovedListenerTest {

    @InjectMocks
    private ProposalApprovedListener listener;

    @Mock
    private S3Service s3ServiceMock;

    @Test
    void listenDocumentQueue_UploadsPdf_WhenSuccessful() {
        doNothing().when(s3ServiceMock).upload(any(ApprovedProposalDto.class));
        assertThatCode(() -> listener.listenDocumentQueue(createApprovedProposalDto())).doesNotThrowAnyException();
    }

    @Test
    void listenDocumentUserQueue_GetsProposalUrl_WhenSuccessful() {
        doNothing().when(s3ServiceMock).getProposalUrl(any(UserReceiptDto.class));
        assertThatCode(() -> listener.listenDocumentUserQueue(createUserReceiptDto())).doesNotThrowAnyException();
    }

    @Test
    void listenDocumentUserQueueDelete_DeletesUserFolder_WhenSuccessful() {
        doNothing().when(s3ServiceMock).deleteUserFolder(anyLong());
        assertThatCode(() -> listener.listenDocumentUserQueueDelete(1L)).doesNotThrowAnyException();
    }

}