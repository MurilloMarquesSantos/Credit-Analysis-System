package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.dto.UserReceiptDto;
import system.dev.marques.exception.NoPdfFoundException;
import system.dev.marques.mapper.ProposalMapper;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final PdfGeneratorService pdfGenerator;

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private final ProducerService producerService;

    private final ProposalMapper mapper;

    private static final String FOLDER_PREFIX = "receipts/user-";

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public void upload(ApprovedProposalDto dto) {

        byte[] pdf = pdfGenerator.generate(dto);

        String key = FOLDER_PREFIX + dto.getUserId() + "/proposal-" + dto.getProposalId();

        String url = uploadAndGetUrl(pdf, key);

        ProposalNotificationDto proposalNotificationDto = mapper.toProposalNotificationDto(dto);

        proposalNotificationDto.setUrl(url);

        producerService.sendNotification(proposalNotificationDto);
    }


    private String uploadAndGetUrl(byte[] pdf, String key) {
        PutObjectRequest putRequest = PutObjectRequest.
                builder()
                .bucket(bucketName)
                .contentType("application/pdf")
                .key(key)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(pdf));


        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();

        URL url = s3Presigner.presignGetObject(presignRequest).url();
        return url.toString();
    }

    public void getProposalUrl(UserReceiptDto dto) {
        String key = FOLDER_PREFIX + dto.getUserId() + "/proposal-" + dto.getProposalId();

        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.headObject(request);
        } catch (S3Exception e) {
            throw new NoPdfFoundException("Receipt not found for the given proposal. " + e.getMessage());
        }

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();

        String url = s3Presigner.presignGetObject(presignRequest).url().toString();

        ProposalNotificationDto proposalNotificationDto = mapper.toProposalNotificationDto(dto);

        proposalNotificationDto.setUrl(url);

        producerService.sendUserReceipt(proposalNotificationDto);
    }

    public void deleteUserFolder(Long userId) {
        String prefix = FOLDER_PREFIX + userId + "/";

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .build()
        );

        if (!listResponse.contents().isEmpty()) {
            s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(d -> d.objects(listResponse.contents().stream()
                            .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                            .toList()))
                    .build());
        }
    }

}
