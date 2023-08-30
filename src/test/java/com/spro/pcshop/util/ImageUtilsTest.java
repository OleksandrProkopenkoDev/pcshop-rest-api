package com.spro.pcshop.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {

    @Test
    void readFile_shouldReturnByteArray(){
        byte[] bytes = ImageUtils.readFile("src/main/resources/images/monitors/hp_9FM22AA/175135466.jpg");
        assertThat(bytes).isNotEmpty();
    }
}