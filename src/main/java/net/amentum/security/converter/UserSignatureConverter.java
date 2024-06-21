package net.amentum.security.converter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class UserSignatureConverter {
    public byte[] fromBase64ToByte (String signatureEncodedData) {
        return Base64.decodeBase64(signatureEncodedData);
    }
}
