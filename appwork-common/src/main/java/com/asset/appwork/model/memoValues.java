package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Bassel on 3/1/2020.
 */
@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_MemosValues")
public class memoValues {
    @Id
    Long id;
    String memosId;
    String jsonKey;
    String value;

}