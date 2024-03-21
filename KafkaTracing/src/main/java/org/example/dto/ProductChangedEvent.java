package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//TODO: (1412) добавить описание
public class ProductChangedEvent {

    private UUID productId;

    private String productCode;

    private String productName;

    private String vgl;

    private String vglFullName;
}
