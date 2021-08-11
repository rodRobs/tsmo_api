package com.mx.tsmo.enums;

public enum EnviaAuth {

    // Test
    USER("TSANMIGUEL"),
    PASS("YyyE8e1#%e"),
    CLIENTE("1000193"),
    URL("http://qaclientes.envia.tpak.mx/WS/api/"),
    // Produccion
    URL_PROD("http://189.206.139.226/WS/api/"),
    // Carga Normal
    USER_CARGA_NORMAL("TSMO-PAQ"),
    PASS_CARGA_NORMAL("$5q5QqQqq$"),
    CLIENTE_CARGA_NORMAL("1000249"),
    // Carga Pesada
    USER_CARGA_PESADA("TSMO-CARGA"),
    PASS_CARGA_PESADA("K$0ckkCc#"),
    CLIENTE_CARGA_PESADA("1000250"),
    // LTL
    USER_LTL("TSMO-LTL"),
    PASS_LTL("h%hqHQ2#6q"),
    CLIENTE_LTL("1000251")
    ;


    private String data;
    private EnviaAuth(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }

}
