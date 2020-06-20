package hu.elte.bsc.thesis.communcation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStoreRq {
    private String storeName;
    private String address;
    private Long companyId;
}
