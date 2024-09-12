package deu.ex.sevenstars.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info (
                title = "SEVEN STARS",
                version = "ver 1.0",
                description = "SEVEN STARS RESTful API Documentation (7팀 API 문서)"
        ),
        servers = { @Server(
                description = "Prod ENV",
                url = "http://localhost:8080/"
        )
        }
)
public class SwaggerOpenAPIConfig {
}
