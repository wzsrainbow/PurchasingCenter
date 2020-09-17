package org.jeecg.modules.contract.elements.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.contract.elements.entity.ContractElements;

/**
 * @Description: 合同元素表
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface ContractElementsMapper extends BaseMapper<ContractElements> {

    //查询合同元素数据
    public List<ContractElements> elementlist(@Param("htbh")String htbh);
}