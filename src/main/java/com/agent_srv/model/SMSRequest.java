package com.agent_srv.model;

import lombok.Data;

@Data
public class SMSRequest {
    private String to;
    private String message;

    public SMSRequest(String reciever_phnumber) {
        to = reciever_phnumber;
    }

    public void setNotifyMsg(String transfer_reference,float montant){
        message = "Transfer reference : " + transfer_reference;
        message = message + "\n -- Montant : " + montant;
    }

    public void setNotifyMsgAndPinCode(String transfer_reference,String pin_code,float montant){
        message = "Transfer reference : " + transfer_reference;
        message = message + "\n -- Pin Code : " + pin_code;
        message = message + "\n -- Montant : " + montant+ " DH";
    }

    public void setLock_NotifyMsg(String motif , Multitransfer multitransfer){
        message = "Votre transfer est blocké -- ";
        message = message + "\n -- Transfer reference : " + multitransfer.getTransfers().get(0).getTransfer_reference();
        message = message + "\n -- Montant : " + multitransfer.getTransfers().get(0).getTransfer_amount()+ " DH";
        message = message + "\n -- Béneficier : " + multitransfer.getTransfers().get(0).getReceiver_fname() + multitransfer.getTransfers().get(0).getReceiver_lname();
        message = message + "\n -- Motif : " + multitransfer.getTransfers().get(0).getMotif();
    }

    public void setUnLock_NotifyMsg(String motif , Multitransfer multitransfer){
        message = "Votre transfer est déblocké -- ";
        message = message + "\n -- Transfer reference : " + multitransfer.getTransfers().get(0).getTransfer_reference();
        message = message + "\n -- Montant : " + multitransfer.getTransfers().get(0).getTransfer_amount()+ " DH";
        message = message + "\n -- Béneficier : " + multitransfer.getTransfers().get(0).getReceiver_fname() + multitransfer.getTransfers().get(0).getReceiver_lname();
        message = message + "\n -- Motif : " + multitransfer.getTransfers().get(0).getMotif();
    }

    public void setExtort_NotifyMsg(String motif , Multitransfer multitransfer){
        message = "Votre transfer est extourné -- ";
        message = message + "\n -- Transfer reference : " + multitransfer.getTransfers().get(0).getTransfer_reference();
        message = message + "\n -- Montant : " + multitransfer.getTransfers().get(0).getTransfer_amount()+ " DH";
        message = message + "\n -- Béneficier : " + multitransfer.getTransfers().get(0).getReceiver_fname() + multitransfer.getTransfers().get(0).getReceiver_lname();
        message = message + "\n -- Motif : " + multitransfer.getTransfers().get(0).getMotif();
    }

    public void setReturn_NotifyMsg(String motif , Multitransfer multitransfer){
        message = "Votre transfer est restitué -- ";
        message = message + "\n -- Transfer reference : " + multitransfer.getTransfers().get(0).getTransfer_reference();
        message = message + "\n -- Montant : " + multitransfer.getTransfers().get(0).getTransfer_amount()+ " DH";
        message = message + "\n -- Béneficier : " + multitransfer.getTransfers().get(0).getReceiver_fname() + multitransfer.getTransfers().get(0).getReceiver_lname();
        message = message + "\n -- Motif : " + multitransfer.getTransfers().get(0).getMotif();
    }

    public void setOtpVerifyMsg(String otp) {
        message = " -- OTP : " + otp;
    }
}
