package com.mx.tsmo.cobertura.service;

import com.mx.tsmo.cobertura.model.dto.CoberturaPost;
import com.mx.tsmo.cobertura.model.dto.CoberturaResponse;

public interface CoberturaService {

    public CoberturaResponse[] coberturaENVIA(CoberturaPost cobertura);
    public CoberturaResponse coberturaTSMO(CoberturaPost cobertura);
    boolean coberturaCP(String cp);

}
