package com.vaadin.demo.dashboard.configuration;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

/**
 * Created by Ronen on 9/3/2017.
 */
@Configuration
@EnableSideBar
@EnableI18N
@PropertySource(value= {"classpath:application.properties"})
public class AppConfig {

    @Value("${eBay.token}")
    private String token;

    @Bean
    public ApiContext getApiContext() {
        ApiContext apiContext = new ApiContext();
        ApiCredential cred = apiContext.getApiCredential();
        cred.seteBayToken(token);
        String productionGatewayURL = "https://api.ebay.com/wsapi";
        String sandboxGatewayURL = "https://api.sandbox.ebay.com/wsapi";
        apiContext.setApiServerUrl(productionGatewayURL);
        apiContext.setSite(SiteCodeType.US);
        return apiContext;
    }
}
