package todo.list.api.App.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PageableService {

    public <T> Page<T> createPageFromList(List<T> yourList, Pageable pageable) {
        Pageable defaultPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("nome")
        );
        int start = (int) defaultPageable.getOffset();
        int end = Math.min((start + defaultPageable.getPageSize()), yourList.size());

        List<T> sublist = yourList.subList(start, end);

        return new PageImpl<>(sublist, defaultPageable, yourList.size());
    }
}
