package com.nidyran.rolebasedspringsecurity.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanierItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="mealID")
    private Meal meal;

    private String NameMeal;



    @Lob
    private String image;
    private int qty;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panierId", nullable = false)
    private Panier panier;


}
