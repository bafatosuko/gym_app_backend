package gr.myproject.wod_core_backend.admin;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import gr.myproject.wod_core_backend.repository.UserRepository;
import gr.myproject.wod_core_backend.repository.WorkoutSessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AdminUserInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                WorkoutSessionRepository workoutSessionRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@admin.gr";

            User admin = userRepository.findByUsername(adminEmail).orElse(null);

            if (admin == null) {
                admin = new User();
                admin.setUsername(adminEmail);
                admin.setFirstname("Admin");
                admin.setLastname("User");
                admin.setRole(Role.ADMIN);
                admin.setPassword(passwordEncoder.encode("!Test123"));
                admin.setIsActive(true);

                userRepository.save(admin);
                System.out.println("Admin user created: " + adminEmail);
            } else {
                System.out.println("Admin user already exists: " + adminEmail);
            }


            // --- Trainer User ---
            String trainerEmail = "trainer@trainer.gr";
            User trainer = userRepository.findByUsername(trainerEmail).orElse(null);
            if (trainer == null) {
                trainer = new User();
                trainer.setUsername(trainerEmail);
                trainer.setFirstname("Trainer");
                trainer.setLastname("User");
                trainer.setRole(Role.TRAINER);
                trainer.setPassword(passwordEncoder.encode("!Test123"));
                trainer.setIsActive(true);
                userRepository.save(trainer);
                System.out.println("✅ Trainer user created: " + trainerEmail);
            }

            // --- Demo WorkoutSessions ---
            if (workoutSessionRepository.count() == 0) {
                LocalDate today = LocalDate.now();
                List<WorkoutSession> demoSessions = new ArrayList<>();

                for (int i = 0; i < 7; i++) { // 7 μέρες μπροστά
                    LocalDate sessionDate = today.plusDays(i);

                    demoSessions.add(new WorkoutSession("WOD", "Πρωινή προπόνηση WOD",
                            LocalTime.of(8, 0), 15, sessionDate, trainer));

                    demoSessions.add(new WorkoutSession("INTRO", "Μάθημα για νέους",
                            LocalTime.of(9, 0), 15, sessionDate, trainer));

                    demoSessions.add(new WorkoutSession("STRENGTH", "Προπόνηση δύναμης",
                            LocalTime.of(18, 0), 15, sessionDate, trainer));
                }

                workoutSessionRepository.saveAll(demoSessions);
                System.out.println("✅ Demo workout sessions created for 7 days");
            } else {
                System.out.println("ℹ️ Workout sessions already exist, skipping demo seeding");
            }

        };
    }
}

