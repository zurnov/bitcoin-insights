package com.zurnov.bitcoin.insights.repository;

import com.zurnov.bitcoin.insights.domain.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
}
