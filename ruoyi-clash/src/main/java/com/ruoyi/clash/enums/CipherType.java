package com.ruoyi.clash.enums;

public enum CipherType {
    AES_256_GCM("aes-256-gcm");
    private String value;

    CipherType(String value) {
        this.value = value;
    }
}
