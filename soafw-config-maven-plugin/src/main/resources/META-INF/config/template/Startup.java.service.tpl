package com.kjt.service.#{artifactId};

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Startup {
    public void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        new String[] {"classpath*:/META-INF/config/spring/spring-service.xml"});
        context.start();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
