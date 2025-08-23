package com.gundam;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple EmailSample.
 */
public class EmailSampleTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    void printLatestEmailViaPOP() {
        // This will print the latest email using POP3
        EmailSample.main(new String[]{"POP"});
    }
}
