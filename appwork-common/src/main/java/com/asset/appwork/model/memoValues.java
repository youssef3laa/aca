package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Bassel on 3/1/2021.
 */
@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_MemosValues")
public class memoValues {
    @Id
    Long id;
    String memosId;
    String jsonKey;
    String value;

}