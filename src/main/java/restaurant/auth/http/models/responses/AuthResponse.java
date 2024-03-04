package restaurant.auth.http.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    public AuthResponse(String email, boolean isAuthenticated) {
        this.email = email;
        this.isAuthenticated = isAuthenticated;
    }

    private String email;
    private boolean isAuthenticated;
    private boolean isAdmin = false;
}
