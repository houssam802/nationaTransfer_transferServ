package com.agent_srv.service.impl;

import com.agent_srv.config.TwilioConfig;
import com.agent_srv.config.TwilioInitializer;
import com.agent_srv.model.SMSRequest;
import com.agent_srv.service.SMSService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioInitializer.class);
    private final TwilioConfig twilioConfig;

    @Autowired
    public SMSServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSMS(SMSRequest smsRequest) {
        MessageCreator message = Message.creator(
                new PhoneNumber(smsRequest.getTo()),
                new PhoneNumber(twilioConfig.getPhone_number()),
                smsRequest.getMessage()
        );
        message.create();
        LOGGER.info("SMS send {} ",smsRequest);

    }
}
