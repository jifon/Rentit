package com.neobis.rentit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone_number"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String lastName;

    @Size(max = 20)
    private String middleName;

    private String googleId;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column( length = 64)
    private String verificationCode;

    private boolean enabled;

    private Date dob;
    @NotBlank
    @Size(max = 120)
    private String password;

    private String usersImageUrl;

    private Boolean premiumSubscription;

    private LocalDate premiumEndTime;

    private String premiumBannerUrl;

    private Long premiumProductId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "date_created" /*, nullable = false **/)
    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    private LocalDateTime dateDeleted;

    private Double balance = (double) 0;

    private Double rating;




    public String getUsername(){
        return email!= null ? getEmail() : getPhoneNumber();
    }


    public User(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.enabled =false;
        this.dateCreated = LocalDateTime.now();
        this.rating = 0.0;
    }


}

