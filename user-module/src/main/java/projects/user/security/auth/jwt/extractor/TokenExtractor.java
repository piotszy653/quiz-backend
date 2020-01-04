package projects.user.security.auth.jwt.extractor;

public interface TokenExtractor {
    String extract(String payload);
}
