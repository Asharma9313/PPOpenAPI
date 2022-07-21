package com.pulsepoint.commons.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on attribute level
public @interface AuditReportColumn {
    /**
     * Label to show in the report row for attribute changes
     */
    String reportLabel();

    /**
     * Label prefix reference attribute to render in the report row for attribute changes
     */
    String labelPrefixField() default "";

    /**
     * Label suffix reference attribute to render in the report row for attribute changes
     */
    String labelSuffixField() default "";

    /**
     * To map indirect values to show in the report row for attribute changes
     */
    String[] valueReferenceAttributes() default {};

    /**
     * Constant value to show in the report row for attribute changes, overrides actual values and <b>valueReferenceAttributes</b>
     */
    String valueConstant() default "";

    /**
     * To exclude an entity field in the specified categories. by default, field will be included in all the category reports.
     */
    AuditReport.Category[] excludeIn() default {};

    /**
     * To specify a format for the final result. See {@link AuditReport.Format} for supported formats.
     */
    AuditReport.Format format() default AuditReport.Format.Default;
}