package com.ecommerce.entity;

import com.ecommerce.dto.FAQDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    public FAQDto getFAQDto() {
        FAQDto faqDto = new FAQDto();
        faqDto.setId(id);
        faqDto.setQuestion(question);
        faqDto.setAnswer(question);
        faqDto.setProductId(product.getId());
        faqDto.setProductName(product.getName());
        return faqDto;
    }


}
