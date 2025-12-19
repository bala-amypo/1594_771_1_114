@Override
public ServiceCounter addCounter(ServiceCounter counter) {

    if (counter == null) {
        counter = new ServiceCounter();
    }

    ServiceCounter saved = serviceCounterRepository.save(counter);
    return saved;
}
