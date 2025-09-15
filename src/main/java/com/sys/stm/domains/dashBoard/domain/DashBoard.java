package com.sys.stm.domains.dashBoard.domain;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DashBoard extends BaseEntity {

    private Long id;
}