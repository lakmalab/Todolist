package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TodoItem {
    private String title;
    private String description;
    private Boolean isdone;
}
