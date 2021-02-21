package com.asset.appwork.dto;

import com.asset.appwork.model.Lookup;
import lombok.Data;

import java.util.List;

@Data
public class LookupCategoryValues {
    Long Id;
    String category;
    List<Lookup> lookups;
}
