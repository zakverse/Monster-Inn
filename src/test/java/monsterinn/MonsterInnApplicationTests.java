package monsterinn;

import monsterinn.model.User;
import monsterinn.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
class MonsterInnApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void testUserAuth() {
		Optional<User> adminOpt = userRepository.findByUsername("admin");
		if (adminOpt.isPresent()) {
			User admin = adminOpt.get();
			System.out.println("=== USER ADMIN FOUND ===");
			System.out.println("Username: " + admin.getUsername());
			System.out.println("Password Hash: " + admin.getPassword());
			System.out.println("Role: " + admin.getRole());
			boolean match = passwordEncoder.matches("admin123", admin.getPassword());
			System.out.println("Password match admin123? " + match);
		} else {
			System.out.println("=== USER ADMIN NOT FOUND IN DB ===");
			User newAdmin = new User("admin", passwordEncoder.encode("admin123"), "ADMIN");
			userRepository.save(newAdmin);
			System.out.println("=== SAVED NEW ADMIN PROGRAMMATICALLY ===");
		}
	}
}
