package org.jeecg.modules.contract.contract.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.contract.entity.ContractInformation;
import org.jeecg.modules.contract.contract.mapper.ContractInformationMapper;
import org.jeecg.modules.contract.contract.service.IContractInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description: 合同信息表
 * @Author: jeecg-boot
 * @Date:   2020-08-28
 * @Version: V1.0
 */
@Service
public class ContractInformationServiceImpl extends ServiceImpl<ContractInformationMapper, ContractInformation> implements IContractInformationService {

    @Autowired
    ContractInformationMapper ch;

    public  Integer findpzh(){
        return ch.findpzh();
    }
}
