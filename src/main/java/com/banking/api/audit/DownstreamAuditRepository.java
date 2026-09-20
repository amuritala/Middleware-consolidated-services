package com.banking.api.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DownstreamAuditRepository extends JpaRepository<DownstreamAudit, UUID> {
}
