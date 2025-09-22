package org.gayan.dls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 9:20 PM */
@EnableTransactionManagement
@SpringBootApplication
@Slf4j
public class DigitalLibraryServiceRunner {
  public static void main(String[] args) {
    SpringApplication.run(DigitalLibraryServiceRunner.class, args);
  }
}
