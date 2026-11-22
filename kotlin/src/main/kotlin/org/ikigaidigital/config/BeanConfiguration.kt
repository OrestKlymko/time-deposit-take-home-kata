package org.ikigaidigital.config

import org.ikigaidigital.TimeDepositCalculator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The shared domain classes stay framework-free (no Spring annotations on them),
 * so they are wired into the context here.
 */
@Configuration
class BeanConfiguration {

    @Bean
    fun timeDepositCalculator() = TimeDepositCalculator()
}
