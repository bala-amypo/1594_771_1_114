public interface TokenLogRepository extends JpaRepository<TokenLog, Long> {
    List<TokenLog> findByToken_IdOrderByLoggedAtAsc(Long tokenId);
}
