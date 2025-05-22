package system.dev.marques.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;
    private boolean empty;
}
