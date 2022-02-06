package com.agent_srv.dto;

import com.agent_srv.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultitransferDTO {

    private LocalDateTime created_at = LocalDateTime .now();
    private LocalDateTime  ended_at = created_at.plusDays(3);

    @NotNull
    private Integer id_client=0;

    @NotNull
    private Float total_amount;

    @NotNull
    private Float total_expense_amount;

    @NotNull
    private int expense_id;

    @NotNull
    private boolean transfer_by_cash;

    @NotNull
    private boolean notify_transfer;

    @NotNull
    private String sender_fname;

    @NotNull
    private String sender_lname;

    @NotNull
    private String sender_phnumber;

    @NotNull
    private boolean request_by_prospect_client;

    @NotNull
    private int sended_by_agent;

    @NotNull
    private String motif;

    private List<TransferDTO> transfers;
}
