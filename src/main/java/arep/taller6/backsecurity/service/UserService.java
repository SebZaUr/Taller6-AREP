package arep.taller6.backsecurity.service;

import arep.taller6.backsecurity.model.UserDTO;
import arep.taller6.backsecurity.enums.RoleEntity;
import arep.taller6.backsecurity.model.UserEntity;
import arep.taller6.backsecurity.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity save (UserDTO userDTO) {
        RoleEntity rol = RoleEntity.valueOf(userDTO.getRole());
        UserEntity user = UserEntity.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encrypt(userDTO.getPassword()))
                .role(rol)
                .build();
        userRepository.save(user);
        return user;
    }

    public List<UserEntity> getUser(UserDTO userDTO) {
        Optional<UserEntity> user = userRepository.findByEmail(userDTO.getEmail());
        if(user.isPresent()){
            UserEntity userEntity = (UserEntity) user.get();
            if(userEntity.getPassword().equals(encrypt(userDTO.getPassword()))) {
                return userRepository.findAll();
            }else{
                throw new RuntimeException("The password is incorrect");
            }
        }else{
            throw new RuntimeException("The user is not in the database");
        }
    }

    /**
     * Me permite encriptar la contraseña para guardarla en la base de datos
     *
     * @param password La contraseña que voy a encriptar.
     */
    public String encrypt(String password){
        String encryptPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02X", b));
            }
            encryptPassword = hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptPassword;
    }

    public Iterable<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
