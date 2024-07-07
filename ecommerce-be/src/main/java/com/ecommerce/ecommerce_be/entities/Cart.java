package com.ecommerce.ecommerce_be.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "cart", schema = "ecommerce")
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private Float price;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "personid", nullable = false)
    private Users person;

    public Cart(){}

}
