@Override
public Token issueToken(Long counterId) {

    Token token = new Token();   // ❗ NEVER null

    token.setTokenNumber(UUID.randomUUID().toString());
    token.setStatus("WAITING");

    Token savedToken = tokenRepository.save(token);
    return savedToken;
}
