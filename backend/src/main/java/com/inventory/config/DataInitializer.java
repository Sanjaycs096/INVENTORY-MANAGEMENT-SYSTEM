package com.inventory.config;

import com.inventory.model.User;
import com.inventory.model.User.Role;
import com.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes default data on application startup.
 * Creates admin user if not exists, and resets password if hash is invalid.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminUser();
        initDemoUser();
    }

    private void initDemoUser() {
        try {
            var existing = userRepository.findByUsername("demo");
            if (existing.isEmpty()) {
                User demo = new User();
                demo.setUsername("demo");
                demo.setEmail("demo@inventory.com");
                demo.setPasswordHash(passwordEncoder.encode("demo001"));
                demo.setFullName("Demo Guest");
                demo.setRole(Role.STAFF); // stored as STAFF in DB (constraint); identified as DEMO via username
                demo.setIsActive(true);
                userRepository.save(demo);
                log.info("\u2705 Demo user created. Login: demo / demo001");
            } else {
                User demo = existing.get();
                if (!passwordEncoder.matches("demo001", demo.getPasswordHash())) {
                    demo.setPasswordHash(passwordEncoder.encode("demo001"));
                    demo.setRole(Role.STAFF); // keep as STAFF in DB
                    userRepository.save(demo);
                    log.info("\u2705 Demo user password reset. Login: demo / demo001");
                } else {
                    log.info("\u2705 Demo user OK. Login: demo / demo001");
                }
            }
        } catch (Exception e) {
            log.error("\u274C Failed to initialize demo user: {}", e.getMessage());
        }
    }

    private void initAdminUser() {
        try {
            var existingAdmin = userRepository.findByUsername("admin");

            if (existingAdmin.isEmpty()) {
                // Create admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@inventory.com");
                admin.setPasswordHash(passwordEncoder.encode("123@Admin"));
                admin.setFullName("System Administrator");
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                userRepository.save(admin);
                log.info("✅ Admin user created successfully. Login: admin / 123@Admin");
            } else {
                // Admin exists - verify password works, reset if broken hash
                User admin = existingAdmin.get();
                // Accept both old and new password to handle migration
                boolean passwordValid = passwordEncoder.matches("123@Admin", admin.getPasswordHash())
                        || passwordEncoder.matches("Admin@123", admin.getPasswordHash());

                if (!passwordValid) {
                    admin.setPasswordHash(passwordEncoder.encode("123@Admin"));
                    userRepository.save(admin);
                    log.info("✅ Admin password reset successfully. Login: admin / 123@Admin");
                } else if (!passwordEncoder.matches("123@Admin", admin.getPasswordHash())) {
                    // Still on old password — migrate to new one
                    admin.setPasswordHash(passwordEncoder.encode("123@Admin"));
                    userRepository.save(admin);
                    log.info("✅ Admin password migrated to new credential. Login: admin / 123@Admin");
                } else {
                    log.info("✅ Admin user OK. Login: admin / 123@Admin");
                }
            }
        } catch (Exception e) {
            log.error("❌ Failed to initialize admin user: {}", e.getMessage());
        }
    }
}
