package com.agent_srv.model;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Multitransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_multitransfer;

    private LocalDateTime created_at = LocalDateTime .now();
    private LocalDateTime  ended_at = created_at.plusDays(4);

    @NotNull
    private Integer id_client;

    @NotNull
    private Float total_amount;

    @NotNull
    private String motif;

    @NotNull
    private Float total_expense_amount;

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
    private boolean transfer_by_cash;

    @NotNull
    private int expense_id;

    @NotNull
    private boolean notify_transfer;

    @OneToMany(targetEntity = Transfer.class ,cascade = CascadeType.ALL)
    @JoinColumn(name = "id_Multitransfer" , referencedColumnName = "id_multitransfer")
    private List<Transfer> transfers;

}
