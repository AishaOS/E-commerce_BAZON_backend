package com.ecommerce.service.auth;

import com.ecommerce.dto.ChangePasswordDto;
import com.ecommerce.dto.SignupRequest;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.UserRole;
import com.ecommerce.repo.OrderRepository;
import com.ecommerce.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (null == adminAccount) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }

    @Transactional
    public UserDto createUser(SignupRequest signupRequest) throws Exception {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(user);
        order.setStatus(OrderStatus.Pending);

        orderRepository.save(order);
        UserDto createdUserDto = new UserDto();
        createdUserDto.setId(createdUser.getId());
        return createdUserDto;
    }


    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto userDto = new UserDto();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userDto = optionalUser.get().getUserDto();
            userDto.setReturnedImg(optionalUser.get().getImg());
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws IOException {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(userDto.getEmail());
            user.setName(userDto.getName());
            if(userDto.getImg() != null){
                user.setImg(userDto.getImg().getBytes());
            }

            User updatedUser = userRepository.save(user);
            UserDto updatedUserDto = new UserDto();
            updatedUserDto.setId(updatedUser.getId());
            return updatedUserDto;
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> updatePasswordById(ChangePasswordDto changePasswordDto) {
        User user = null;
        try {
            Optional<User> userOptional = userRepository.findById(changePasswordDto.getId());
            if (userOptional.isPresent()) {
                user = userOptional.get();
                if (this.bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));
                    User updateUser = userRepository.save(user);
                    UserDto userDto = new UserDto();
                    userDto.setId(updateUser.getId());
                    return ResponseEntity.status(HttpStatus.OK).body(userDto);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Old password is incorrect");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}


