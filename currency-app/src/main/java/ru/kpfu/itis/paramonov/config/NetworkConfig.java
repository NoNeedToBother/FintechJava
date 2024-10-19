package ru.kpfu.itis.paramonov.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@AllArgsConstructor
public class NetworkConfig {

    private CentralBankApiConfigurationProperties centralBankApiConfigurationProperties;

    @Bean("central_bank_api_client")
    public RestClient restClient() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        return RestClient.builder(restTemplate)
                .baseUrl(centralBankApiConfigurationProperties.getMainUri())
                .build();
    }
}
