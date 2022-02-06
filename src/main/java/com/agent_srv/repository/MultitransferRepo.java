package com.agent_srv.repository;

import com.agent_srv.model.Multitransfer;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface MultitransferRepo extends JpaRepository<Multitransfer,Integer> {

    @Query(value = "SELECT * FROM multitransfer WHERE sended_by_agent = ?1 ORDER BY id_multitransfer DESC",
            nativeQuery = true)
    List<Multitransfer> findByIdAgent(int agent, Pageable pageable);

    @Query(value = "SELECT * FROM multitransfer WHERE id_client=?1 ",
            nativeQuery = true)
    List<Multitransfer> findByIdClient(int idClient);

    @Query(value = "SELECT count(*) FROM multitransfer WHERE sended_by_agent = ?1 ",
            nativeQuery = true)
    Integer countByidAgent(int idAgent);

    @Query(value = "SELECT * FROM multitransfer ORDER BY id_multitransfer DESC ",
            nativeQuery = true)
    List<Multitransfer> getTransfers(Pageable pageable);

    @Query(value = "SELECT count(*) FROM multitransfer",
            nativeQuery = true)
    Integer countMultitransfers();

    @Query(value = "SELECT * FROM multitransfer ORDER BY id_multitransfer DESC",
            nativeQuery = true)
    List<Multitransfer> getTransfersAll();

    @Query(value = "SELECT * FROM multitransfer WHERE sended_by_agent = ?1 ORDER BY id_multitransfer DESC",
            nativeQuery = true)
    List<Multitransfer> getTransfersByAgentAll(int idAgent);
}
