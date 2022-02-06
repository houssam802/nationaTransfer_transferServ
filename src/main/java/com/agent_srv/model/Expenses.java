package com.agent_srv.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int expense_id;
    private String expense_type;
}
