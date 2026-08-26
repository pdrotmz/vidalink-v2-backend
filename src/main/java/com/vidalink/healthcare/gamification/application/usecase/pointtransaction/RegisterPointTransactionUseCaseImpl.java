package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;

import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterPointTransactionUseCaseImpl implements RegisterPointTransactionUseCase {

    private final PointTransactionRepository pointTransactionRepository;
    private final UserRepository userRepository;

    @Override
    public void execute(UUID userId, Integer amount, PointTransactionType type, PointTransactionSource source) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));


        PointTransaction transaction = new PointTransaction();
        transaction.setUserId(user.getId());
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setSource(source);

        pointTransactionRepository.save(transaction);
    }
}
