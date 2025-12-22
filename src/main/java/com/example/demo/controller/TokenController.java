@RestController
@RequestMapping("/tokens")
@Tag(name = "Tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue new token")
    public Token issue(@PathVariable Long counterId) {
        return tokenService.issueToken(counterId);
    }
}
