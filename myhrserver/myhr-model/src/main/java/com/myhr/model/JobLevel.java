package com.myhr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class JobLevel implements Serializable {
    private Long id;

    private String name;

    private String titleLevel;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createDate;

    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobLevel jobLevel = (JobLevel) o;
        return Objects.equals(name, jobLevel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
