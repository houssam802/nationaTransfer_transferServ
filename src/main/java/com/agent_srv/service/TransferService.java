package com.agent_srv.service;

import com.agent_srv.model.Multitransfer;
import com.agent_srv.model.Transfer;

import java.util.List;
import java.util.Map;

public interface TransferService{
    Transfer getTransferByReference(String reference);

    Transfer updateTransferById(int id, Transfer transfer);

    List<List<Transfer>> getTransferByStatus(String Status);

   // Transfer updateTransferByReference(String reference, Transfer transfer);

    Transfer checkTransfer_code_pin(String reference, String pin_code);

    Transfer extort_transfer(Transfer transfer, String motif);

    Transfer bloque_transfer(Transfer transfer, String motif);

    Transfer return_transfer(Transfer transfer, String motif);

    Transfer unlock_transfer(Transfer transfer, String motif);

    Transfer serve_transfer(Transfer transfer);

    Map<String, Boolean> checkRecipientInfosByReference(String reference, Transfer transfer);

    Transfer getWalletTransferByReference(String reference);
}
