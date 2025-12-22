public interface TokenRepository
        extends JpaRepository<Token, Long> {

    List<Token> findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
        Long counterId, String status);

    Optional<Token> findByTokenNumber(String tokenNumber);
}
