package ru.gxfin.core.base.db.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig extends CommonConfig {
}
