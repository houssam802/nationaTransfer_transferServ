package com.agent_srv.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_transfer;

    private String transfer_reference ;
    private String code_pin;

    @NotNull
    private float transfer_amount;
    @NotNull
    private int transfer_status;

    private LocalDateTime received_at;

    @NotNull
    private String receiver_fname;
    @NotNull
    private String receiver_lname;
    @NotNull
    private String receiver_phnumber;

    private String motif="";
    private Integer id_multitransfer;
}
