package com.vidalink.healthcare.gamification.application.usecase.level;

import com.vidalink.healthcare.gamification.application.dto.response.level.UserLevelResponse;
import com.vidalink.healthcare.gamification.domain.enums.level.Level;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserLevelUseCaseImpl implements GetUserLevelUseCase{

    private final PointTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public UserLevelResponse execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        int totalPoints = transactionRepository
                .findByUserId(user.getId())
                .stream()
                .filter(transaction ->
                        transaction.getType() == PointTransactionType.CREDIT)
                .mapToInt(PointTransaction::getAmount)
                .sum();

        Level level = Arrays.stream(Level.values())
                .filter(currentLevel -> totalPoints >= currentLevel.getRequiredPoints())
                .max(Comparator.comparingInt(Level::getRequiredPoints))
                .orElse(Level.BEGINNER);

        return new UserLevelResponse(
                user.getId(),
                level,
                totalPoints
        );
    }
}
