package com.neobis.rentit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neobis.rentit.model.enums.ApplicationStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer passNum;

    private String INN;

    private LocalDate issuedDatePass;

    private LocalDate expDatePass;

    private String issuedAuthorityPass;

    @Enumerated(value = EnumType.STRING)
    private ApplicationStatus status;

    @OneToOne
    @JoinColumn(name = "address_id")
    private UserAddress address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "fullRegistration")
    private List<UserImages> imagesList;


    public FullRegistration(Integer passNum, String INN, LocalDate issuedDatePass,
                            LocalDate expDatePass, String issuedAuthorityPass,
                            ApplicationStatus status, UserAddress address,
                            User user) {
        this.passNum = passNum;
        this.INN = INN;
        this.issuedDatePass = issuedDatePass;
        this.expDatePass = expDatePass;
        this.issuedAuthorityPass = issuedAuthorityPass;
        this.status = status;
        this.address = address;
        this.user = user;
    }
}