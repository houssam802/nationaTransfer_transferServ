package com.agent_srv.repository;

import com.agent_srv.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepo extends JpaRepository<Transfer,Integer> {


    @Query(value = "SELECT * FROM transfer WHERE transfer_reference = ?1 Limit 1",
            nativeQuery = true)
    public Transfer getTransferByreference(String transfer_reference);

    @Query(value = "SELECT * FROM transfer WHERE transfer_reference = ?1 Limit 1",
            nativeQuery = true)
    Transfer getTransferByWalletreference(String reference);

    @Query(value = "SELECT * FROM transfer WHERE transfer_status = ?1 Limit 5",
            nativeQuery = true)
    public List<Transfer> getTransferByStatus(Integer status);

    @Query(value = "SELECT * FROM transfer WHERE transfer_reference = ?1 and receiver_fname = ?2 " +
            "and receiver_lname = ?3 and receiver_phnumber = ?4 ",
            nativeQuery = true)
    Transfer checkRecipientInfosByReference(String reference, String receiver_fname, String receiver_lname, String receiver_phnumber);

}
