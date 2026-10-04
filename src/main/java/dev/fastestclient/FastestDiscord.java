package dev.fastestclient;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Discord Rich Presence lewat IPC (Windows named pipe), tanpa library tambahan.
 * Tidak jalan kalau discord.client_id kosong atau bukan Windows.
 */
public final class FastestDiscord {
    private static boolean started;

    private FastestDiscord() {
    }

    public static synchronized void start() {
        if (started) {
            return;
        }
        if (!FastestConfig.getBoolean("discord.enabled")) {
            return;
        }
        final String clientId = FastestConfig.get("discord.client_id").trim();
        if (clientId.isEmpty()) {
            return;
        }
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) {
            return;
        }
        started = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runLoop(clientId);
            }
        }, "FastestCore-Discord");
        thread.setDaemon(true);
        thread.start();
    }

    private static void runLoop(String clientId) {
        RandomAccessFile pipe = null;
        for (int i = 0; i < 10 && pipe == null; i++) {
            try {
                pipe = new RandomAccessFile("\\\\.\\pipe\\discord-ipc-" + i, "rw");
            } catch (IOException ignored) {
                // coba pipe berikutnya
            }
        }
        if (pipe == null) {
            return;
        }

        try {
            write(pipe, 0, "{\"v\":1,\"client_id\":\"" + esc(clientId) + "\"}");
            read(pipe);
            write(pipe, 1, activityJson());
            read(pipe);

            while (true) {
                Object[] frame = read(pipe);
                int op = ((Integer) frame[0]).intValue();
                if (op == 3) {
                    write(pipe, 4, (String) frame[1]);
                } else if (op == 2) {
                    break;
                }
            }
        } catch (IOException ignored) {
            // Discord ditutup
        } finally {
            try {
                pipe.close();
            } catch (IOException ignored) {
                // abaikan
            }
        }
    }

    private static String activityJson() {
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        long start = System.currentTimeMillis() / 1000L;

        StringBuilder activity = new StringBuilder();
        activity.append("{\"details\":\"").append(esc(FastestConfig.get("discord.details"))).append("\"");
        activity.append(",\"state\":\"").append(esc(FastestConfig.get("discord.state"))).append("\"");
        activity.append(",\"timestamps\":{\"start\":").append(start).append("}");
        String image = FastestConfig.get("discord.large_image").trim();
        if (!image.isEmpty()) {
            activity.append(",\"assets\":{\"large_image\":\"").append(esc(image))
                    .append("\",\"large_text\":\"Fastest Client\"}");
        }
        activity.append("}");

        return "{\"cmd\":\"SET_ACTIVITY\",\"args\":{\"pid\":" + pid + ",\"activity\":" + activity
                + "},\"nonce\":\"" + UUID.randomUUID() + "\"}";
    }

    private static void write(RandomAccessFile pipe, int op, String json) throws IOException {
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buf = ByteBuffer.allocate(8 + data.length).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(op);
        buf.putInt(data.length);
        buf.put(data);
        pipe.write(buf.array());
    }

    private static Object[] read(RandomAccessFile pipe) throws IOException {
        byte[] header = new byte[8];
        pipe.readFully(header);
        ByteBuffer hb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        int op = hb.getInt();
        int len = hb.getInt();
        if (len < 0 || len > 1024 * 1024) {
            throw new IOException("Bad frame length");
        }
        byte[] payload = new byte[len];
        pipe.readFully(payload);
        return new Object[]{Integer.valueOf(op), new String(payload, StandardCharsets.UTF_8)};
    }

    private static String esc(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}
