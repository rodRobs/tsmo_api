package com.mx.tsmo.envios.service;

import com.mx.tsmo.envios.model.dao.EnvioGranelDao;
import com.mx.tsmo.envios.model.domain.EnviosGranel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnviosGranelServiceImpl implements EnviosGranelService {

    @Autowired
    EnvioGranelDao envioGranelDao;

    @Override
    public EnviosGranel guardar(EnviosGranel enviosGranel) {
        return envioGranelDao.save(enviosGranel);
    }
}
