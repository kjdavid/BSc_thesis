package hu.elte.bsc.thesis.communcation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditStoreItemRq {
    private Long id;
    private Integer stock;
    private Integer sellingPrice;
    private Double discount;
}
