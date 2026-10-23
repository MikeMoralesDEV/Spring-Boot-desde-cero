package com.example.p6springboot;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Configura cómo se determina y se cambia el idioma de cada petición web.
 */
@Configuration
public class ConfiguracionIdioma implements WebMvcConfigurer {

	/**
	 * Guarda la elección de idioma en la sesión HTTP, no en una base de datos.
	 *
	 * Al no existir todavía una elección para la sesión, se utiliza español.
	 * La sesión es temporal: no es persistencia de datos de negocio.
	 *
	 * @return resolvedor de idioma usado por Spring MVC y Thymeleaf
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolvedor = new SessionLocaleResolver();
		resolvedor.setDefaultLocale(Locale.forLanguageTag("es"));
		return resolvedor;
	}

	/**
	 * Permite cambiar el idioma enviando un parámetro de consulta llamado "lang".
	 *
	 * Por ejemplo, /?lang=en solicita inglés y /?lang=es solicita español.
	 *
	 * @return interceptor que lee el parámetro lang y actualiza la sesión
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	/**
	 * Registra el interceptor para que Spring procese lang antes del controlador.
	 *
	 * @param registry registro de interceptores de Spring MVC
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
