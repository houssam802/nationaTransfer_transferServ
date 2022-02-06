package com.agent_srv.service;

import com.agent_srv.model.SMSRequest;

public interface SMSService {
    void sendSMS(SMSRequest smsRequest);
}
