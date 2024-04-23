package com.ecommerce.entity;

import com.ecommerce.dto.ReviewDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    public ReviewDto getReviewDto() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(id);
        reviewDto.setRating(rating);
        reviewDto.setDescription(description);
        reviewDto.setReturnedImg(img);
        reviewDto.setProductId(product.getId());
        reviewDto.setUserId(user.getId());
        reviewDto.setUsername(user.getName());
        return reviewDto;
    }

}
