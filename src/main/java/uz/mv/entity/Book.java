package uz.mv.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Amonov Bunyod, пт 24.12.2021 17:08 .
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode (of = "fileId")
public class Book {
    private int id;
    private String fileId;
    private String ownerId;
    private String name;
    private String author;
    private String type;
    private String size;
    private int downloadCount;
}
