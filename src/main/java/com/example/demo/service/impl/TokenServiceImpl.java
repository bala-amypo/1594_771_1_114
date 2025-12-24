@Override
public Token issueToken(Long counterId) {

    ServiceCounter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));

    if (!counter.getIsActive()) {
        throw new IllegalArgumentException("Counter not active");
    }

    Token token = new Token();
    token.setServiceCounter(counter);
    token.setStatus("WAITING");
    token.setIssuedAt(LocalDateTime.now());
    token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());

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

    } else {
        throw new IllegalArgumentException("Invalid status transition");
    }

    TokenLog log = new TokenLog();
    log.setToken(token);
    log.setMessage("Status changed to " + status);
    log.setTimestamp(LocalDateTime.now());
    logRepository.save(log);

    return token;
}
