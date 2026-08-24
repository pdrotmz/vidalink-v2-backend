package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;

import com.vidalink.healthcare.gamification.entity.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.entity.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserPointTransactionsUseCaseImpl implements GetUserPointTransactionsUseCase {

    private final PointTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public List<PointTransactionResponse> execute(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return transactionRepository
                .findByUserId(user.getId())
                .stream()
                .map(transaction ->
                        new PointTransactionResponse(
                                transaction.getId(),
                                transaction.getUserId(),
                                transaction.getAmount(),
                                transaction.getType(),
                                transaction.getSource(),
                                transaction.getCreatedAt()
                        )
                )
                .toList();
    }
}
