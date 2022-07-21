package com.pulsepoint.hcp365.enums;

public enum ReportStatus {
    Submitted(3000),
    Pending(3001),
    Running(3002),
    Success(3003),
    Fail(3004),
    Lost_Data(3005),
    Credential_Failure(3006),
    Delivery_Failure(3007);

    private final Integer intValue;

    ReportStatus(int intValue) {
        this.intValue = intValue;
    }

    public Integer toInt() {
        return intValue;
    }

    public static ReportStatus fromInt(Integer intValue) {
        if (intValue == 3000) {
            return Submitted;
        } else if (intValue == 3001) {
            return Pending;
        } else if (intValue == 3002) {
            return Running;
        } else if (intValue == 3003) {
            return Success;
        } else if (intValue == 3004) {
            return Fail;
        } else if (intValue == 3005) {
            return Lost_Data;
        } else if (intValue == 3006) {
            return Credential_Failure;
        } else if (intValue == 3007) {
            return Delivery_Failure;
        } else {
            throw new AssertionError("Unknown Status: " + intValue);
        }
    }
}
