package com.lfkj.dt.test;

import org.junit.Test;

import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;

public class AudioTest {
    public AudioTest() {
    }

    @Test
    public void testAudio() throws MalformedURLException, InterruptedException {
        Applet.newAudioClip(new URL("http://res.iciba.com/resource/amp3/oxford/0/71/37/71377ee16a9e050aaf16a61a8f5229e4.mp3"))
                .loop();
        Thread.sleep(1000);
    }
}