package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import java.util.Optional;

//@MappedSuperclass
@Entity
@Table(name = "OpenTextEntityIdentityComponentsIdentity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula(
        "CASE WHEN unitTypeCode IS NOT NULL " +
                "THEN 'Unit' " +
                "ELSE ( " +
                "   CASE WHEN groupCode IS NOT NULL " +
                "   then 'Group' " +
                "   ELSE ( " +
                "       CASE WHEN userId IS NOT NULL " +
                "       then 'User' " +
                "       ELSE 'Unknown' " +
                "       END ) " +
                "   END ) " +
                "END "
)
@DiscriminatorValue("Unknown")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseIdentity {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @Column(name = "Name_en")
    String nameEn;
    @Column(name = "Name_ar")
    String nameAr;
    @Column(name = "IdentityDisplayName")
    String displayName;

    public BaseIdentity() {}

    public BaseIdentity(BaseIdentity identity) {
        this.id = identity.getId();
        this.name = identity.getName();
        this.description = identity.getDescription();
        this.nameEn = identity.getNameEn();
        this.nameAr = identity.getNameAr();
        this.displayName = identity.getDisplayName();
    }

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
