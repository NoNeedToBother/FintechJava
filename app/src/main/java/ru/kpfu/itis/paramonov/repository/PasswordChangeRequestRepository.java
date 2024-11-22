package ru.kpfu.itis.paramonov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.paramonov.entity.PasswordChangeRequest;

@Repository
public interface PasswordChangeRequestRepository extends JpaRepository<PasswordChangeRequest, Long> {

    @Query(value = "delete from password_change_requests as req where req.user_id = :userId", nativeQuery = true)
    @Modifying
    void removePasswordChangeRequestsByUser(Long userId);

    @Query("select req from PasswordChangeRequest req where req.user.id = :userId")
    PasswordChangeRequest getRequestByUserId(Long userId);
}
