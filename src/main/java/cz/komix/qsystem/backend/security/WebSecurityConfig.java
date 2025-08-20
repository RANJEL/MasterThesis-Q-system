package cz.komix.qsystem.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

/**
 * Configuration of Spring Security, which pages can be accessed with which use role, ldap configuration
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.server.url}")
    private String ldapServerUrl;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.user.search.base}")
    private String ldapUserSearchBase;

    @Value("${ldap.user.search.filter}")
    private String ldapUserSearchFilter;

    @Value("${ldap.user.password.key}")
    private String ldapUserPasswordKey;

    @Value("${ldap.group.search.base}")
    private String ldapGroupSearchBase;

    @Value("${ldap.group.search.filter}")
    private String ldapGroupSearchFilter;

    @Value("${ldap.group.role.attribute}")
    private String ldapGroupRoleAttribute;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/components/content/admin/**").hasRole("ADMIN")
                .antMatchers("/javax.faces.resource/**").permitAll()
                .anyRequest().authenticated();
        // login
        http.formLogin().loginPage("/login.xhtml").permitAll()
                .defaultSuccessUrl("/app.xhtml", true)
                .failureUrl("/login.xhtml?error=true");
        // logout
        http.logout().logoutSuccessUrl("/login.xhtml");
        // not needed as JSF 2.2 is implicitly protected against CSRF
        http.csrf().disable();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        String ldapUrl = ldapServerUrl;
        if (ldapUrl.lastIndexOf('/') != ldapUrl.length() - 1) {
            ldapUrl += '/';
        }
        ldapUrl += ldapBaseDn;

        auth
                .ldapAuthentication()
                .userSearchBase(ldapUserSearchBase)
                .userSearchFilter(ldapUserSearchFilter)
                .groupSearchBase(ldapGroupSearchBase)
                .groupSearchFilter(ldapGroupSearchFilter)
                .groupRoleAttribute(ldapGroupRoleAttribute)
                .contextSource()
                .url(ldapUrl)
                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute(ldapUserPasswordKey);
    }
}
