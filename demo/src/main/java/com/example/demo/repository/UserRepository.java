package com.example.demo.repository;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    User findById(int id);
    void deleteById(int id);
}