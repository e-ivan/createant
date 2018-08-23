package com.qnyy.re.business.vo;

import com.qnyy.re.business.entity.ShakearoundDevice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 店铺
 * Created by E_Iva on 2018.6.6 0006.
 */
@Getter@Setter@NoArgsConstructor
public class StoreVO {
    public StoreVO(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    private Long storeId;
    private String storeName;
    private Double latitude;
    private Double longitude;
    private Integer deliveFee;
    private Integer deliveTotalFee;
    private String storeLogo;
    private Integer storeScore;
    private Integer storeSaleCount;
    private List<ShakearoundDevice> devices;
}
