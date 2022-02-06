package com.agent_srv.service.impl;

import com.agent_srv.exception.TransfersException;
import com.agent_srv.model.Multitransfer;
import com.agent_srv.model.Transfer;
import com.agent_srv.repository.TransferRepo;
import com.agent_srv.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class TransferServiceImpl implements TransferService {

    public static List<String> transfer_status = Arrays.asList("A servir","Servie" ,"Extourné","Restitué","Bloqué","Débloqué","Déshérence") ;

    @Autowired
    private TransferRepo transferRepo;

    private MultitransferServiceImpl multitransferService;

    @Override
    public Transfer getTransferByReference(String reference) {
        Transfer transferDb = transferRepo.getTransferByreference(reference);
        if(transferDb == null ) throw new TransfersException("inexistent reference !");
        else{
            return transferDb;
        }
    }

    @Override
    public Transfer getWalletTransferByReference(String reference) {
        Transfer transferDb = transferRepo.getTransferByWalletreference(reference);
        if(transferDb == null ) throw new TransfersException("inexistent reference !");
        else{
            return transferDb;
        }
    }

    @Override
    public List<List<Transfer>> getTransferByStatus(String status) {
        Integer status_id = getIdOfStatus(status);
        List<Transfer> transferDb = transferRepo.getTransferByStatus(status_id);
        if(transferDb == null ) throw new TransfersException("inexistent Status !");
        else return groupTransfers_by_multid(transferDb);
    }

    @Override
    public Transfer checkTransfer_code_pin(String reference, String pin_code) {
        Transfer transfer = transferRepo.getTransferByreference(reference);
        if (transfer == null) throw new TransfersException(" wrong information !");
        else {
            if (!decrypt(pin_code, transfer.getCode_pin())) throw new TransfersException(" wrong information !");
        }
        return transfer;

    }

    @Override
    public Transfer serve_transfer(Transfer transfer) {
        if(transfer.getTransfer_status()!=getIdOfStatus("Restitué") && transfer.getTransfer_status()!=getIdOfStatus("bloqué")){
            if(transfer.getTransfer_status()==getIdOfStatus("Déshérence")){
                throw new TransfersException(" Escheat transfer !");
            }
            transfer.setTransfer_status(getIdOfStatus("Servie"));
            transfer.setReceived_at(LocalDateTime.now());
        }else throw new TransfersException(" Transfer already Blocked !");
        return transfer;
    }

    @Override
    public Map<String, Boolean> checkRecipientInfosByReference(String reference, Transfer transfer) {
        Transfer transferdb = transferRepo.checkRecipientInfosByReference(reference,transfer.getReceiver_fname(),transfer.getReceiver_lname(),transfer.getReceiver_phnumber());
        if(transferdb == null ) throw new TransfersException("invalid reference infos !");
        else{
            Map<String, Boolean> infos = new HashMap<>();
            infos.put("infos", true);
            infos.put("pin", (transferdb.getCode_pin()!=null));
            return infos;
        }
    }


    @Override
    public Transfer extort_transfer(Transfer transfer, String motif) {
        if(transfer.getTransfer_status()!=getIdOfStatus("Restitué") && transfer.getTransfer_status()!=getIdOfStatus("bloqué")){
            if(transfer.getTransfer_status()==getIdOfStatus("Déshérence")){
                throw new TransfersException(" Escheat transfer !");
            }
            if(transfer.getTransfer_status()==getIdOfStatus("Servie")){
                throw new TransfersException(" Already served !");
            }
            transfer.setTransfer_status(getIdOfStatus("Extourné"));
            transfer.setMotif(motif);
        }else throw new TransfersException(" Transfer already Blocked !");
        return transfer;
    }

    @Override
    public Transfer bloque_transfer(Transfer transfer, String motif) {
        if(transfer.getTransfer_status()!=getIdOfStatus("Restitué") && transfer.getTransfer_status()!=getIdOfStatus("bloqué")){
            if(transfer.getTransfer_status()==getIdOfStatus("Déshérence")){
                throw new TransfersException(" Escheant transfer !");
            }
            if(transfer.getTransfer_status()==getIdOfStatus("Servie")){
                throw new TransfersException(" Already served !");
            }
            transfer.setTransfer_status(getIdOfStatus("Bloqué"));
            transfer.setMotif(motif);
        }else throw new TransfersException(" Transfer already Blocked !");
        return transfer;
    }

    @Override
    public Transfer return_transfer(Transfer transfer, String motif) {
        if(transfer.getTransfer_status()!=getIdOfStatus("Restitué") && transfer.getTransfer_status()!=getIdOfStatus("bloqué")){
            if(transfer.getTransfer_status()==getIdOfStatus("Déshérence")){
                throw new TransfersException(" Escheat transfer !");
            }
            if(transfer.getTransfer_status()==getIdOfStatus("Servie")){
                throw new TransfersException(" Already served !");
            }
            transfer.setTransfer_status(getIdOfStatus("Restitué"));
            transfer.setMotif(motif);
        }else throw new TransfersException(" Transfer already Blocked !");
        return transfer;
    }

    @Override
    public Transfer unlock_transfer(Transfer transfer, String motif) {
            if(transfer.getTransfer_status()==getIdOfStatus("Déshérence")){
                throw new TransfersException(" Escheat transfer !");
            }
            if(transfer.getTransfer_status()==getIdOfStatus("Servie")){
                throw new TransfersException(" Already served !");
            }
            transfer.setTransfer_status(getIdOfStatus("Débloqué"));
            transfer.setMotif(motif);
            return transfer;
    }
/*
    @Override
    public Transfer updateTransferByReference(String reference, Transfer transfer) {
        Transfer transferDb = transferRepo.getTransferByreference(reference);
        if(transferDb == null ) throw new TransfersException("inexistent transfer !");

        if(Objects.nonNull(transfer.getTransfer_status())){
            transferDb.setTransfer_status(transfer.getTransfer_status());
        }
        if(Objects.nonNull(transfer.getReceived_at())){
            transferDb.setReceived_at(transfer.getReceived_at());
        }
        if(Objects.nonNull(transfer.getTransfer_reference())){
            transferDb.setTransfer_reference(transfer.getTransfer_reference());
        }
        if(Objects.nonNull(transfer.getCode_pin())){
            transferDb.setCode_pin(transfer.getCode_pin());
        }

        return transferRepo.save(transfer);
    }*/

    @Override
    public Transfer updateTransferById(int id, Transfer transfer) {
        Transfer transferDb = transferRepo.findById(id).get();
        if(transferDb == null ) throw new TransfersException("inexistent transfer !");

        if(Objects.nonNull(transfer.getTransfer_status())){
            transferDb.setTransfer_status(transfer.getTransfer_status());
        }
        if(Objects.nonNull(transfer.getReceived_at())){
            transferDb.setReceived_at(transfer.getReceived_at());
        }
        if(Objects.nonNull(transfer.getTransfer_reference())){
            transferDb.setTransfer_reference(transfer.getTransfer_reference());
        }
        if(Objects.nonNull(transfer.getCode_pin())){
            transferDb.setCode_pin(transfer.getCode_pin());
        }
        if(Objects.nonNull(transfer.getMotif())){
            transferDb.setMotif(transfer.getMotif());
        }

        return transferRepo.save(transfer);
    }

    public static Integer getIdOfStatus(String status){
        return transfer_status.indexOf(status) + 1 ;
    }

    private List<List<Transfer>> groupTransfers_by_multid(List<Transfer> transfers){
        List<List<Transfer>> list_transfers_tmp = new ArrayList<>();

        List<Transfer> transfers_tmp = new ArrayList<Transfer>(transfers);
        List<Transfer> transfers_tmp2 = new ArrayList<Transfer>(transfers);

        AtomicInteger k= new AtomicInteger(transfers_tmp.size());
        for(int i = 0; i< k.get(); i++){
            List<Transfer> transfers_new_elem = new ArrayList<>();
            transfers_new_elem.add(transfers_tmp.get(i));
            int finalI = i;
            transfers_tmp2.remove(transfers_tmp.get(i));
            transfers_tmp2.forEach(elem2 -> {
                if(transfers_tmp.get(finalI).getId_multitransfer().equals(elem2.getId_multitransfer()) &&
                        !transfers_tmp.get(finalI).getId_transfer().equals(elem2.getId_transfer()) ){
                    transfers_new_elem.add(elem2);
                    transfers_tmp.remove(elem2);
                    k.set(k.get() - 1 );
                }
            });
            list_transfers_tmp.add(transfers_new_elem);
        }


        return list_transfers_tmp ;
    }

    public static List<Multitransfer> cleanList_by_status(List<Multitransfer> multitransfers , String status){
        List<Multitransfer>  multitransfers_tmp = new ArrayList<>();
        multitransfers.stream()
                .filter(mltr -> mltr.getTransfers().get(0).getTransfer_status() != getIdOfStatus(status))
                .forEach(elem -> {multitransfers_tmp.add(elem);});
        multitransfers.removeAll(multitransfers_tmp);
        return multitransfers;
    }

    public static String crypt(String code_pin)  {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = code_pin.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }



    public static boolean decrypt(String code_pin,String hash_code_pin)  {
        if(hash_code_pin.equals(crypt(code_pin))) return true;
        else return false;
    }

}
