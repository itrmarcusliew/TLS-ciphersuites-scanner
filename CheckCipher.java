import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStore;

public class CheckCipher {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error, enter host and port number: java CheckCipher <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int timeout = 2000;
        String keystore = args[2];
        String password = args[3];

        String keystorePath = ".\\" + keystore;
        String keystorePass = password;

        System.out.printf("Scanning localhost:%d using TLS context...%n%n", port);

        try {
            SSLContext sslContext = createSSLContext(keystorePath, keystorePass);
            SSLSocketFactory factory = sslContext.getSocketFactory();
            String[] allCiphers = factory.getSupportedCipherSuites();

            for (String cipher : allCiphers) {
                // Filter out non-SSL/TLS ciphers if necessary
                //if (cipher.contains("_WITH_")) 
				{
                    try (SSLSocket socket = (SSLSocket) factory.createSocket()) {
                        socket.connect(new InetSocketAddress(host, port), timeout);
                        socket.setEnabledCipherSuites(new String[]{cipher});
                        
                        // Attempt actual handshake
                        socket.startHandshake();
                        
                        String protocol = socket.getSession().getProtocol();
                        System.out.printf("[SUPPORTED] %-50s (%s)%n", cipher, protocol);
                    } catch (IOException e) {
                        // Connection refused or handshake failed (cipher not supported)
						System.out.printf("[CONNECTION REFUSED] %-50s %n", cipher);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing SSL Context: " + e.getMessage());
        }
        
        System.out.println("\nScan Complete.");
    }

    private static SSLContext createSSLContext(String path, String pass) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(path), pass.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, pass.toCharArray());

        // We trust all for the scan to ensure we see what the server allows
        TrustManager[] trustAll = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String auth) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String auth) {}
            }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), trustAll, new java.security.SecureRandom());
        return sc;
    }
}