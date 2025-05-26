package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public class ApprovedProposalDto {

        private Long proposalId;

        private Long userId;

        private String cpf;

        private String userEmail;

        private String userName;

        private Double userIncome;

        private Double requestedAmount;

        private Integer installments;

        private LocalDateTime createdAt;

        private String purpose;

        private double installmentsValue;

        public String getFormattedCreatedAt() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
            return this.createdAt.format(formatter);
        }

}
