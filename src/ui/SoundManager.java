package ui;

import javax.sound.sampled.*;

/**
 * Generates and plays audio effects programmatically to avoid requiring sound file assets.
 * All sounds are played in background threads to avoid blocking the main UI thread.
 */
public class SoundManager {
    private static final int SAMPLE_RATE = 16000;

    /**
     * Plays a pleasant wood-block sound for standard moves.
     */
    public static void playMove() {
        playTone(320, 180, 50, 0.2f);
    }

    /**
     * Plays a textured noise-clack sound for captures.
     */
    public static void playCapture() {
        playNoise(65, 0.18f);
    }

    /**
     * Plays a dual chime notification for check.
     */
    public static void playCheck() {
        new Thread(() -> {
            playToneImmediate(520, 520, 85, 0.15f);
            try { Thread.sleep(90); } catch (InterruptedException e) {}
            playToneImmediate(660, 660, 130, 0.15f);
        }).start();
    }

    /**
     * Plays a descending arpeggio for checkmate/game over.
     */
    public static void playGameOver() {
        new Thread(() -> {
            int[] notes = {523, 440, 349, 262}; // Descending arpeggio
            for (int note : notes) {
                playToneImmediate(note, note - 40, 160, 0.12f);
                try { Thread.sleep(180); } catch (InterruptedException e) {}
            }
        }).start();
    }

    private static void playTone(int startFreq, int endFreq, int durationMs, float volume) {
        new Thread(() -> playToneImmediate(startFreq, endFreq, durationMs, volume)).start();
    }

    private static void playToneImmediate(int startFreq, int endFreq, int durationMs, float volume) {
        try {
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buf = new byte[numSamples];
            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double fraction = (double) i / numSamples;
                double freq = startFreq + (endFreq - startFreq) * fraction;
                double angle = 2.0 * Math.PI * freq * t;
                double envelope = 1.0 - fraction; // Linear decay
                
                // Attack envelope: quick fade-in
                if (i < 120) {
                    envelope *= (double) i / 120.0;
                }
                buf[i] = (byte) (Math.sin(angle) * 127.0 * volume * envelope);
            }
            playBuffer(buf);
        } catch (Exception e) {
            // Silently catch audio resource errors
        }
    }

    private static void playNoise(int durationMs, float volume) {
        new Thread(() -> {
            try {
                int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
                byte[] buf = new byte[numSamples];
                java.util.Random rand = new java.util.Random();
                for (int i = 0; i < numSamples; i++) {
                    double fraction = (double) i / numSamples;
                    double envelope = 1.0 - fraction;
                    
                    // Mix noise with a short low frequency thud for punch
                    double pitchT = 2.0 * Math.PI * 150 * ((double) i / SAMPLE_RATE);
                    double synth = Math.sin(pitchT) * 0.3;
                    double noise = rand.nextFloat() * 2.0 - 1.0;
                    
                    buf[i] = (byte) ((noise * 0.7 + synth) * 127.0 * volume * envelope);
                }
                playBuffer(buf);
            } catch (Exception e) {
                // Silently catch audio resource errors
            }
        }).start();
    }

    private static void playBuffer(byte[] buf) throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format, buf.length);
        line.start();
        line.write(buf, 0, buf.length);
        line.drain();
        line.close();
    }
}
