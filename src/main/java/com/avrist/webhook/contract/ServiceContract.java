package com.avrist.webhook.contract;

import com.avrist.webhook.exception.ServiceValidationException;

public interface ServiceContract<INPUT, OUTPUT> {
    OUTPUT execute(INPUT input) throws ServiceValidationException;
}
