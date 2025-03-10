package com.fusiontech.api.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paged<Content> {
    private List<Content> content;
    private Integer currentPage;
    private Integer totalPage;
}
