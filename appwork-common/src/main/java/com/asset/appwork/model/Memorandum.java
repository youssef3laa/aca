package com.asset.appwork.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Bassel on 3/1/2021.
 */
@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_Memos")
public class Memorandum {
    @Id
    Long id;
    String requestId;
    String jsonId;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "memosId")
    private List<memoValues> memoValues;
}