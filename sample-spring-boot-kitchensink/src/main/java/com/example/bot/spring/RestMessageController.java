
package com.example.bot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class RestMessageController {

    @RequestMapping("/")
    public String index() {
    	return "hi";
    }

}
