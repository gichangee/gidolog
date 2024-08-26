package com.gildong.api.repository;

import com.gildong.api.domain.Session;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session,Long> {
    Optional<Session> findByAccessToken(String sessionId);
}
