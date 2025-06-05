package system.dev.marques.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.dto.UserReceiptDto;
import system.dev.marques.exception.NoPdfFoundException;
import system.dev.marques.mapper.ProposalMapper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.*;
import static system.dev.marques.util.QueueDtoCreator.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private PdfGeneratorService pdfGeneratorMock;

    @Mock
    private S3Client s3ClientMock;

    @Mock
    private S3Presigner s3PresignerMock;

    @Mock
    private ProducerService producerServiceMock;

    @Mock
    private ProposalMapper mapperMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucketName", "bucket-name");
    }

    @Test
    void upload_UploadsPDF_WhenSuccessful() throws MalformedURLException {
        when(pdfGeneratorMock.generate(any(ApprovedProposalDto.class))).thenReturn(new byte[0]);

        PresignedGetObjectRequest presignedRequestMock = mock(PresignedGetObjectRequest.class);

        URL fakeUrl = URI.create("https://fake-s3-url.com/fakefile.pdf").toURL();

        when(presignedRequestMock.url()).thenReturn(fakeUrl);
        when(s3PresignerMock.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedRequestMock);

        when(mapperMock.toProposalNotificationDto(any(ApprovedProposalDto.class)))
                .thenReturn(createProposalNotificationDto());

        doNothing().when(producerServiceMock).sendNotification(any(ProposalNotificationDto.class));

        ApprovedProposalDto dto = createApprovedProposalDto();

        assertThatCode(() -> s3Service.upload(dto)).doesNotThrowAnyException();

        verify(s3ClientMock, times(1))
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));

        verify(s3PresignerMock, times(1))
                .presignGetObject(any(GetObjectPresignRequest.class));

    }


    @Test
    void getProposalUrl_SendsURL_WhenSuccessful() throws MalformedURLException {
        PresignedGetObjectRequest presignedRequestMock = mock(PresignedGetObjectRequest.class);
        URL fakeUrl = URI.create("https://fake-s3-url.com/fakefile.pdf").toURL();

        when(presignedRequestMock.url()).thenReturn(fakeUrl);
        when(s3PresignerMock.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedRequestMock);

        when(mapperMock.toProposalNotificationDto(any(UserReceiptDto.class)))
                .thenReturn(createProposalNotificationDto());

        doNothing().when(producerServiceMock).sendNotification(any(ProposalNotificationDto.class));

        UserReceiptDto dto = createUserReceiptDto();

        assertThatCode(() -> s3Service.getProposalUrl(dto)).doesNotThrowAnyException();
    }

    @Test
    void getProposalUrl_ThrowsNoPdfFoundException_WhenNoPDFIsFound() {
        ReflectionTestUtils.setField(s3Service, "bucketName", "");

        willThrow(S3Exception.class).given(s3ClientMock).headObject(any(HeadObjectRequest.class));

        UserReceiptDto dto = createUserReceiptDto();

        assertThatExceptionOfType(NoPdfFoundException.class)
                .isThrownBy(() -> s3Service.getProposalUrl(dto))
                .withMessageContaining("Receipt not found for the given proposal.");

    }

    @Test
    void deleteUserFolder_DeletesFolder_WhenSuccessful() {
        S3Object s3Object = S3Object.builder()
                .key("user-folder/file1.pdf")
                .build();

        ListObjectsV2Response mockResponse = ListObjectsV2Response.builder()
                .contents(List.of(s3Object))
                .build();

        when(s3ClientMock.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(mockResponse);

        assertThatCode(() -> s3Service.deleteUserFolder(1L)).doesNotThrowAnyException();
    }

    @Test
    void deleteUserFolder_DoNothing_WhenNoFolderIsFound() {
        ListObjectsV2Response mockResponse = ListObjectsV2Response.builder()
                .contents(Collections.emptyList())
                .build();

        when(s3ClientMock.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(mockResponse);

        assertThatCode(() -> s3Service.deleteUserFolder(1L)).doesNotThrowAnyException();
    }


}