    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }