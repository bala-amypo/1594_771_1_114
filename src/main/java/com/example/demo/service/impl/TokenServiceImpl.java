@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(
        TokenRepository tokenRepo,
        ServiceCounterRepository counterRepo,
        TokenLogRepository logRepo,
        QueuePositionRepository queueRepo
    ) {
        this.tokenRepo = tokenRepo;
        this.counterRepo = counterRepo;
        this.logRepo = logRepo;
        this.queueRepo = queueRepo;
    }
}
