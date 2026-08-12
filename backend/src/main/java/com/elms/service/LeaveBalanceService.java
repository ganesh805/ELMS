package com.elms.service;

import com.elms.dto.request.BalanceAdjustDTO;
import com.elms.dto.response.LeaveBalanceDTO;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional(readOnly = true)
    public List<LeaveBalanceDTO> getUserLeaveBalances(Long userId, Integer year) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        return leaveBalanceRepository.findByUserIdAndYear(userId, targetYear).stream()
                .map(EntityMapper::toLeaveBalanceDTO)
                .toList();
    }

    @Transactional
    public LeaveBalanceDTO adjustBalance(BalanceAdjustDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + dto.getLeaveTypeId()));

        Optional<LeaveBalance> existingOpt = leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(
                dto.getUserId(), dto.getLeaveTypeId(), dto.getYear());

        LeaveBalance balance;
        if (existingOpt.isPresent()) {
            balance = existingOpt.get();
            balance.setAllocated(dto.getAllocated());
            balance.setRemaining(balance.getAllocated() - balance.getUsed());
        } else {
            balance = LeaveBalance.builder()
                    .user(user)
                    .leaveType(leaveType)
                    .year(dto.getYear())
                    .allocated(dto.getAllocated())
                    .used(0)
                    .remaining(dto.getAllocated())
                    .build();
        }

        LeaveBalance saved = leaveBalanceRepository.save(balance);
        return EntityMapper.toLeaveBalanceDTO(saved);
    }
}
