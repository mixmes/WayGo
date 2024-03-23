package ru.sfedu.server.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.subscription.SubscriptionTransactionDTO;
import ru.sfedu.server.model.subscription.SubscriptionTransaction;

@Component
public class SubscriptionTransactionConverter implements Converter<SubscriptionTransaction, SubscriptionTransactionDTO> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public SubscriptionTransactionDTO convertToDto(SubscriptionTransaction entity) {
        return mapper.map(entity, SubscriptionTransactionDTO.class);
    }

    @Override
    public SubscriptionTransaction convertToEntity(SubscriptionTransactionDTO dto) {
        return mapper.map(dto, SubscriptionTransaction.class);
    }
}
