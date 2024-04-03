package com.ecommerce.service.admin.faq;

import com.ecommerce.dto.FAQDto;

public interface FAQService {
    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
