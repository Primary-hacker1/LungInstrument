package com.just.machine.dao;

import java.lang.System;

@androidx.room.Entity(tableName = "patients")
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\bR\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u00eb\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u001cJ\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0005H\u00c6\u0003J\t\u0010R\u001a\u00020\u0005H\u00c6\u0003J\t\u0010S\u001a\u00020\u0005H\u00c6\u0003J\t\u0010T\u001a\u00020\u0005H\u00c6\u0003J\t\u0010U\u001a\u00020\u0005H\u00c6\u0003J\t\u0010V\u001a\u00020\u0005H\u00c6\u0003J\t\u0010W\u001a\u00020\u0015H\u00c6\u0003J\t\u0010X\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0005H\u00c6\u0003J\t\u0010[\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0005H\u00c6\u0003J\t\u0010]\u001a\u00020\u0005H\u00c6\u0003J\t\u0010^\u001a\u00020\u0015H\u00c6\u0003J\t\u0010_\u001a\u00020\u0005H\u00c6\u0003J\t\u0010`\u001a\u00020\u0005H\u00c6\u0003J\t\u0010a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010c\u001a\u00020\u0005H\u00c6\u0003J\t\u0010d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010e\u001a\u00020\u0005H\u00c6\u0003J\u00ef\u0001\u0010f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u0017\u001a\u00020\u00052\b\b\u0002\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\u0019\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u0015H\u00c6\u0001J\u0013\u0010g\u001a\u00020h2\b\u0010i\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010j\u001a\u00020\u0015H\u00d6\u0001J\t\u0010k\u001a\u00020\u0005H\u00d6\u0001R\u001a\u0010\f\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010\u0019\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001e\"\u0004\b\"\u0010 R\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001e\"\u0004\b$\u0010 R\u001a\u0010\n\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u001e\"\u0004\b&\u0010 R\u001a\u0010\u0011\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010\u001e\"\u0004\b(\u0010 R\u001a\u0010\u0010\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001e\"\u0004\b*\u0010 R\u001a\u0010\u001b\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010\u0018\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u001e\"\u0004\b0\u0010 R\u001a\u0010\u000f\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u001e\"\u0004\b2\u0010 R\u001a\u0010\u0017\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u001e\"\u0004\b4\u0010 R\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u001e\"\u0004\b6\u0010 R\u001a\u0010\u0016\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u001e\"\u0004\b8\u0010 R\u001a\u0010\t\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010\u001e\"\u0004\b:\u0010 R\u001a\u0010\r\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u001e\"\u0004\b<\u0010 R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u001e\"\u0004\b>\u0010 R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010,\"\u0004\b@\u0010.R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010BR\u001a\u0010\u000e\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u001e\"\u0004\bD\u0010 R\u001a\u0010\u0012\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010\u001e\"\u0004\bF\u0010 R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bG\u0010\u001e\"\u0004\bH\u0010 R\u001a\u0010\u0013\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\u001e\"\u0004\bJ\u0010 R\u001a\u0010\u001a\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bK\u0010\u001e\"\u0004\bL\u0010 R\u001a\u0010\b\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bM\u0010\u001e\"\u0004\bN\u0010 \u00a8\u0006l"}, d2 = {"Lcom/just/machine/dao/PatientBean;", "", "patientId", "", "name", "", "sex", "height", "weight", "identityCard", "birthday", "age", "BMI", "medicalRecordNumber", "predictDistances", "diseaseHistory", "currentMedications", "clinicalDiagnosis", "remark", "stride", "numberOfTrialsParticipated", "", "hospitalNumber", "floorNo", "department", "addTime", "updatedTime", "deleteTheTag", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getBMI", "()Ljava/lang/String;", "setBMI", "(Ljava/lang/String;)V", "getAddTime", "setAddTime", "getAge", "setAge", "getBirthday", "setBirthday", "getClinicalDiagnosis", "setClinicalDiagnosis", "getCurrentMedications", "setCurrentMedications", "getDeleteTheTag", "()I", "setDeleteTheTag", "(I)V", "getDepartment", "setDepartment", "getDiseaseHistory", "setDiseaseHistory", "getFloorNo", "setFloorNo", "getHeight", "setHeight", "getHospitalNumber", "setHospitalNumber", "getIdentityCard", "setIdentityCard", "getMedicalRecordNumber", "setMedicalRecordNumber", "getName", "setName", "getNumberOfTrialsParticipated", "setNumberOfTrialsParticipated", "getPatientId", "()J", "getPredictDistances", "setPredictDistances", "getRemark", "setRemark", "getSex", "setSex", "getStride", "setStride", "getUpdatedTime", "setUpdatedTime", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class PatientBean {
    @androidx.room.ColumnInfo(name = "id")
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long patientId = 0L;
    @org.jetbrains.annotations.NotNull
    private java.lang.String name;
    @org.jetbrains.annotations.NotNull
    private java.lang.String sex;
    @org.jetbrains.annotations.NotNull
    private java.lang.String height;
    @org.jetbrains.annotations.NotNull
    private java.lang.String weight;
    @org.jetbrains.annotations.NotNull
    private java.lang.String identityCard;
    @org.jetbrains.annotations.NotNull
    private java.lang.String birthday;
    @org.jetbrains.annotations.NotNull
    private java.lang.String age;
    @org.jetbrains.annotations.NotNull
    private java.lang.String BMI;
    @org.jetbrains.annotations.NotNull
    private java.lang.String medicalRecordNumber;
    @org.jetbrains.annotations.NotNull
    private java.lang.String predictDistances;
    @org.jetbrains.annotations.NotNull
    private java.lang.String diseaseHistory;
    @org.jetbrains.annotations.NotNull
    private java.lang.String currentMedications;
    @org.jetbrains.annotations.NotNull
    private java.lang.String clinicalDiagnosis;
    @org.jetbrains.annotations.NotNull
    private java.lang.String remark;
    @org.jetbrains.annotations.NotNull
    private java.lang.String stride;
    private int numberOfTrialsParticipated;
    @org.jetbrains.annotations.NotNull
    private java.lang.String hospitalNumber;
    @org.jetbrains.annotations.NotNull
    private java.lang.String floorNo;
    @org.jetbrains.annotations.NotNull
    private java.lang.String department;
    @org.jetbrains.annotations.NotNull
    private java.lang.String addTime;
    @org.jetbrains.annotations.NotNull
    private java.lang.String updatedTime;
    private int deleteTheTag;
    
    @org.jetbrains.annotations.NotNull
    public final com.just.machine.dao.PatientBean copy(long patientId, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String sex, @org.jetbrains.annotations.NotNull
    java.lang.String height, @org.jetbrains.annotations.NotNull
    java.lang.String weight, @org.jetbrains.annotations.NotNull
    java.lang.String identityCard, @org.jetbrains.annotations.NotNull
    java.lang.String birthday, @org.jetbrains.annotations.NotNull
    java.lang.String age, @org.jetbrains.annotations.NotNull
    java.lang.String BMI, @org.jetbrains.annotations.NotNull
    java.lang.String medicalRecordNumber, @org.jetbrains.annotations.NotNull
    java.lang.String predictDistances, @org.jetbrains.annotations.NotNull
    java.lang.String diseaseHistory, @org.jetbrains.annotations.NotNull
    java.lang.String currentMedications, @org.jetbrains.annotations.NotNull
    java.lang.String clinicalDiagnosis, @org.jetbrains.annotations.NotNull
    java.lang.String remark, @org.jetbrains.annotations.NotNull
    java.lang.String stride, int numberOfTrialsParticipated, @org.jetbrains.annotations.NotNull
    java.lang.String hospitalNumber, @org.jetbrains.annotations.NotNull
    java.lang.String floorNo, @org.jetbrains.annotations.NotNull
    java.lang.String department, @org.jetbrains.annotations.NotNull
    java.lang.String addTime, @org.jetbrains.annotations.NotNull
    java.lang.String updatedTime, int deleteTheTag) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public PatientBean() {
        super();
    }
    
    public PatientBean(long patientId, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String sex, @org.jetbrains.annotations.NotNull
    java.lang.String height, @org.jetbrains.annotations.NotNull
    java.lang.String weight, @org.jetbrains.annotations.NotNull
    java.lang.String identityCard, @org.jetbrains.annotations.NotNull
    java.lang.String birthday, @org.jetbrains.annotations.NotNull
    java.lang.String age, @org.jetbrains.annotations.NotNull
    java.lang.String BMI, @org.jetbrains.annotations.NotNull
    java.lang.String medicalRecordNumber, @org.jetbrains.annotations.NotNull
    java.lang.String predictDistances, @org.jetbrains.annotations.NotNull
    java.lang.String diseaseHistory, @org.jetbrains.annotations.NotNull
    java.lang.String currentMedications, @org.jetbrains.annotations.NotNull
    java.lang.String clinicalDiagnosis, @org.jetbrains.annotations.NotNull
    java.lang.String remark, @org.jetbrains.annotations.NotNull
    java.lang.String stride, int numberOfTrialsParticipated, @org.jetbrains.annotations.NotNull
    java.lang.String hospitalNumber, @org.jetbrains.annotations.NotNull
    java.lang.String floorNo, @org.jetbrains.annotations.NotNull
    java.lang.String department, @org.jetbrains.annotations.NotNull
    java.lang.String addTime, @org.jetbrains.annotations.NotNull
    java.lang.String updatedTime, int deleteTheTag) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getPatientId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    public final void setName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSex() {
        return null;
    }
    
    public final void setSex(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getHeight() {
        return null;
    }
    
    public final void setHeight(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getWeight() {
        return null;
    }
    
    public final void setWeight(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getIdentityCard() {
        return null;
    }
    
    public final void setIdentityCard(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBirthday() {
        return null;
    }
    
    public final void setBirthday(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAge() {
        return null;
    }
    
    public final void setAge(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBMI() {
        return null;
    }
    
    public final void setBMI(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMedicalRecordNumber() {
        return null;
    }
    
    public final void setMedicalRecordNumber(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPredictDistances() {
        return null;
    }
    
    public final void setPredictDistances(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDiseaseHistory() {
        return null;
    }
    
    public final void setDiseaseHistory(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrentMedications() {
        return null;
    }
    
    public final void setCurrentMedications(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getClinicalDiagnosis() {
        return null;
    }
    
    public final void setClinicalDiagnosis(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRemark() {
        return null;
    }
    
    public final void setRemark(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getStride() {
        return null;
    }
    
    public final void setStride(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final int component17() {
        return 0;
    }
    
    public final int getNumberOfTrialsParticipated() {
        return 0;
    }
    
    public final void setNumberOfTrialsParticipated(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getHospitalNumber() {
        return null;
    }
    
    public final void setHospitalNumber(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFloorNo() {
        return null;
    }
    
    public final void setFloorNo(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDepartment() {
        return null;
    }
    
    public final void setDepartment(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAddTime() {
        return null;
    }
    
    public final void setAddTime(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component22() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUpdatedTime() {
        return null;
    }
    
    public final void setUpdatedTime(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final int component23() {
        return 0;
    }
    
    public final int getDeleteTheTag() {
        return 0;
    }
    
    public final void setDeleteTheTag(int p0) {
    }
}