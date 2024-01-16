package ua.foxmided.foxstudent103852.schoolappspring.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CommonConfig {
    private final BufferedReader commonReader;

    public CommonConfig() {
        commonReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Lazy
    @Bean
    public BufferedReader bufferedReader() {
        return commonReader;
    }
}
