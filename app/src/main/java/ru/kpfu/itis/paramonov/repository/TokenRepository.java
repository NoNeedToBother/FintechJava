package ru.kpfu.itis.paramonov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.paramonov.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where t.user.id = :userId and t.token = :token")
    Token getTokenByUserIdAndToken(Long userId, String token);

    @Query("update Token t set t.invalidated = true where t.user.id = :userId")
    @Modifying
    void invalidateAllTokensByUserId(Long userId);
}
