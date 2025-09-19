package org.gayan.dls;


import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 9:20 PM
 */
@SpringBootApplication
@Slf4j
public class DigitalLibraryServiceRunner implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(DigitalLibraryServiceRunner.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("test");
    }
}