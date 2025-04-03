package org.yvesguilherme.statistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.yvesguilherme.statistics.domain.Transaction;
import org.yvesguilherme.statistics.request.TransactionRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
  Transaction toTransaction(TransactionRequest transactionRequest);
}
