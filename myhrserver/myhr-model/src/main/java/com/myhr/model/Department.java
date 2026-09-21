package com.myhr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Department implements Serializable {
    private Long id;

    private String name;

    private Long parentId;

    private String depPath;

    private Boolean enabled;

    private Boolean isParent;

    private List<Department> children;

    private Integer result;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
