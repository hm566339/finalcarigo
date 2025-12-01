package com.carigo.drivinglicense.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.drivinglicense.model.DlKycHistory;

import java.util.List;

public interface DlKycHistoryRepository extends JpaRepository<DlKycHistory, Long> {
    List<DlKycHistory> findByDlIdOrderByCreatedAtDesc(String dlId);
}
