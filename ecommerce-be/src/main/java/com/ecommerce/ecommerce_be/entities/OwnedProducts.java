package com.ecommerce.ecommerce_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ownedproducts", schema = "ecommerce")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnedProducts {

    @Id
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @NotBlank
    private Users users;

    @ManyToOne
    @JoinColumn(name="productid", nullable = false)
    @NotBlank
    private Product product;
}
