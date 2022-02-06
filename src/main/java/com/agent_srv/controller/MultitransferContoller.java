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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api_transfer")
@CrossOrigin(allowCredentials = "true",  originPatterns = "*")
public class MultitransferContoller {

    @Autowired
    private MultitransferService multitransferService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private SMSService smsService;

    private final MultitransferConverter multitransferConverter;
    @Autowired
    public MultitransferContoller(MultitransferConverter multitransferConverter) {
        this.multitransferConverter = multitransferConverter;
    }

    @PostMapping("/createTransfer/agent/byCash")
    public ResponseEntity<Integer> createMultitransferAgent_byCash(@Valid @RequestBody Multitransfer multitransfer){
        Multitransfer multitransfer1 = multitransferService.createTransfer(multitransfer);
        multitransfer1.setTransfer_by_cash(true);
        AtomicReference<Integer> id_multitransfer = new AtomicReference<>(multitransfer1.getId_multitransfer());
        setTransferData(multitransfer1,id_multitransfer);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransfer1.getId_multitransfer());
    }

    @PostMapping("/createTransfer/agent")
    public ResponseEntity<Integer> createMultitransferAgent(@Valid @RequestBody Multitransfer multitransfer){
        Multitransfer multitransfer1 = multitransferService.createTransfer(multitransfer);
        multitransfer1.setRequest_by_prospect_client(false);
        multitransfer1.setTransfer_by_cash(false);
        AtomicReference<Integer> id_multitransfer = new AtomicReference<>(multitransfer1.getId_multitransfer());
        setTransferData(multitransfer1,id_multitransfer);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransfer1.getId_multitransfer());
    }

    @PostMapping("/createTransfer/client")
    public ResponseEntity<Integer> createMultitransferClient(@Valid @RequestBody Multitransfer multitransfer){
        Multitransfer multitransfer1 = multitransferService.createTransfer(multitransfer);
        multitransfer1.setRequest_by_prospect_client(false);
        multitransfer1.setSended_by_agent(0);
        multitransfer1.setTransfer_by_cash(false);
        AtomicReference<Integer> id_multitransfer = new AtomicReference<>(multitransfer1.getId_multitransfer());
        setTransferData(multitransfer1,id_multitransfer);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransfer1.getId_multitransfer());
    }

    @GetMapping("/MultiTransfer/agent")
    public ResponseEntity<List<MultitransferDTO>> getMultiTransferByidAgent(
            @RequestParam("idAgent") int idAgent , @RequestParam("page") int page  ){
        List<Multitransfer> multitransfers = multitransferService.getMultiTransferByidAgent(idAgent,page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(multitransfers));
    }

    @GetMapping("/MultiTransfer/agent/all")
    public ResponseEntity<List<MultitransferDTO>> getMultiTransferByidAgentAll(
            @RequestParam("idAgent") int idAgent ){
        List<Multitransfer> multitransfers = multitransferService.getMultiTransferByidAgentAll(idAgent);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(multitransfers));
    }


    @GetMapping("/MultiTransfers")
    public ResponseEntity<List<MultitransferDTO>> getMultiTransfers(@RequestParam("page") int page  ){
        List<Multitransfer> multitransfers = multitransferService.getMultiTransfers(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(multitransfers));
    }

    @GetMapping("/MultiTransfers/all")
    public ResponseEntity<List<MultitransferDTO>> getMultiTransfersAll(){
        List<Multitransfer> multitransfers = multitransferService.getMultiTransfersAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(multitransfers));
    }

    @GetMapping("/MultiTransfer/count/agent")
    public ResponseEntity<?> countTransferByIdAgent(@RequestParam("idAgent") int idAgent){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferService.countTransferByIdAgent(idAgent));
    }

    @GetMapping("/MultiTransfers/count")
    public ResponseEntity<?> countTransfers(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferService.countTransfers());
    }


    @GetMapping("/MultiTransfers/client/{idClient}")
    public ResponseEntity<List<MultitransferDTO>> getMultiTransferByidClient(
            @PathVariable("idClient") int idClient){
        List<Multitransfer> multitransfers = multitransferService.getMultiTransferByidClient(idClient);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(multitransferConverter.convertToDTOs(multitransfers));
    }

    @PostMapping("/MultiTransfer/client/otp")
    public ResponseEntity<Boolean> sendOtpToClient(
            @RequestParam("otp") String otp ,  @RequestParam("phone_number") String phone_number){
        SMSRequest smsRequest = new SMSRequest("+"+phone_number);
        smsRequest.setOtpVerifyMsg(otp);
        smsService.sendSMS(smsRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(true);
    }




    private void setTransferData(Multitransfer multitransfer1 ,AtomicReference<Integer> id_multitransfer){
        multitransfer1.getTransfers().forEach(transfer -> {
            Integer id_transfer = transfer.getId_transfer();
            transfer.setTransfer_reference("837"+ id_transfer.toString()+ RandomStringUtils.random(10 - id_transfer.toString().length() ,false,true));
            transfer.setMotif(multitransfer1.getMotif());
            transfer.setTransfer_status(1);
            String pin = RandomStringUtils.random(8 ,true,true);

            if(!multitransfer1.isNotify_transfer() && multitransfer1.isTransfer_by_cash()){
                transfer.setCode_pin(TransferServiceImpl.crypt(pin));
                ///send to source
                SMSRequest smsRequest_src = new SMSRequest(multitransfer1.getSender_phnumber());
                smsRequest_src.setNotifyMsgAndPinCode(transfer.getTransfer_reference(),pin,transfer.getTransfer_amount());
                smsService.sendSMS(smsRequest_src);
            }
            if(multitransfer1.isNotify_transfer() && multitransfer1.isTransfer_by_cash()){
                transfer.setCode_pin(TransferServiceImpl.crypt("test"));
                ///send to both
                SMSRequest smsRequest_src = new SMSRequest(multitransfer1.getSender_phnumber());
                smsRequest_src.setNotifyMsg(transfer.getTransfer_reference(),transfer.getTransfer_amount());
                smsService.sendSMS(smsRequest_src);

                SMSRequest smsRequest = new SMSRequest(transfer.getReceiver_phnumber());
                smsRequest.setNotifyMsgAndPinCode(transfer.getTransfer_reference(),pin,transfer.getTransfer_amount());
                smsService.sendSMS(smsRequest);
            }
            transfer.setId_multitransfer(id_multitransfer.get());
            transferService.updateTransferById(transfer.getId_transfer(),transfer);

        });

    }

}
