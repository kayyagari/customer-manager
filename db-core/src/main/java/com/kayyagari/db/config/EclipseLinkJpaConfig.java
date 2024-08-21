package com.kayyagari.db.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

//@Configuration
//@EnableJpaRepositories({ "com.kayyagari" })
//@EntityScan({ "com.kayyagari" })
public class EclipseLinkJpaConfig extends JpaBaseConfiguration {

	public EclipseLinkJpaConfig(DataSource dataSource, JpaProperties properties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
		super(dataSource, properties, jtaTransactionManager);
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put(PersistenceUnitProperties.WEAVING, true);
		return props;
	}

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
		LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
		emfBean.setJpaVendorAdapter(createJpaVendorAdapter());
		emfBean.setDataSource(ds);

		return emfBean;
	}
}
