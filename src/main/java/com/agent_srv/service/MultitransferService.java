package com.agent_srv.service;

import com.agent_srv.model.Multitransfer;
import com.agent_srv.model.Transfer;

import java.util.List;
import java.util.Map;

public interface MultitransferService {
    Multitransfer createTransfer(Multitransfer multitransfer);

    List<Multitransfer> getMultiTransferByidAgent(int idAgent , int page);

    Multitransfer getMultiTransferById(Integer id_multitransfer);

    Multitransfer updateMultitransferByTransfer(Multitransfer multitransfer,Transfer transfer);

    List<Multitransfer> getMultiTransferByidClient(int idClient);

    List<Multitransfer> getMultiTransfers(int page);

    Multitransfer checkIfExpired(Multitransfer multitransfer);

    Multitransfer extort_transfer(String reference, String motif);


    Multitransfer bloque_transfer(String reference, String motif);

    Multitransfer return_transfer(String reference, String motif);

    Multitransfer unlock_transfer(String reference, String motif);

    Multitransfer serve_transfer(String reference);

    Integer countTransferByIdAgent(int idAgent);

    Integer countTransfers();

    List<Multitransfer> getMultiTransferByidAgentAll(int idAgent);

    List<Multitransfer> getMultiTransfersAll();
}
