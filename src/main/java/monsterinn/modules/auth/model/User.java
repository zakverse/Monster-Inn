package monsterinn.modules.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Biar otomatis ada Getter/Setter (kalau pakai Lombok)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private String role; // Contoh: "STAFF"
}