package com.lfkj.dt.test;

import javafx.scene.media.AudioClip;
import org.junit.Test;

import java.net.URISyntaxException;

public class AudioTest {
    public AudioTest() {
    }

    @Test
    public void testWebAudio() throws InterruptedException {
        new AudioClip("http://res.iciba.com/resource/amp3/oxford/0/71/37/71377ee16a9e050aaf16a61a8f5229e4.mp3").play();
        Thread.sleep(5000);
    }

    @Test
    public void testLocalAudio() throws InterruptedException, URISyntaxException {
        new AudioClip(getClass().getResource("/com/lfkj/dt/test/testSound.mp3").toURI().toString()).play();
        Thread.sleep(5000);
    }
}