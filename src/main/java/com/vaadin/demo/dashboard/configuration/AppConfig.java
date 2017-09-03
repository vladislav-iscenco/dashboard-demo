package com.vaadin.demo.dashboard.configuration;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

/**
 * Created by Ronen on 9/3/2017.
 */
@Configuration
@EnableSideBar
@EnableI18N
public class AppConfig {

    @Bean
    public ApiContext getApiContext() {
        ApiContext apiContext = new ApiContext();
        ApiCredential cred = apiContext.getApiCredential();
        cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**PgisWQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ABk4uoDJSApwSdj6x9nY+seQ**2uADAA**AAMAAA**URvpbbX7WfPYYAn1jZvpJtwBfiOge9qUevowhGSt35mJdmQ4VmmAeF9ntuORAaoOdcX4wclIJO9EZ6k+A88jouiCQaX4eelWYtUF9GXTmNLm4mJP0HwtRLcR0pLnkdsvLLAW0Qw0adypgEWCF+mGot8Yb7FIsgE2J4hzdlwt2bVLf2ZnEz9lq0GIg/zIvh1Gx0GVM5bZzu/jnRuhYMgsKlAJZuQ0+wXyD8lV+0LFAhqwoeowd2UfAqNDi4WIWhpk/F3e6fKEY2ndTah27O46ZPAzHnusz881EcwCnkmjQu7RRd2ZFysHFuOKXWZpqRloVNa5qXExzWDO2wUb3QoDMnifx4pVOwSI3mHKD18TX8RkoLSlWB9EVHqNL08xKc9WuKek95khL9zpUXVRFTyX+jGNhtLqMQR3jaw5nCFVkZY4nvrIljDZfALSL/TIbwIezviSoY/PaXmiseW/e1XZhdic/sCaO106QIbjmTamMOfer4NY5l3oZ1DSpjd4a9tgwnaJ4zMzGc5ghQ1z+02WJJiWKkvzh70E1dGeQ5oLFQPZkAH1H3gDcHU4CpQOPrCfpuSZkF7EIc72FX36quM3NAoWM5whj7SEmPBm//aBwpSw9nxMc8NGy/xoHxV5GFMm2F3TNYTOFDVXwNaUaHoSoW/fAizS1T6TfaYSrfaZZAFR0d9EEUZHG9nHbDIWfTqM7UwH+Abvi5St2Sx6qHtMPfpUP2+zO62oeGmfnZBwz2kAEAuJbNcKMq0LkoNHftj5");
        String productionGatewayURL = "https://api.ebay.com/wsapi";
        String sandboxGatewayURL = "https://api.sandbox.ebay.com/wsapi";
        apiContext.setApiServerUrl(productionGatewayURL);
        apiContext.setSite(SiteCodeType.US);
        return apiContext;
    }
}
