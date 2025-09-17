package com.sys.stm.domains.board.domain;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
/** 작성자: 배지원 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Board extends BaseEntity {

    private Long id;
}