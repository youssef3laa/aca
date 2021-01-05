package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Bassel on 3/1/2020.
 */
@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_Memos")
public class memorandum {
    @Id
    Long id;
    String requestId;
    String jsonId;

//    @OneToOne
//    @JoinColumn(name="Id")
//    private memoValues memoValues;
}