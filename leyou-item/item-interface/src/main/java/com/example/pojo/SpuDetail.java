package com.example.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "tb_spu_detail")
public class SpuDetail {

    @Id
    @NotNull(message = "com.example.entity.SpuDatail: spuId不能为空")
    private Long spuId;

    private String description;

    private String genericSpec;
    private String specialSpec;

    private String packingList;
    private String afterService;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {

        this.afterService = afterService;
    }

    @Override
    public String toString() {
        return "SpuDetail{" +
                "spuId=" + spuId +
                ", description='" + description + '\'' +
                ", genericSpec='" + genericSpec + '\'' +
                ", SpecialSpec='" + specialSpec + '\'' +
                ", packingList='" + packingList + '\'' +
                ", afterService='" + afterService + '\'' +
                '}';
    }
}
