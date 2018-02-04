package com.lyft.java.v1;

import com.lyft.java.v1.Quote;
import com.lyft.java.v1.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Controller
public class ConsumeController {
    Quote quot1 = new Quote();

    @RequestMapping("/consume")
    public String consume(Model model) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        quot1 = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", quot1.class);
        model.addAttribute("quote", quot1);
        return "consume";
    }
}
