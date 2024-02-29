package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {
    @Mock
    private UsersRepository usersRepository;

    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    void shouldAuthenticateWithCorrectCredentials() throws AlreadyAuthenticatedException, EntityNotFoundException {
        String login = "testUser";
        String password = "password123";
        User mockUser = new User(2L, login, password);
        when(usersRepository.findByLogin(login)).thenReturn(mockUser);
        boolean result = usersService.authenticate(login, password);

        assertTrue(result);

        verify(usersRepository, times(1)).update(mockUser);
    }

    @Test
    void shouldThrowExceptionForIncorrectLogin() {
        String login = "nonExistentUser";
        String password = "password123";

        when(usersRepository.findByLogin(login)).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> usersService.authenticate(login, password));
        verify(usersRepository, never()).update(any());
    }

    @Test
    void shouldNotAuthenticateWithIncorrectPassword() {
        String login = "nonExistentUser";
        String password = "password123";
        String incorrectPassword = "password";
        User mockUser = new User(2L, login, password);
        when(usersRepository.findByLogin(login)).thenReturn(mockUser);
        boolean result = usersService.authenticate(login, incorrectPassword);

        assertFalse(result);

        verify(usersRepository, never()).update(any());
    }

    @Test
    void shouldThrowAlreadyAuthenticatedException() {
        String login = "testUser";
        String password = "password123";
        User mockUser = new User(2L, login, password);
        mockUser.setAuthenticated(true);
        when(usersRepository.findByLogin(login)).thenReturn(mockUser);
        assertThrows(AlreadyAuthenticatedException.class, () -> usersService.authenticate(login, password));

        verify(usersRepository, never()).update(any());
    }
}
