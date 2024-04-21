package nostr.id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.java.Log;
import nostr.base.PrivateKey;
import nostr.base.PublicKey;
import nostr.crypto.bech32.Bech32;
import nostr.crypto.bech32.Bech32Prefix;
import nostr.crypto.schnorr.Schnorr;
import nostr.util.AbstractBaseConfiguration;
import nostr.util.NostrException;

import java.io.IOException;
import java.util.logging.Level;

/**
 * @author squirrel
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Log
public class Identity extends AbstractBaseIdentity {

    private static final ThreadLocal<Identity> INSTANCE = new ThreadLocal<>();

    @ToString.Exclude
    private final PrivateKey privateKey;

    private Identity() throws IOException, NostrException {
        this.privateKey = new IdentityConfiguration("").getPrivateKey();
    }

    public Identity(@NonNull PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public static Identity getInstance() {
        if (INSTANCE.get() == null) {
            try {
                INSTANCE.set(new Identity());
            } catch (IOException | NostrException ex) {
                throw new RuntimeException(ex);
            }
        }

        return INSTANCE.get();
    }

    public static Identity getInstance(@NonNull PrivateKey privateKey) {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new Identity(privateKey));
        }

        return INSTANCE.get();
    }

    public static Identity getInstance(@NonNull String privateKey) {
        return getInstance(new PrivateKey(privateKey));
    }

    /**
	 * @return A strong pseudo random identity
	 */
    public static Identity generateRandomIdentity() {
        return new Identity(PrivateKey.generateRandomPrivKey());
    }

    @Log
    static class IdentityConfiguration extends AbstractBaseConfiguration {

        IdentityConfiguration(@NonNull String name) throws IOException {
            super(name, CONFIG_TYPE_IDENTITY);
        }

        PrivateKey getPrivateKey() throws NostrException {
            String privKey = getProperty("privateKey");

            if (privKey == null) {
                throw new RuntimeException("Missing private key. Aborting....");
            }
            String hex = privKey.startsWith(Bech32Prefix.NSEC.getCode()) ? Bech32.fromBech32(privKey) : privKey;
            return new PrivateKey(hex);
        }

        PublicKey getPublicKey() throws NostrException {
            String pubKey = getProperty("publicKey");
            if (pubKey == null || pubKey.trim().isEmpty()) {
                log.log(Level.FINE, "Generating new public key...");
                try {
                    var publicKey = Schnorr.genPubKey(getPrivateKey().getRawData());
                    return new PublicKey(publicKey);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, null, ex);
                    throw new NostrException(ex);
                }
            } else {
                String hex = pubKey.startsWith(Bech32Prefix.NPUB.getCode()) ? Bech32.fromBech32(pubKey) : pubKey;
                return new PublicKey(hex);
            }
        }
    }

}
