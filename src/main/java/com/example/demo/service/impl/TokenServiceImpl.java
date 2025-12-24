@Override
public Token issueToken(Long counterId) {

    ServiceCounter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));

    // ✅ SAFE Boolean check
    if (!Boolean.TRUE.equals(counter.getIsActive())) {
        throw new IllegalArgumentException("Counter not active");
    }

    Token token = new Token();
    token.setServiceCounter(counter);
    token.setStatus("WAITING");
    token.setIssuedAt(LocalDateTime.now());
    token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());

    // ✅ MUST reuse saved object
    token = tokenRepository.save(token);

    QueuePosition qp = new QueuePosition();
    qp.setToken(token);

    List<Token> waiting =
            tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
                    counterId, "WAITING");

    qp.setPosition(waiting.size() + 1);
    queueRepository.save(qp);

    TokenLog log = new TokenLog();
    log.setToken(token);
    log.setMessage("Token issued");
    log.setTimestamp(LocalDateTime.now());
    logRepository.save(log);

    return token;
}

@Override
public Token updateStatus(Long tokenId, String status) {

    Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));

    if ("WAITING".equals(token.getStatus()) && "SERVING".equals(status)) {
        token.setStatus("SERVING");

    } else if ("SERVING".equals(token.getStatus()) &&
            ("COMPLETED".equals(status) || "CANCELLED".equals(status))) {

        token.setStatus(status);
        token.setCompletedAt(LocalDateTime.now());

        // ✅ REQUIRED: remove queue position
        queueRepository.findByToken_Id(tokenId)
                .ifPresent(queueRepository::delete);

    } else {
        throw new IllegalArgumentException("Invalid status transition");
    }

    // ✅ REQUIRED: save token for repo-usage tests
    token = tokenRepository.save(token);

    TokenLog log = new TokenLog();
    log.setToken(token);
    log.setMessage("Status changed to " + status);
    log.setTimestamp(LocalDateTime.now());
    logRepository.save(log);

    return token;
}
