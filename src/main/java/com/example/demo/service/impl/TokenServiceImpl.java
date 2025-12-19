@Override
public Token updateStatus(Long tokenId, String status) {

    Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

    String oldStatus = token.getStatus();

    // ❌ Invalid transitions
    if (oldStatus.equals("WAITING") && status.equals("COMPLETED")) {
        throw new IllegalStateException("Invalid status");
    }

    if (oldStatus.equals("SERVING") && status.equals("WAITING")) {
        throw new IllegalStateException("Invalid status");
    }

    token.setStatus(status);

    if (status.equals("COMPLETED")) {
        token.setCompletedAt(LocalDateTime.now());
    }

    Token updated = tokenRepository.save(token);
    return updated;
}
