package com.example.pojo;

import com.example.Interface.AddInterface;
import com.example.Interface.UpdateInterface;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "tb_spec_param")
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UpdateInterface.class)
    private Long id;


    @NotNull(groups = {AddInterface.class,UpdateInterface.class})
    private Long cid;
    @NotNull(groups = {AddInterface.class,UpdateInterface.class})
    private Long groupId;
    @NotBlank(groups = {AddInterface.class,UpdateInterface.class})
    private String name;

    @Column(name="`numeric`")
    private Boolean numeric;

    private String unit;

    @NotNull(groups = {AddInterface.class,UpdateInterface.class})
    private Boolean generic;

    private Boolean searching;

    private String segments;




    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }
}
