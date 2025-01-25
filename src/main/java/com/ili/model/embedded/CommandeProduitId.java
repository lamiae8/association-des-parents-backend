package com.ili.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class CommandeProduitId  implements Serializable {

    private Long commandeId;
    private Long produitId;

    public CommandeProduitId() {
    }

    public CommandeProduitId(Long commandeId, Long produitId) {
        this.commandeId = commandeId;
        this.produitId = produitId;
    }


}
