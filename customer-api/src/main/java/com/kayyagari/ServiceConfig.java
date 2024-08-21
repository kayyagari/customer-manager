package com.kayyagari;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// this is not working in IDE without the agent, so leaving it at this
@Configuration
//@EnableLoadTimeWeaving(aspectjWeaving = AspectJWeaving.ENABLED)
@EnableAspectJAutoProxy
@EnableJpaRepositories({ "com.kayyagari" })
@EntityScan({ "com.kayyagari" })
@ComponentScan({ "com.kayyagari" })
public class ServiceConfig {//implements LoadTimeWeavingConfigurer {
//	@Override
//	public LoadTimeWeaver getLoadTimeWeaver() {
//		return new ReflectiveLoadTimeWeaver(getClass().getClassLoader());
//	}
}
