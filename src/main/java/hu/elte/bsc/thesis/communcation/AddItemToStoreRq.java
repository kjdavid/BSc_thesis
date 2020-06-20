package hu.elte.bsc.thesis.communcation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToStoreRq {
    private Long storeId;
    private Long companyItemId;
    private Double discount;
    private Integer sellingPrice;
    private Integer stock;
}
