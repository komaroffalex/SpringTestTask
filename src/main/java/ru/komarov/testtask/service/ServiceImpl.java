package ru.komarov.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.komarov.testtask.entity.User;
import ru.komarov.testtask.entity.UserStatus;
import ru.komarov.testtask.repository.UserRepository;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class ServiceImpl {
    private final UserRepository userRepository;
    private Map<String, UserStatus> statusTracker;
    @NotNull
    private final ScheduledExecutorService workerThreads;
    private Map<String, ScheduledFuture<?>> futures;

    @Autowired
    public ServiceImpl(@NotNull final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.statusTracker = new HashMap<>();
        this.workerThreads = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                new ThreadFactoryBuilder().setNameFormat("worker").build());
        this.futures = new HashMap<>();
    }

    public User findUser(@NotNull final String userId) throws NoSuchElementException {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(()
                -> new NoSuchElementException("User is not present!"));
    }

    public Long upsertUser(@NotNull final String firstName, @NotNull final String lastName,
                           @NotNull final String email, @NotNull final String phone) {
        final User testUsr1 = new User();
        testUsr1.setFirstName(firstName);
        testUsr1.setLastName(lastName);
        testUsr1.setEmail(email);
        testUsr1.setPhone(phone);
        return userRepository.save(testUsr1).getId();
    }

    public UserStatus getStatus(@NotNull final String userId) throws NoSuchElementException {
        findUser(userId);
        Optional<UserStatus> status = Optional.ofNullable(statusTracker.get(userId));
        return status.orElse(UserStatus.OFFLINE);
    }

    public void setStatus(@NotNull final String userId, @NotNull final UserStatus status)
            throws NoSuchElementException {
        findUser(userId);
        statusTracker.put(userId, status);
        Optional<ScheduledFuture<?>> task = Optional.ofNullable(futures.get(userId));
        task.ifPresent((s) -> s.cancel(false));
        if (task.isPresent()) futures.remove(userId);
        futures.put(userId, workerThreads.schedule(() -> statusTracker.put(userId, UserStatus.AWAY),
                5, TimeUnit.MINUTES));
    }
}
