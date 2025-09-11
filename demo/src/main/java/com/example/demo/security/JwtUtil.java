package com.example.demo.security;

public class JwtUtil {
        private String secretKey;
        private long expirationTime;

        public String generateToken(String username) {
            // Implement token generation logic
            return "generatedToken";
        }

        public boolean validateToken(String token) {
            // Implement token validation logic
            return true;
        }

        public String getUsernameFromToken(String token) {
            // Implement logic to extract username from token
            return "username";
        }
}
