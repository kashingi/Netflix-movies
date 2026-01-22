package com.netflix.serviceImpl;

import com.netflix.dao.UserRepository;
import com.netflix.dto.request.UserRequest;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.UserResponse;
import com.netflix.entity.User;
import com.netflix.enums.Role;
import com.netflix.exception.EmailAlreadyExistsException;
import com.netflix.exception.InvalidRoleException;
import com.netflix.service.EmailService;
import com.netflix.service.UserService;
import com.netflix.util.PaginationUtils;
import com.netflix.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

//Add your annotations here
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ServiceUtils serviceUtils;
    @Autowired
    private EmailService emailService;

    @Override
    public MessageResponse createUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        validateRole(userRequest.getRole());

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        user.setRole(Role.valueOf(userRequest.getRole().toUpperCase()));
        user.setActive(true);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));

        //Save user
        userRepository.save(user);

        //Send email
        emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);

        return new MessageResponse("User created successfully.");
    }

    private void validateRole(String role) {
        if (Arrays.stream(Role.values()).noneMatch(r-> r.name().equalsIgnoreCase(role))) {
            throw new InvalidRoleException("Invalid role : " + role);
        }
    }

    @Override
    public MessageResponse updateUser(Long id, UserRequest userRequest) {
        User user = serviceUtils.getUserByIdOrThrow(id);

        ensureNotLastActiveAdmin(user);
        validateRole(userRequest.getRole());

        user.setFullName(userRequest.getFullName());
        user.setRole(Role.valueOf(userRequest.getRole().toUpperCase()));

        //Save updated user
        userRepository.save(user);

        return new MessageResponse("User updated successfully.");
    }

    private void ensureNotLastActiveAdmin(User user) {
        if (user.isActive() && user.getRole() == Role.ADMIN) {
            long activeAdminCount = userRepository.countByRoleAndActive(Role.ADMIN, true);

            if (activeAdminCount <= 1) {
                throw new RuntimeException("Cannot deactivate the last admin user");
            }
        }
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size);

        Page<User> userPage;

        if (search != null && !search.trim().isEmpty()) {
            userPage = userRepository.searchUsers(search.trim(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        return PaginationUtils.toPageResponse(userPage, UserResponse::fromEntity);
    }

    @Override
    public MessageResponse deleteUser(Long id, String currentUserEmail) {
        User user = serviceUtils.getUserByIdOrThrow(id);

        if (user.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You cannot delete your own account");
        }

        ensureNotLastAdmin(user, "delete");

        userRepository.deleteById(id);

        return new MessageResponse("User deleted successfully");
    }

    private void ensureNotLastAdmin(User user, String operation) {
        if (user.getRole()  == Role.ADMIN) {
            long adminCount = userRepository.countByRole(Role.ADMIN);

            if (adminCount <= 1) {
                throw new RuntimeException("You cannot " + operation + " the last admin user");
            }
        }
    }

    @Override
    public MessageResponse toggleUserStatus(Long id, String currentUserEmail) {
        User user = serviceUtils.getUserByIdOrThrow(id);

        if (user.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You cannot deactivate your own account");
        }

        ensureNotLastActiveAdmin(user);

        user.setActive(!user.isActive());

        userRepository.save(user);

        return new MessageResponse("User status updated successfully.");
    }

    @Override
    public MessageResponse changeUserRole(Long id, UserRequest userRequest) {
        User user = serviceUtils.getUserByIdOrThrow(id);

        validateRole(userRequest.getRole());

        Role newRole = Role.valueOf(userRequest.getRole().toUpperCase(Locale.ROOT));
        if (user.getRole() == Role.ADMIN && newRole == Role.USER) {
            ensureNotLastAdmin(user, "change the role of");
        }

        user.setRole(newRole);
        userRepository.save(user);

        return new MessageResponse("User role updated successfully.");
    }

}
