package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by karim on 10/28/20.
 */
@Data
public class User {
    @Id
    @Column(name="UserEntityId")
    long id;
    String DisplayName;


    public String toString(){
        return "";
    }
}
