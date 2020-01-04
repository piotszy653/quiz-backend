package projects.user.data;

import org.springframework.data.domain.PageRequest;

public interface IPageRequestData {
    default PageRequest getDefaultPageRequest(){
        return PageRequest.of(0, 5);
    }
}
