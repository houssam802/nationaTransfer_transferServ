package com.agent_srv.service.impl;

import com.agent_srv.exception.TransfersException;
import com.agent_srv.model.Multitransfer;
import com.agent_srv.model.Transfer;
import com.agent_srv.repository.MultitransferRepo;
import com.agent_srv.service.MultitransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class MultitransferServiceImpl implements MultitransferService {

    @Autowired
    private TransferServiceImpl transferService;

    @Autowired
    private MultitransferRepo multitransferRepo;

    @Override
    public Multitransfer createTransfer(Multitransfer multitransfer) {
        return multitransferRepo.save(multitransfer);
    }

    @Override
    public List<Multitransfer> getMultiTransferByidAgent(int idAgent , int page) {
        Pageable pageable = PageRequest.of(page-1 , 5);
        List<Multitransfer> multitransfers = multitransferRepo.findByIdAgent(idAgent,pageable);
        List<Multitransfer> multitransfers_tmp = new ArrayList<>();
        multitransfers.forEach(multitransfer -> {
            multitransfers_tmp.add(checkIfExpired(multitransfer));
        });
        return multitransfers_tmp ;
    }

    @Override
    public Integer countTransferByIdAgent(int idAgent) {
        return multitransferRepo.countByidAgent(idAgent);
    }

    @Override
    public Integer countTransfers() {
        return multitransferRepo.countMultitransfers();
    }


    @Override
    public Multitransfer getMultiTransferById(Integer id_multitransfer) {
        Multitransfer multitransfer = multitransferRepo.findById(id_multitransfer).get();
        return checkIfExpired(multitransfer);
    }

    @Override
    public Multitransfer updateMultitransferByTransfer(Multitransfer multitransfer,Transfer transfer) {
       /* List<Transfer> transfers = new ArrayList<>();
        multitransfer.getTransfers().forEach(tranf -> {
            if(tranf.getId_transfer() != transfer.getId_transfer()){
                tranf.setId_multitransfer(transfer.getId_multitransfer());
                transfers.add(tranf);
            }
        });
        transfers.add(transfer);
        multitransfer.setTransfers(transfers);*/
        multitransferRepo.save(multitransfer);
        return multitransfer;
    }

    @Override
    public List<Multitransfer> getMultiTransferByidClient(int idClient) {
        List<Multitransfer> multitransfers = multitransferRepo.findByIdClient(idClient);
        List<Multitransfer> multitransfers_tmp = new ArrayList<>();
        multitransfers.forEach(multitransfer -> {
            multitransfers_tmp.add(checkIfExpired(multitransfer));
        });
        return multitransfers_tmp;
    }

    @Override
    public List<Multitransfer> getMultiTransfers(int page) {
        Pageable pageable = PageRequest.of(page-1 , 5);
        List<Multitransfer> multitransfers = multitransferRepo.getTransfers(pageable);
        List<Multitransfer> multitransfers_tmp = new ArrayList<>();
        multitransfers.forEach(multitransfer -> {
            multitransfers_tmp.add(checkIfExpired(multitransfer));
        });
        return multitransfers_tmp;
    }

    @Override
    public List<Multitransfer> getMultiTransferByidAgentAll(int idAgent) {
        List<Multitransfer> multitransfers = multitransferRepo.getTransfersByAgentAll(idAgent);
        List<Multitransfer> multitransfers_tmp = new ArrayList<>();
        multitransfers.forEach(multitransfer -> {
            multitransfers_tmp.add(checkIfExpired(multitransfer));
        });
        return multitransfers_tmp;
    }

    @Override
    public List<Multitransfer> getMultiTransfersAll() {
        List<Multitransfer> multitransfers = multitransferRepo.getTransfersAll();
        List<Multitransfer> multitransfers_tmp = new ArrayList<>();
        multitransfers.forEach(multitransfer -> {
            multitransfers_tmp.add(checkIfExpired(multitransfer));
        });
        return multitransfers_tmp;    }

    @Override
    public Multitransfer checkIfExpired(Multitransfer multitransfer) {
        LocalDateTime day = LocalDateTime .now();
            if(compare_dates(day,multitransfer.getEnded_at()) > 0){
                for (Transfer transfer : multitransfer.getTransfers()) {
                int status = transfer.getTransfer_status();
                if(transfer.getReceived_at() == null && ( status == TransferServiceImpl.getIdOfStatus("A servir")
                        | status == TransferServiceImpl.getIdOfStatus("Débloqué"))){
                    transfer.setTransfer_status(TransferServiceImpl.getIdOfStatus("Déshérence"));
                    transfer.setMotif("Date of receipt exceeded !");
                 }
            }
        }
        multitransferRepo.save(multitransfer);
        return multitransfer;
    }

    @Override
    public Multitransfer serve_transfer(String reference) {
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        Transfer transfer_tmp = transferService.serve_transfer(transfer);
        transfer_tmp.setMotif(multitransfer.getMotif());
        updateMultitransferByTransfer(multitransfer,transfer_tmp);
        return multitransfer;
    }

    @Override
    public Multitransfer extort_transfer(String reference, String motif) {
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        LocalDateTime day = LocalDateTime .now();
        if((day.getDayOfMonth() - multitransfer.getCreated_at().getDayOfMonth()) != 0){
            throw new TransfersException("You have exceeded the broadcast day !");
        }
        Transfer transfer_tmp = transferService.extort_transfer(transfer, motif);
        transfer_tmp.setMotif(motif);
        updateMultitransferByTransfer(multitransfer,transfer_tmp);
        return multitransfer;
    }

    @Override
    public Multitransfer bloque_transfer(String reference, String motif) {
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        Transfer transfer_tmp =transferService.bloque_transfer(transfer, motif);
        transfer_tmp.setMotif(motif);
        updateMultitransferByTransfer(multitransfer,transfer_tmp);
        return multitransfer;
    }

    @Override
    public Multitransfer return_transfer(String reference, String motif) {
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        Transfer transfer_tmp =transferService.return_transfer(transfer, motif);
        transfer_tmp.setMotif(motif);
        updateMultitransferByTransfer(multitransfer,transfer_tmp);
        return multitransfer;
    }

    @Override
    public Multitransfer unlock_transfer(String reference, String motif) {
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        Transfer transfer_tmp = transferService.unlock_transfer(transfer, motif);
        transfer_tmp.setMotif(multitransfer.getMotif());
        updateMultitransferByTransfer(multitransfer,transfer_tmp);
        return multitransfer;
    }


    private Integer compare_dates(LocalDateTime date1 , LocalDateTime date2){
        LocalDateTime day = LocalDateTime .now();
        return date1.compareTo(date2);
    }

    private Multitransfer setMultitransfer_unique(Transfer transfer){
        Multitransfer multitransfer = getMultiTransferById(transfer.getId_multitransfer());
        List<Transfer> transfers = new ArrayList<Transfer>();
        transfers.add(transfer);
        multitransfer.setTransfers(transfers);
        return multitransfer;
    }

}
