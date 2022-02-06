package com.agent_srv.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    @NotNull
    private float transfer_amount;
    private String transfer_reference ;
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
    @NotNull
    private String id_multitransfer;
}
