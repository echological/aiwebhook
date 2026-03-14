package org.techsolution.webhook.contract;

import org.techsolution.webhook.exception.ServiceValidationException;

public interface ServiceContract<INPUT, OUTPUT> {
    OUTPUT execute(INPUT input) throws ServiceValidationException;
}
