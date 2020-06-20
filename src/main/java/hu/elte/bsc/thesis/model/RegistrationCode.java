/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

import hu.elte.bsc.thesis.model.User.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */
@Entity
@Table(name = "REGISTRATIONCODES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationCode {
    @Column(nullable = false, unique = true)
    @Id
    private String code;

    @Version
    private int version;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = false)
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role createdAccountType;
}
