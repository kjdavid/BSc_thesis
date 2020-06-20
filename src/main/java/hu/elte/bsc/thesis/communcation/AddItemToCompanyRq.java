package hu.elte.bsc.thesis.communcation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCompanyRq {
    private String barcode;
    private String itemName;
    private int purchasePrice;
    private String description;
    private String base64str;
}
