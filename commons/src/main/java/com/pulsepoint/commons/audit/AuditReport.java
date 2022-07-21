package com.pulsepoint.commons.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface AuditReport {
    /**
     * Categories that this entity affects in the Audit Report
     */
    Category[] categories() default {};

    /**
     * The feature label to show in the Audit Report details
     */
    String featureName() default "Unassigned";

    /**
     * Use only if you want to show a popup dialog to render the details of the entity(only on ADD).
     * To render the attributes of this entity as updates instead of create, use showCreateAsUpdate = "true"
     */
    AuditCreateField[] createData() default {};

    /**
     * If set to true, processes the created attributes as updated
     */
    boolean showCreateAsUpdate() default false;

    /**
     * If set to true, processes the updated attributes as created
     * If set showAsCreate and showCreateAsUpdate, showCreateAsUpdate will be given priority
     */
    boolean showAsCreate() default false;

    /**
     * Use in cases where we need to distinguish between multiple instances on the report
     * The value of the IdentifierProperty will be rendered as suffix to the feature name.
     */
    String identifierProperty() default "";

     enum Category {

       REPORTTEMPLATE

    }
     enum Format {
        Default,
        ActiveInactive,
        AddedDeleted,
        EnableDisable,
        OnOff,
        YesNo

    }
}