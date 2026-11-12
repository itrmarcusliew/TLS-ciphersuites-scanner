CheckCipher - TLS Cipher Suite Scanner
--------------------------------------
A Java-based tool for scanning which TLS cipher suites a target host:port supports. 

It creates a custom SSL context from a provided dummy keystore/truststore and tests each supported cipher by attempting a TLS handshake.

Features
--------
TLS 1.2 and 1.3 support 

— Tests all KeyManagerFactory-configured ciphers against the target Custom SSL context 

— Bypasses server certificate validation to focus purely on cipher support

Categorized output — Marks each cipher as 

[SUPPORTED] (handshake possible) or 

[CONNECTION REFUSED] (handshake not possible)

timeout — 2-second default connect/handshake timeout


Command
-------
java CheckCipher "host" "port" "keystore-file" "keystore-password"


Parameter	Description
---------------------
host=Target hostname or IP address to connect to

port=Target port number (e.g., 443 for HTTPS)

keystore-file=Path to the keystore file (JKS or PKCS12 format)

keystore-password=Password to unlock the keystore


Example
-------
java CheckCipher localhost 443 clientPROD.jks ng9oy8JZabcSiZU


Output Format
-------------
Each cipher suite is reported as one of:

Status	Meaning
[SUPPORTED]	TLS handshake is possible with this cipher (protocol also shown)

[CONNECTION REFUSED]	TLS handshake not supported — cipher not supported or SSL error

Sample Output

Scanning localhost:443 using TLS context...

[SUPPORTED] TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384              (TLSv1.2)

[CONNECTION REFUSED] TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384

[SUPPORTED] TLS_RSA_WITH_AES_256_GCM_SHA384                  (TLSv1.2)  

[CONNECTION REFUSED] TLS_CHACHA20_POLY1305_SHA256             (TLSv1.3)

Scan Complete.






