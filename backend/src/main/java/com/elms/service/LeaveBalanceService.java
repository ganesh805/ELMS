package com.elms.service;

import com.elms.dto.response.LeaveBalanceDTO;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<LeaveBalanceDTO> getUserLeaveBalances(Long userId, Integer year) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        int targetYear = (year != null) ? year : Year.now().getValue();
        return leaveBalanceRepository.findByUserIdAndYear(userId, targetYear).stream()
                .map(EntityMapper::toLeaveBalanceDTO)
                .toList();
    }
}
