package blaybus.happynewyear.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedMethods("GET","POST","DELETE","OPTIONS","PUT")
                .allowedHeaders("*")
                .allowedOriginPatterns("*");
        //.allowCredentials(true);
    }
}
