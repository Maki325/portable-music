package me.maki325.mcmods.portablemusic.client.sound;

import net.minecraft.client.sounds.AudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MP3AudioStream implements AudioStream {

    private InputStream inputstream;

    public MP3AudioStream(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    @Override public AudioFormat getFormat() {
        return null;
    }

    @Override public ByteBuffer read(int p_120120_) throws IOException {
        return null;
    }

    /*public ByteBuffer read(int p_120167_) throws IOException {
        ByteBuffer bytebuffer = this.inputstream.read(p_120167_);
        if (!bytebuffer.hasRemaining()) {
            this.stream.close();
            this.bufferedInputStream.reset();
            this.stream = this.provider.create(new LoopingAudioStream.NoCloseBuffer(this.bufferedInputStream));
            bytebuffer = this.stream.read(p_120167_);
        }

        return bytebuffer;
    }*/

    @Override public void close() throws IOException {
        this.inputstream.close();
    }
}
