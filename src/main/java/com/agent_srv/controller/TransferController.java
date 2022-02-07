package com.agent_srv.controller;


import com.agent_srv.controller.converter.MultitransferConverter;
import com.agent_srv.dto.MultitransferDTO;
import com.agent_srv.model.Multitransfer;
import com.agent_srv.model.SMSRequest;
import com.agent_srv.model.Transfer;
import com.agent_srv.service.MultitransferService;
import com.agent_srv.service.SMSService;
import com.agent_srv.service.TransferService;
import com.agent_srv.service.impl.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api_transfer")
@CrossOrigin(allowCredentials = "true",  originPatterns = "*")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private MultitransferService multitransferService;

    @Autowired
    private SMSService smsService;

    private final MultitransferConverter multitransferConverter;
    @Autowired
    public TransferController(MultitransferConverter multitransferConverter) {
        this.multitransferConverter = multitransferConverter;
    }

    @GetMapping("/UniqueTransfer/{reference}")
    public ResponseEntity<MultitransferDTO> getTransferByReference(@PathVariable("reference") String reference){
        Transfer transfer = transferService.getTransferByReference(reference);
        Multitransfer multitransfer = multitransferService.getMultiTransferById(transfer.getId_multitransfer());
        List<Transfer> transfers = new ArrayList<Transfer>();
        transfers.add(transfer);
        multitransfer.setTransfers(transfers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }

    @GetMapping("/UniqueTransfer/wallet/{reference}")
    public ResponseEntity<MultitransferDTO> getWalletTransferByReference(@PathVariable("reference") String reference){
        Transfer transfer = transferService.getWalletTransferByReference(reference);
        Multitransfer multitransfer = multitransferService.getMultiTransferById(transfer.getId_multitransfer());
        List<Transfer> transfers = new ArrayList<Transfer>();
        transfers.add(transfer);
        multitransfer.setTransfers(transfers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }


    @GetMapping("/UniqueTransfer/status/{status}")
    public ResponseEntity<List<MultitransferDTO>> getTransferByStatus(@PathVariable("status") String status){
        List<List<Transfer>> list_transfers = transferService.getTransferByStatus(status);
        List<Multitransfer> list_multitransfer = new ArrayList<>();
        list_transfers.forEach(transfers -> {
            Multitransfer multitransfer = multitransferService.getMultiTransferById(transfers.get(0).getId_multitransfer());
            multitransfer.setTransfers(transfers);
            list_multitransfer.add(multitransfer);
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(TransferServiceImpl.cleanList_by_status(list_multitransfer,status)));
    }

    @PutMapping("/UniqueTransfer/{id}")
    public Transfer updateTransferById(@PathVariable("id") int id,
                                       @RequestBody Transfer transfer){
        return transferService.updateTransferById(id,transfer);
    }

    @PostMapping("/UniqueTransfer/serve/{reference}")
    public ResponseEntity<MultitransferDTO> serveTransferByReference(@PathVariable("reference") String reference){
        Multitransfer multitransfer=multitransferService.serve_transfer(reference);
        Transfer transfer = multitransfer.getTransfers().get(0);
        SMSRequest smsRequest_src = new SMSRequest(multitransfer.getSender_phnumber());
        smsRequest_src.setExtort_NotifyMsg("",multitransfer);
        smsService.sendSMS(smsRequest_src);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }


    @PostMapping("/UniqueTransfer/serve/{reference}")
    public ResponseEntity<?> checkRecipientInfosByReference(@PathVariable("reference") String reference,
                                                            @RequestBody Transfer transfer){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transferService.checkRecipientInfosByReference(reference,transfer));
    }

    @PostMapping("/UniqueTransfer/extort/{reference}")
    public ResponseEntity<MultitransferDTO> extortTransferByReference(@PathVariable("reference") String reference,
                                                                      @RequestParam("motif") String motif){
        Multitransfer multitransfer=multitransferService.extort_transfer(reference,motif);
        Transfer transfer = multitransfer.getTransfers().get(0);
        SMSRequest smsRequest_src = new SMSRequest(multitransfer.getSender_phnumber());
        smsRequest_src.setExtort_NotifyMsg(motif,multitransfer);
        smsService.sendSMS(smsRequest_src);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }

    @PutMapping("/UniqueTransfer/bloque/{reference}")
    public ResponseEntity<MultitransferDTO> bloqueTransferByReference(@PathVariable("reference") String reference,
                                                                       @RequestParam("motif") String motif){
        Multitransfer multitransfer = multitransferService.bloque_transfer(reference,motif);
        Transfer transfer = multitransfer.getTransfers().get(0);
        SMSRequest smsRequest_src = new SMSRequest(multitransfer.getSender_phnumber());
        smsRequest_src.setLock_NotifyMsg(motif,multitransfer);
        smsService.sendSMS(smsRequest_src);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }

    @PutMapping("/UniqueTransfer/return/{reference}")
    public ResponseEntity<MultitransferDTO> returnTransferByReference(@PathVariable("reference") String reference,
                                                                      @RequestParam("motif") String motif){
        Multitransfer multitransfer = multitransferService.return_transfer(reference,motif);
        Transfer transfer = multitransfer.getTransfers().get(0);
        SMSRequest smsRequest_src = new SMSRequest(multitransfer.getSender_phnumber());
        smsRequest_src.setReturn_NotifyMsg(motif,multitransfer);
        smsService.sendSMS(smsRequest_src);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }

    @PutMapping("/UniqueTransfer/unlock/{reference}")
    public ResponseEntity<MultitransferDTO> unlockTransferByReference(@PathVariable("reference") String reference,
                                                                      @RequestParam("motif") String motif){
        Multitransfer multitransfer = multitransferService.unlock_transfer(reference,motif);
        Transfer transfer = multitransfer.getTransfers().get(0);
        SMSRequest smsRequest_src = new SMSRequest(multitransfer.getSender_phnumber());
        smsRequest_src.setUnLock_NotifyMsg(motif,multitransfer);
        smsService.sendSMS(smsRequest_src);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }


    @GetMapping("/UniqueTransfer/pin_code/{reference}")
    public ResponseEntity<MultitransferDTO> checkTransfer_code_pin(@PathVariable("reference") String reference,
                                           @RequestParam("code_pin") String pin_code){
        Transfer transfer = transferService.checkTransfer_code_pin(reference,pin_code);
        Multitransfer multitransfer = multitransferService.getMultiTransferById(transfer.getId_multitransfer());
        List<Transfer> transfers = new ArrayList<Transfer>();
        transfers.add(transfer);
        multitransfer.setTransfers(transfers);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTO(multitransfer));
    }
}
