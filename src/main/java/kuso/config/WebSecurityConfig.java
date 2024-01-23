package kuso.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import kuso.handler.OAuth2LogoutHandler;

@Configuration
//@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private OAuth2LogoutHandler oAuth2LogoutHandler;
	
	protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeRequests(a -> a
                .antMatchers("/","/oauth2Login","/checkAccount","/registerAccount","/normalLogin","/loginForGoogle",
        		"/loginForFB","/welcome","/goEmailVerify","/sendEmail","/verifyEmail","/goMain","/checkNeedChangePazz",
                "/changePazz","/getUserInfo","/checkRole","/goRoleSetting","/getRoleData","/saveRoleData","/error",
                "/testReturnMap","/testWithParam","/testError",
                "/webjars/**","https://connect.facebook.net/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            )
//        	.authorizeRequests(a -> a
//    			.anyRequest().permitAll()
//        	)
            .logout(l -> l
                .logoutSuccessUrl("/")
                .addLogoutHandler(oAuth2LogoutHandler)
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
//            .csrf(c -> c
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )
//            .csrf().disable()
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2Login();
    }
	
//	@Bean
//    public ClientRegistrationRepository clientRegistrationRepository(){
//		List<ClientRegistration> registrationList = new ArrayList<ClientRegistration>();
//		registrationList.add(this.gitHubClientRegistration());
//		registrationList.add(this.googleClientRegistration());
//		registrationList.add(this.lineClientRegistration());
//		
//        return new InMemoryClientRegistrationRepository(registrationList);
//    }
	
	
//	private ClientRegistration gitHubClientRegistration(){
//        return ClientRegistration.withRegistrationId("github")
//                .clientId("08c947436d444bbe7e52")
//                .clientSecret("cca0d90d346dc3f1238e5ae7105078b4e55a4e8f")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("http://localhost:8080")
//                
//                .scope("profile")
//                
//                .authorizationUri("https://github.com/login/oauth2/v2.1/authorize")
//                .tokenUri("https://github.com/login/oauth2/v2.1/token")
//                
//                .userNameAttributeName("userId")
//                .userInfoUri("https://github.com/login/oauth2/v2.1/profile")
//                .clientName("GITHUB")
//                
//                .build();
//    }
//	
//	private ClientRegistration googleClientRegistration(){
//        return ClientRegistration.withRegistrationId("google")
//                .clientId("629648582155-sgmdnobqquum1q887osrcrgponeale9g.apps.googleusercontent.com")
//                .clientSecret("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")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("http://localhost:8080")
//                
//                .scope("profile")
//                
//                //.authorizationUri("https://google.com/login/oauth2/v2.1/authorize")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                //.tokenUri("https://google.com/login/oauth2/v2.1/token")
//                .tokenUri("https://www.googleapis.com/oauth2/v3/token")
//                
//                .userNameAttributeName("userId")
//                .userInfoUri("https://www.googleapis.com/oauth2/v1/userinfo")
//                .clientName("GOOGLE")
//                
//                .build();
//    }
//	
//	private ClientRegistration lineClientRegistration(){
//        return ClientRegistration.withRegistrationId("line")
//                .clientId("1661401869")
//                .clientSecret("7ee158a798b4c888c3300890dd81a62f")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("http://localhost:8080")
//                .scope("profile")
//                .authorizationUri("https://access.line.me/oauth2/v2.1/authorize")
//                .tokenUri("https://api.line.me/oauth2/v2.1/token")
//                .userNameAttributeName("userId")
//                .userInfoUri("https://api.line.me/v2/profile")
//                .clientName("LINE")
//                .build();
//    }
}
