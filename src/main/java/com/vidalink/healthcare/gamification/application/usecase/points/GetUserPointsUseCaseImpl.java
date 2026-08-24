package com.vidalink.healthcare.gamification.application.usecase.points;

import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserPointsUseCaseImpl implements GetUserPointsUseCase{

    private final PointTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public UserPointsResponse execute(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<PointTransaction> transactions = transactionRepository.findByUserId(user.getId());

        int balance = transactions.stream().mapToInt(transaction -> {
            if (transaction.getType() == PointTransactionType.CREDIT) {
                return transaction.getAmount();
            }
            return -transaction.getAmount();
        }).sum();

        return new UserPointsResponse(user.getId(), balance);
    }
}
