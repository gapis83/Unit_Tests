package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;

public class UsersServiceImpl {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    boolean authenticate(String login, String password) throws AlreadyAuthenticatedException {
        boolean result = false;
        try {
            User user = usersRepository.findByLogin(login);
            if (!user.isAuthenticated() && user.getPassword().equals(password)) {
                user.setAuthenticated(true);
                usersRepository.update(user);
                result = true;
            } else if (user.isAuthenticated()) {
                throw new AlreadyAuthenticatedException("User is already authenticated");
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
        return result;
    }

}
