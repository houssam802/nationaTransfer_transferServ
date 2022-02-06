package com.agent_srv.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class TwilioConfig {
    @Value( "${twilio.auth_token}" )
    private String auth_token;

    @Value( "${twilio.account_sid}" )
    private String account_sid;

    @Value( "${twilio.phone_number}" )
    private String phone_number;
}
