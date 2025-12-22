@Entity
public class ServiceCounter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String counterName;
    private String department;
    private Boolean isActive;
}
