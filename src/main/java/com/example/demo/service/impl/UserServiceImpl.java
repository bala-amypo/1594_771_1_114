@Override
public User register(User user) {

    if (user == null) {
        user = new User();
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User savedUser = userRepository.save(user);
    return savedUser;
}
