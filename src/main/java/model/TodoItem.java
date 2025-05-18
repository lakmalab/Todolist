package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class TodoItem {
    private String title;
    private String description;
    private String completiontime;
    private Boolean isdone;
}
