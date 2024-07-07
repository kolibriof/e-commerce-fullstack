package com.ecommerce.ecommerce_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "availableproducts", schema = "ecommerce")
@Getter
public class Product {

    @Id
    @Setter
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String imglink;

    @NotBlank
    private Float price;

    public Product(){}

    public Product(Integer id, String name, String description, String imglink, Float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imglink = imglink;
        this.price = price;
    }
}
