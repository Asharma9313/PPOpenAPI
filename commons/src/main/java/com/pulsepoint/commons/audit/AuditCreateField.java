package com.pulsepoint.commons.audit;


public @interface AuditCreateField {

    /**
     * Label of the attribute in the creation details pop-up
     */
    String label();

    /**
     * Value the attribute in the creation details pop-up, this will be ignored if valueConstant is declared.
     */
    String valueRef() default "";

    /**
     * A Static value to be displayed in the creation details pop-up, overrides valueRef attribute.
     */
    String valueConstant() default "";
}
