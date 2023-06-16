package br.com.java.thread.async.helper.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SyncRequest {

    @Autowired
    private RestTemplate restTemplate;

    public void makeSyncRequest(String url, int objIndex) {
        restTemplate.getForObject(url,String.class);
        //log.info("Sync Request {}",objIndex + 1);
    }
}
