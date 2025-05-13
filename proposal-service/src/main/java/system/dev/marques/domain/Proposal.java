package system.dev.marques.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import system.dev.marques.domain.enums.ProposalStatus;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String cpf;

    private String userEmail;

    private String userName;

    private Double userIncome;

    private Double requestedAmount;

    private Integer installments;

    private LocalDateTime createdAt;

    private String purpose;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
